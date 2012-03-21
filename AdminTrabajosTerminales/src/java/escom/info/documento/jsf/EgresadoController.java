package escom.info.documento.jsf;

import escom.info.egresado.jpa.Egresado;
import escom.info.documento.jsf.util.JsfUtil;
import escom.info.documento.jsf.util.PaginationHelper;
import escom.info.egresado.ejb.EgresadoFacade;
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

@ManagedBean (name="egresadoController")
@SessionScoped
public class EgresadoController implements Serializable{

    private Egresado current;
    private DataModel items = null;
    @EJB private escom.info.egresado.ejb.EgresadoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public EgresadoController() {
    }

    public Egresado getSelected() {
        if (current == null) {
            current = new Egresado();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Egresado> getListEgresados(){
        List<Egresado>  l=getFacade().findAll();
        return l;

    }
    private EgresadoFacade getFacade() {
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
    //    recreateModel();
        return "List";
    }

    public String prepareView(Egresado e) {
        current=e;


        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Egresado();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setCorreo(current.getCorreo());
            current.setGeneracion(current.getGeneracion());
            current.setId(current.getId());
            current.setNombre(current.getNombre());
            current.setProfesorDocenteList(current.getProfesorDocenteList());

            getFacade().create(current);
            JsfUtil.addSuccessMessage(("El egresado ha sido creado satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(("Error al crear Egresado"));
            return null;
        }
    }

    public String prepareEdit(Egresado e ) {
        current=e;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("El egresado ha sido creado satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(("Eror al crear egresado"));
            return null;
        }
    }

    public String destroy(Egresado e) {
        current=e;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
       // recreateModel();
        getFacade().remove(e);
        JsfUtil.addSuccessMessage("Egresado a sido Eliminado Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/egresado").getString("EgresadoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/egresado").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Egresado.class)
    public static class EgresadoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EgresadoController controller = (EgresadoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "egresadoController");
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
            if (object instanceof Egresado) {
                Egresado o = (Egresado) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+EgresadoController.class.getName());
            }
        }

    }

}
