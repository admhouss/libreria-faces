package escom.info.profesor.jsf;

import escom.info.profesor.jpa.ProfesorDocente;
import escom.info.profesor.jsf.util.JsfUtil;
import escom.info.profesor.jsf.util.PaginationHelper;
import escom.info.profesor.ejb.ProfesorDocenteFacade;
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

@ManagedBean (name="profesorDocenteController")
@SessionScoped
public class ProfesorDocenteController implements Serializable{

    private ProfesorDocente current;
    private DataModel items = null;
    @EJB private escom.info.profesor.ejb.ProfesorDocenteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProfesorDocenteController() {
    }

    public List<ProfesorDocente> getListProfesorDocente(){
     List<ProfesorDocente> p=getFacade().findAll();
     return p;
    }

    public ProfesorDocente getSelected() {
        if (current == null) {
            current = new ProfesorDocente();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProfesorDocenteFacade getFacade() {
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

    public String prepareView(ProfesorDocente p) {
        current = p;//(ProfesorDocente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ProfesorDocente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setDocumento(current.getDocumento());
            current.setEgresado(current.getEgresado());
            current.setIdProfesor(current.getIdProfesor());
            current.setProfesor(current.getProfesor());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Profesor docente crado satisfactoriamente");
            return prepareView(current);
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error al crear Profesor docente");
            return null;
        }
    }

    public String prepareEdit() {
        current = (ProfesorDocente)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.setDocumento(current.getDocumento());
            current.setEgresado(current.getEgresado());
            current.setIdProfesor(current.getIdProfesor());
            current.setProfesor(current.getProfesor());

            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Profesor docente editado satisfactoriamente");
            return "View";
        } catch (Exception e) {
             e.printStackTrace();
            JsfUtil.addErrorMessage("Error al editar profesor docente");
            return null;
        }
    }

    public String destroy(ProfesorDocente p) {
        current = p;//(ProfesorDocente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
        JsfUtil.addSuccessMessage("Profesor Docente eliminado satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/profesor").getString("ProfesorDocenteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/profesor").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=ProfesorDocente.class)
    public static class ProfesorDocenteControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProfesorDocenteController controller = (ProfesorDocenteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "profesorDocenteController");
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
            if (object instanceof ProfesorDocente) {
                ProfesorDocente o = (ProfesorDocente) object;
                return getStringKey(o.getIdProfesor());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ProfesorDocenteController.class.getName());
            }
        }

    }

}
