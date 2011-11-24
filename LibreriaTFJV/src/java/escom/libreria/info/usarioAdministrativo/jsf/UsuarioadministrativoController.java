package escom.libreria.info.usarioAdministrativo.jsf;

import escom.libreria.info.usarioAdministrativo.jpa.Usuarioadministrativo;
import escom.libreria.info.usarioAdministrativo.jsf.util.JsfUtil;
import escom.libreria.info.usarioAdministrativo.jsf.util.PaginationHelper;
import escom.libreria.info.usarioAdministrativo.ejb.UsuarioadministrativoFacade;
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

@ManagedBean (name="usuarioadministrativoController")
@SessionScoped
public class UsuarioadministrativoController implements Serializable{

    private Usuarioadministrativo current;
    private DataModel items = null;
    @EJB private escom.libreria.info.usarioAdministrativo.ejb.UsuarioadministrativoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public UsuarioadministrativoController() {
    }

    public Usuarioadministrativo getSelected() {
        if (current == null) {
            current = new Usuarioadministrativo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UsuarioadministrativoFacade getFacade() {
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


    public List<Usuarioadministrativo> getListaUsuarioAdministrativo(){
        return getFacade().findAll();
    }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView(Usuarioadministrativo p) {
       current =p;
        
        return "View";
    }

    public String prepareCreate() {
        current = new Usuarioadministrativo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            Usuarioadministrativo userx=getFacade().find(current.getIdUsuario());
            if(userx==null){
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/UsuarioAdministrativo").getString("UsuarioadministrativoCreated"));
                return prepareView(current);
            }
            JsfUtil.addErrorMessage("El usuario ya se encuentra registrado");
            return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/UsuarioAdministrativo").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Usuarioadministrativo p) {
       current =p;
        
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/UsuarioAdministrativo").getString("UsuarioadministrativoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/UsuarioAdministrativo").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Usuarioadministrativo p) {
       current =p;

       getFacade().remove(current);
       // performDestroy();
        //recreateModel();
       JsfUtil.addSuccessMessage("Usuario Administrativo eliminado satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/UsuarioAdministrativo").getString("UsuarioadministrativoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/UsuarioAdministrativo").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Usuarioadministrativo.class)
    public static class UsuarioadministrativoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioadministrativoController controller = (UsuarioadministrativoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioadministrativoController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuarioadministrativo) {
                Usuarioadministrativo o = (Usuarioadministrativo) object;
                return getStringKey(o.getIdUsuario());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+UsuarioadministrativoController.class.getName());
            }
        }

    }

}
