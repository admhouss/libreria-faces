package escom.info.documento.jsf;

import escom.info.egresado.jpa.Generacion;
import escom.info.documento.jsf.util.JsfUtil;
import escom.info.documento.jsf.util.PaginationHelper;
import escom.info.egresado.ejb.GeneracionFacade;
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

@ManagedBean (name="generacionController")
@SessionScoped
public class GeneracionController  implements Serializable{

    private Generacion current;
    private DataModel items = null;
    @EJB private escom.info.egresado.ejb.GeneracionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public GeneracionController() {
    }

    public List<Generacion> getListGeneracion(){
        List<Generacion> l=getFacade().findAll();
        return l;
    }

    public Generacion getSelected() {
        if (current == null) {
            current = new Generacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GeneracionFacade getFacade() {
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

    public String prepareView(Generacion g) {
        current = g;//(Generacion)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Generacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setAnio(current.getAnio());
            current.setDescripcion(current.getDescripcion());
            current.setEgresadoList(current.getEgresadoList());
            current.setId(current.getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Generacion creada satisfactoriamente");
            return prepareView(current);
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error al crear Generacion");
            return null;
        }
    }

    public String prepareEdit(Generacion g) {
        current = g;//(Generacion)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.setAnio(current.getAnio());
            current.setDescripcion(current.getDescripcion());
            current.setEgresadoList(current.getEgresadoList());
            current.setId(current.getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Generacion editada exitosamente");
            return prepareView(current);
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error al crear");
            return null;
        }
    }

    public String destroy(Generacion g ) {
        current = g;//(Generacion)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
       // recreateModel();
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Generacion Eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/egresado").getString("GeneracionDeleted"));
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

    @FacesConverter(forClass=Generacion.class)
    public static class GeneracionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GeneracionController controller = (GeneracionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "generacionController");
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
            if (object instanceof Generacion) {
                Generacion o = (Generacion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+GeneracionController.class.getName());
            }
        }

    }

}
