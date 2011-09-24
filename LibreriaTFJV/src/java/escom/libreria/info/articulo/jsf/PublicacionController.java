package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.PublicacionFacade;
import java.io.Serializable;
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

@ManagedBean (name="publicacionController")
@SessionScoped
public class PublicacionController implements Serializable{

    private Publicacion current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.PublicacionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Publicacion> listaPublicacion;

    public List<Publicacion> getListaPublicacion() {
        return getFacade().findAll();
         //return listaPublicacion;
    }

    public void setListaPublicacion(List<Publicacion> listaPublicacion) {
        this.listaPublicacion = listaPublicacion;
    }

    public PublicacionController() {
    }

    public Publicacion getSelected() {
        if (current == null) {
            current = new Publicacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PublicacionFacade getFacade() {
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
        return "/publicacion/List";
    }

    public String prepareView(Publicacion p) {
        current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/publicacion/View";
    }

    public String prepareCreate() {
        current = new Publicacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            if(current!=null){
              getFacade().create(current);
              JsfUtil.addSuccessMessage(("Publicacion Created"));
              
              
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return prepareView(current);
    }

    public String prepareEdit(Publicacion p) {
        current=p;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";

    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Publicacion Updated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Publicacion p) {
       current=p;
       getListaPublicacion().remove(current);
       getFacade().remove(current);
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
       JsfUtil.addSuccessMessage("Publicacion Eliminada Satisfactoriamente");

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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PublicacionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Publicacion.class)
    public static class PublicacionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PublicacionController controller = (PublicacionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "publicacionController");
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
            if (object instanceof Publicacion) {
                Publicacion o = (Publicacion) object;
                return getStringKey(o.getIdDc());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+PublicacionController.class.getName());
            }
        }

    }

}
