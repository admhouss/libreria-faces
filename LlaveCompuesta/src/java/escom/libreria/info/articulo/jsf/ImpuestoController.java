package escom.libreria.info.articulo.jsf;


import escom.libreria.info.articulo.Impuesto;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.ImpuestoFacade;
import escom.libreria.info.facturacion.Articulo;
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

@ManagedBean (name="impuestoController")
@SessionScoped
public class ImpuestoController implements Serializable {

    private Impuesto current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.ImpuestoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;


    public ImpuestoController() {
    }

    public Impuesto getSelected() {
        if (current == null) {
            current = new Impuesto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private List<Impuesto> listaImpuesto;

    public void setListaImpuesto(List<Impuesto> listaImpuesto) {
        this.listaImpuesto = listaImpuesto;
    }


    public void prepareSeleccionArticulo(Articulo articulo){
        getSelected().setArticulo(articulo);
        JsfUtil.addSuccessMessage("ARTICULO SELECCIONADO SATISFACTORIAMENTE");
    }
    public List<Impuesto> getListaImpuesto(){

        listaImpuesto= getFacade().findAll();
        setListaImpuesto(listaImpuesto);
        return listaImpuesto;
    }

    private ImpuestoFacade getFacade() {
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
        return "/impuesto/List";
    }

    public String prepareView(Impuesto p) {
        current = p;//(Impuesto)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Impuesto();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
       // List<Impuesto> impuestoPrueba=null;
        try {

            current.setArticulo(current.getArticulo());
         //   impuestoPrueba=getFacade().buscarImpuestoByarticulo(current.getArticulo());
           // if(impuestoPrueba==null|| impuestoPrueba.isEmpty()){
                current.setArticulo(current.getArticulo());
                current.setDescripcion(current.getDescripcion());
                current.setMontoImpuesto(current.getMontoImpuesto());
                getFacade().create(current);
                JsfUtil.addSuccessMessage(("Impuesto Createado Satisfactoriamente"));
                return prepareView(current);
            //}
            //JsfUtil.addErrorMessage("Ya existe un impuesto para este articulo");
            //return "/impuesto/Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear el impuesto"));
            return null;
        }
    }

    public String prepareEdit(Impuesto p) {
        current = p;//(Impuesto)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        current.setArticulo(current.getArticulo());

        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Impuesto actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar impuesto"));
            return null;
        }
    }

    public String destroy(Impuesto p) {
        current =p;// (Impuesto)getItems().getRowData();
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Impuesto eliminado satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Descuento").getString("ImpuestoDeleted"));
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

    @FacesConverter(forClass=Impuesto.class)
    public static class ImpuestoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ImpuestoController controller = (ImpuestoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "impuestoController");
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
            if (object instanceof Impuesto) {
                Impuesto o = (Impuesto) object;
                return getStringKey(o.getArticulo().getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ImpuestoController.class.getName());
            }
        }

    }

}
