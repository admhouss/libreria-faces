package escom.libreria.info.generadorIDpromociones.jsf;

import escom.libreria.info.generadorIDpromociones.GeneradorContador;
import escom.libreria.info.generadorIDpromociones.jsf.util.JsfUtil;
import escom.libreria.info.generadorIDpromociones.jsf.util.PaginationHelper;
import escom.libreria.info.generadorIDpromociones.ejb.GeneradorContadorFacade;

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

@ManagedBean (name="generadorContadorController")
@SessionScoped
public class GeneradorContadorController {

    private GeneradorContador current;
    private DataModel items = null;
    @EJB private escom.libreria.info.generadorIDpromociones.ejb.GeneradorContadorFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public GeneradorContadorController() {
    }

    public GeneradorContador getSelected() {
        if (current == null) {
            current = new GeneradorContador();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GeneradorContadorFacade getFacade() {
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

    public String prepareView() {
        current = (GeneradorContador)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new GeneradorContador();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/generadorID").getString("GeneradorContadorCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/generadorID").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (GeneradorContador)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/generadorID").getString("GeneradorContadorUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/generadorID").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (GeneradorContador)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/generadorID").getString("GeneradorContadorDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/generadorID").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=GeneradorContador.class)
    public static class GeneradorContadorControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GeneradorContadorController controller = (GeneradorContadorController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "generadorContadorController");
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
            if (object instanceof GeneradorContador) {
                GeneradorContador o = (GeneradorContador) object;
                return getStringKey(o.getContador());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+GeneradorContadorController.class.getName());
            }
        }

    }

}
