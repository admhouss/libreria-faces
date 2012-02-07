package escom.libreria.info.suscripciones.jsf;

import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.login.sistema.SistemaController;
import escom.libreria.info.suscripciones.SuscripcionCliente;
import escom.libreria.info.suscripciones.SuscripcionClientePK;
import escom.libreria.info.suscripciones.jsf.util.JsfUtil;
import escom.libreria.info.suscripciones.jsf.util.PaginationHelper;
import escom.libreria.info.suscripciones.ejb.SuscripcionClienteFacade;
import java.io.Serializable;
import java.util.List;
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

@ManagedBean (name="suscripcionClienteController")
@SessionScoped
public class SuscripcionClienteController implements Serializable{

    private SuscripcionCliente current;
    private DataModel items = null;
    @EJB private escom.libreria.info.suscripciones.ejb.SuscripcionClienteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty(value="#{sistemaController.cliente}")
    private Cliente cliente;
   
    public SuscripcionClienteController() {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }






    public SuscripcionCliente getSelected() {
        if (current == null) {
            current = new SuscripcionCliente();
            selectedItemIndex = -1;
        }
        return current;
    }




    public List<SuscripcionCliente> getListSuscripcionesCliente(){

        List<SuscripcionCliente> l=getFacade().findAll();
        return l;
    }

    public List<SuscripcionCliente> getListSuscripcionesByCliente(){

          
        List<SuscripcionCliente> l=null;
        try{
        l=getFacade().getSuscripcionDelCliente(cliente.getId());
        }catch(Exception e){
            e.printStackTrace();

        }
        return l;
    }
    private SuscripcionClienteFacade getFacade() {
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

    public String prepareView(SuscripcionCliente s) {
        current=s;//
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SuscripcionCliente();
      
        return "Create";
    }

    public String create() {
        try {
           SuscripcionClientePK suscripcionClientePK=new SuscripcionClientePK();
           suscripcionClientePK.setIdCliente(current.getCliente().getId());
           suscripcionClientePK.setIdSuscripcion(current.getSuscripcion().getSuscripcionPK().getIdSuscripcion());
           current.setSuscripcionClientePK(suscripcionClientePK);
           current.setArticulo(current.getArticulo());
           current.setCliente(current.getCliente());
           current.setFechaEnvio(current.getFechaEnvio());
           current.setEstadoEnvio(current.getEstadoEnvio());
           getFacade().create(current);
           JsfUtil.addSuccessMessage(("Suscripcion Cliente creada Satisfactoriamente"));
           return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear suscripcion"));
            return null;
        }
    }

    public String prepareEdit(SuscripcionCliente s) {
        current=s;//
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Suscripcion Cliente actualizada Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar suscripcion cliente"));
            return null;
        }
    }

       public String destroy(SuscripcionCliente s) {
        current=s;//
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Suscripcion cliente eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("SuscripcionClienteDeleted"));
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

    @FacesConverter(forClass=SuscripcionCliente.class)
    public static class SuscripcionClienteControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SuscripcionClienteController controller = (SuscripcionClienteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "suscripcionClienteController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.suscripciones.SuscripcionClientePK getKey(String value) {
            escom.libreria.info.suscripciones.SuscripcionClientePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.suscripciones.SuscripcionClientePK();
            key.setIdCliente(values[0]);
            key.setIdSuscripcion(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.suscripciones.SuscripcionClientePK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdCliente());
            sb.append(SEPARATOR);
            sb.append(value.getIdSuscripcion());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SuscripcionCliente) {
                SuscripcionCliente o = (SuscripcionCliente) object;
                return getStringKey(o.getSuscripcionClientePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+SuscripcionClienteController.class.getName());
            }
        }

    }

}
