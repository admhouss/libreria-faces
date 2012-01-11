package escom.libreria.info.cliente.jsf;


import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.administracion.jsf.util.PaginationHelper;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.cliente.Contacto;
import escom.libreria.info.cliente.ejb.ContactoFacade;
import escom.libreria.info.login.sistema.SistemaController;
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

@ManagedBean (name="contactoController")
@SessionScoped
public class ContactoController implements Serializable {

    private Contacto current;
    private DataModel items = null;
    @EJB private ContactoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;

    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }


private Cliente cliente;
    public List<Contacto> getListContactoFromCliente(){
        cliente=sistemaController.getCliente();
        String idCliente=cliente.getId();
        List<Contacto>  l=getFacade().getObtenerContactosByCliente(idCliente);
        return l;
    }

    public ContactoController() {
    }

    public Contacto getSelected() {
        if (current == null) {
            current = new Contacto();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Contacto> getListContactos(){
        return getFacade().findAll();
    }

    private ContactoFacade getFacade() {
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
        //recreateModel();
        return "/cliente/modulo";
    }

    public String prepareView(Contacto p) {
        current = p;//(Contacto)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/contacto/View";
    }

    public String prepareCreate() {
        current = new Contacto();
        selectedItemIndex = -1;
        return "/contacto/Create";
    }

    public String create() {
        try {
           // Cliente cliente=sistemaController.getCliente();
            current.setCliente(cliente);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Contacto Creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear contacto cliente"));
            return null;
        }
    }

    public String prepareEdit(Contacto p ) {
        current = p;//(Contacto)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();

        return "/contacto/Edit";
    }

    public String update() {
        try {
          //  Cliente cliente=sistemaController.getCliente();
            current.setCliente(cliente);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Contacto Actualizado Satisfactoriamente"));
            return "/contacto/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Contacto p) {
        current = p;//(Contacto)getItems().getRowData();
        current.setCliente(cliente);
        ejbFacade.remove(current);
        JsfUtil.addSuccessMessage("Contacto eliminado satisfactoriamente");
        return "/cliente/modulo";
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
            JsfUtil.addSuccessMessage(("ContactoDeleted"));
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

    @FacesConverter(forClass=Contacto.class)
    public static class ContactoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContactoController controller = (ContactoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contactoController");
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
            if (object instanceof Contacto) {
                Contacto o = (Contacto) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ContactoController.class.getName());
            }
        }

    }

}
