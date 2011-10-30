package escom.libreria.info.cliente.jsf;

import escom.libreria.info.cliente.jpa.BitacoraCliente;
import escom.libreria.info.cliente.jsf.util.JsfUtil;
import escom.libreria.info.cliente.jsf.util.PaginationHelper;
import escom.libreria.info.cliente.ejb.BitacoraClienteFacade;
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

@ManagedBean (name="bitacoraClienteController")
@SessionScoped
public class BitacoraClienteController  implements Serializable{

    private BitacoraCliente current;
    private DataModel items = null;
    @EJB private escom.libreria.info.cliente.ejb.BitacoraClienteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public BitacoraClienteController() {
    }

    public BitacoraCliente getSelected() {
        if (current == null) {
            current = new BitacoraCliente();
            selectedItemIndex = -1;
        }
        return current;
    }

    private BitacoraClienteFacade getFacade() {
        return ejbFacade;
    }


    public List<BitacoraCliente> getListBitacoracliente(){
        return getFacade().findAll();
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

    public String prepareView(BitacoraCliente cliente) {
        current =cliente;//(BitacoraCliente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/bitacoraCliente/View";
    }

    public String prepareCreate() {
        current = new BitacoraCliente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BitacoraCliente").getString("BitacoraClienteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BitacoraCliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(BitacoraCliente c) {
        current = c;//(BitacoraCliente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/bitacoraCliente/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BitacoraCliente").getString("BitacoraClienteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BitacoraCliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(BitacoraCliente c) {
        current =c;//(BitacoraCliente)getItems().getRowData();
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Registro eliminado satisfactoriamente");
        return "/bitacoraCliente/List";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BitacoraCliente").getString("BitacoraClienteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BitacoraCliente").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=BitacoraCliente.class)
    public static class BitacoraClienteControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BitacoraClienteController controller = (BitacoraClienteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bitacoraClienteController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.cliente.jpa.BitacoraClientePK getKey(String value) {
            escom.libreria.info.cliente.jpa.BitacoraClientePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.cliente.jpa.BitacoraClientePK();
            key.setIdMovimiento(Integer.parseInt(values[0]));
            key.setIdCliente(values[1]);
            key.setIdArticulo(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(escom.libreria.info.cliente.jpa.BitacoraClientePK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdMovimiento());
            sb.append(SEPARATOR);
            sb.append(value.getIdCliente());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof BitacoraCliente) {
                BitacoraCliente o = (BitacoraCliente) object;
                return getStringKey(o.getBitacoraClientePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+BitacoraClienteController.class.getName());
            }
        }

    }

}
