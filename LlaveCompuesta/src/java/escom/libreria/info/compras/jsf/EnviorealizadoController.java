package escom.libreria.info.compras.jsf;

import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Enviorealizado;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.compras.jsf.util.PaginationHelper;
import escom.libreria.info.compras.ejb.EnviorealizadoFacade;
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

@ManagedBean (name="enviorealizadoController")
@SessionScoped
public class EnviorealizadoController implements Serializable{

    private Enviorealizado current;
    private DataModel items = null;
    @EJB private escom.libreria.info.compras.ejb.EnviorealizadoFacade ejbFacade;
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

    public EnviorealizadoController() {
    }


    public List<Enviorealizado> getListEnvioRealizadosByCliente(){
       List<Enviorealizado> l= getFacade().getEnviosRealizadosByCliente(cliente.getId());
       return l;
    }
    public List<Enviorealizado> getListEnvioRealizados(){
       List<Enviorealizado> l= getFacade().findAll();
       return l;
    }

    public Enviorealizado getSelected() {
        if (current == null) {
            current = new Enviorealizado();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EnviorealizadoFacade getFacade() {
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

    public String prepareView(Enviorealizado er) {
       current=er;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Enviorealizado();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
           current.setArtoculo(current.getArtoculo());
           current.setFechaRecibo(current.getFechaRecibo());
           current.setPedido(current.getPedido());
           current.setIdPedido(current.getPedido().getPedidoPK().getIdPedido());
           current.setObservaciones(current.getObservaciones());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Envio realizado Satisfactoriamente"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Envio realizado"));
            return null;
        }
    }

    public String prepareEdit(Enviorealizado er ) {
       current=er;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Envior ealizado Actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar"));
            return null;
        }
    }

    public String destroy(Enviorealizado er) {
       current=er;
       getFacade().remove(er);
       JsfUtil.addSuccessMessage("Envio Realizado Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("EnviorealizadoDeleted"));
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

    @FacesConverter(forClass=Enviorealizado.class)
    public static class EnviorealizadoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EnviorealizadoController controller = (EnviorealizadoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "enviorealizadoController");
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
            if (object instanceof Enviorealizado) {
                Enviorealizado o = (Enviorealizado) object;
                return getStringKey(o.getIdPedido());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+EnviorealizadoController.class.getName());
            }
        }

    }

}
