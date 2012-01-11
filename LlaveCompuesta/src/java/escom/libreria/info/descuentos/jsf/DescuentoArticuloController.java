package escom.libreria.info.descuentos.jsf;


import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;

import escom.libreria.info.descuentos.DescuentoArticulo;
import escom.libreria.info.descuentos.ejb.DescuentoArticuloFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean (name="descuentoArticuloController")
@SessionScoped
public class DescuentoArticuloController implements Serializable {

    private DescuentoArticulo current;
    private DataModel items = null;
    @EJB private DescuentoArticuloFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String correo;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public DescuentoArticuloController() {
    }

   public List<DescuentoArticulo> getListaDescuentoArticulo(){
       return getFacade().findAll();
   }
    public DescuentoArticulo getSelected() {
        if (current == null) {
            current = new DescuentoArticulo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DescuentoArticuloFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView(DescuentoArticulo p) {
      current=p;

        return "View";
    }

    public String prepareCreate() {
        current = new DescuentoArticulo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setIdArticulo(current.getArticulo().getId());
            DescuentoArticulo  descuento=getFacade().find(current.getIdArticulo());
            if(descuento==null){
               current.setFechaFinal(current.getFechaFinal());
               current.setFechaInicio(current.getFechaInicio());
               current.setArticulo(current.getArticulo());
               getFacade().create(current);
               JsfUtil.addSuccessMessage(("Descuento Articulo Creado satisfactoriamente"));
              return prepareView(current);
            }else{
                JsfUtil.addErrorMessage("Ya existe un descuento para este articulo");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Descuento").getString("PersistenceErrorOccured"));
            return null;
        }
        return null;
    }

    public String prepareEdit(DescuentoArticulo p) {
      current=p;
      current.setArticulo(current.getArticulo());
      current.setIdArticulo(current.getArticulo().getId());
      return "Edit";
    }

    public String update() {
        try {
           // DescuentoArticulo descuento=getFacade().find(current.getIdArticulo());
            //if(descuento!=null){
              //  JsfUtil.addErrorMessage("Ya existe un descuento para este articulo");
               // return null;
            //}

            current.setArticulo(current.getArticulo());
            current.setDescuento(current.getDescuento());
            current.setFechaInicio(current.getFechaInicio());
            current.setFechaFinal(current.getFechaFinal());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Descuento Articulo Actualizado"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Descuento").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(DescuentoArticulo p) {
      current=p;

        //performDestroy();
        //recreateModel();
      JsfUtil.addSuccessMessage("Descuento Eliminado Satisfactoriamente");
        getFacade().remove(current);
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Descuento").getString("DescuentoArticuloDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Descuento").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass=DescuentoArticulo.class)
    public static class DescuentoArticuloControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DescuentoArticuloController controller = (DescuentoArticuloController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "descuentoArticuloController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DescuentoArticulo) {
                DescuentoArticulo o = (DescuentoArticulo) object;
                return getStringKey(o.getIdArticulo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+DescuentoArticuloController.class.getName());
            }
        }

    }

}
