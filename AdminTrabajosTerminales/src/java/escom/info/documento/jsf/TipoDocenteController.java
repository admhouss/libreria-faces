package escom.info.documento.jsf;

import escom.info.documento.jpa.TipoDocente;
import escom.info.documento.jsf.util.JsfUtil;
import escom.info.documento.jsf.util.PaginationHelper;
import escom.info.documento.ejb.TipoDocenteFacade;
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

@ManagedBean (name="tipoDocenteController")
@SessionScoped
public class TipoDocenteController implements Serializable{

    private TipoDocente current;
    private DataModel items = null;
    @EJB private escom.info.documento.ejb.TipoDocenteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public TipoDocenteController() {
    }

    public TipoDocente getSelected() {
        if (current == null) {
            current = new TipoDocente();
            selectedItemIndex = -1;
        }
        return current;
    }
  public List<TipoDocente> getListTipoDocumento(){
      List<TipoDocente> l=getFacade().findAll();
      return l;
  }
    private TipoDocenteFacade getFacade() {
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

    public String prepareView(TipoDocente t) { //ingualar el objeto a current
        current = t;//(TipoDocente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TipoDocente(); //inicializa
        //selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

            //agrgar esto
            current.setDescripcion(current.getDescripcion());
            current.setNombre(current.getNombre());
            current.setId(current.getId());

            //current.setId(current.selectedItemIndex);

            getFacade().create(current); //insertas la informacion
            JsfUtil.addSuccessMessage(("Tipo Docente Creado Saatisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error al crear Tipo Docente");
            return null;
        }
    }

    public String prepareEdit(TipoDocente t ) {
        current = t; //aqui tambien
        
        return "Edit";
    }

    public String update() {
        try {
            current.setDescripcion(current.getDescripcion());
            current.setNombre(current.getNombre());
            current.setId(current.getId());

            getFacade().edit(current);
            JsfUtil.addSuccessMessage("TipoDocente editado satisfactoriamente");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error al editar Tipo Docente");
            return null;
        }
    }

    public String destroy(TipoDocente t) {
        current = t; // aqui tambien
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Tipo Docente"
                + " eliminado satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/documento").getString("TipoDocenteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/documento").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=TipoDocente.class)
    public static class TipoDocenteControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoDocenteController controller = (TipoDocenteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoDocenteController");
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
            if (object instanceof TipoDocente) {
                TipoDocente o = (TipoDocente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+TipoDocenteController.class.getName());
            }
        }

    }

}
