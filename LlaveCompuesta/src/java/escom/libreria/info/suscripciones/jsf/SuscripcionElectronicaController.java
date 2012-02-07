package escom.libreria.info.suscripciones.jsf;

import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.suscripciones.SuscripcionElectronica;
import escom.libreria.info.suscripciones.SuscripcionElectronicaPK;
import escom.libreria.info.suscripciones.jsf.util.JsfUtil;
import escom.libreria.info.suscripciones.jsf.util.PaginationHelper;
import escom.libreria.info.suscripciones.ejb.SuscripcionElectronicaFacade;
import java.io.Serializable;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean (name="suscripcionElectronicaController")
@SessionScoped
public class SuscripcionElectronicaController implements Serializable{

    private SuscripcionElectronica current;
    private DataModel items = null;
    @EJB private escom.libreria.info.suscripciones.ejb.SuscripcionElectronicaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty(value="#{sistemaController.cliente}")
    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    public SuscripcionElectronicaController() {
    }

    public SuscripcionElectronica getSelected() {
        if (current == null) {
            current = new SuscripcionElectronica();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<SuscripcionElectronica> getSuscripcionElectronicaList(){
        List<SuscripcionElectronica> l=  getFacade().findAll();
        return l;
    }

    public List<SuscripcionElectronica> getSuscripcionElectronicaByCliente(){
        List<SuscripcionElectronica> l=null;
        try{
               l= getFacade().getSuscripcionesElectronicasIdCliente(cliente.getId());
        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }
    private SuscripcionElectronicaFacade getFacade() {
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

    public String prepareView(SuscripcionElectronica se) {
        current=se;
        //selectedItemIndex SuscripcionElectron= pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SuscripcionElectronica();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            SuscripcionElectronicaPK pk=new SuscripcionElectronicaPK();
            pk.setIdCliente(current.getCliente().getId());
            pk.setIdSuscripcion(current.getSuscripcion().getSuscripcionPK().getIdSuscripcion());
            current.setSuscripcionElectronicaPK(pk);
            current.setCliente(current.getCliente());
            current.setFechaFin(current.getFechaFin());
            current.setFechaInicio(current.getFechaInicio());
            current.setNoLicencias(current.getNoLicencias());
           
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Suscripcion Electronica  Creada Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Suscripcion Electronica"));
            return null;
        }
    }

    public String prepareEdit(SuscripcionElectronica se) {
        current=se;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Suscripcion Electronica actualizada Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al editar Suscripcion Electronica"));
            return null;
        }
    }

    public String destroy(SuscripcionElectronica se) {
        current=se;
       getFacade().remove(current);
       JsfUtil.addSuccessMessage("Suscripcion electronica eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("SuscripcionElectronicaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=SuscripcionElectronica.class)
    public static class SuscripcionElectronicaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SuscripcionElectronicaController controller = (SuscripcionElectronicaController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "suscripcionElectronicaController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.suscripciones.SuscripcionElectronicaPK getKey(String value) {
            escom.libreria.info.suscripciones.SuscripcionElectronicaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.suscripciones.SuscripcionElectronicaPK();
            key.setIdSuscripcion(Integer.parseInt(values[0]));
            key.setIdCliente(values[1]);
            return key;
        }

        String getStringKey(escom.libreria.info.suscripciones.SuscripcionElectronicaPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdSuscripcion());
            sb.append(SEPARATOR);
            sb.append(value.getIdCliente());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SuscripcionElectronica) {
                SuscripcionElectronica o = (SuscripcionElectronica) object;
                return getStringKey(o.getSuscripcionElectronicaPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+SuscripcionElectronicaController.class.getName());
            }
        }

    }

}
