package com.escom.info.temacliente.jsf;

import com.escom.info.preferenciaCliente.jpa.TemaCliente;
import com.escom.info.temacliente.jsf.util.JsfUtil;
import com.escom.info.temacliente.jsf.util.PaginationHelper;
import com.escom.info.temacliente.ejb.TemaClienteFacade;
import escom.libreria.info.cliente.jsf.ClienteController;
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

@ManagedBean (name="temaClienteController")
@SessionScoped
public class TemaClienteController implements Serializable{

    private TemaCliente current;
    private DataModel items = null;
    @EJB private com.escom.info.temacliente.ejb.TemaClienteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty(value="#{sistemaController.cliente.id}")
    private String cliente;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }



    public TemaClienteController() {
    }

    public TemaCliente getSelected() {
        if (current == null) {
            current = new TemaCliente();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<TemaCliente> getTemaClientes(){
        List<TemaCliente> l=getFacade().getListALL();
        return l;

   }


   public List<TemaCliente> getListTemaClienteToID(){
      List<TemaCliente> l=getFacade().getListaTemaCliente(cliente);
        return l;
   }
    private TemaClienteFacade getFacade() {
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

    public String prepareView(TemaCliente t) {
        current =t;// (TemaCliente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TemaCliente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/TemaCliente").getString("TemaClienteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/TemaCliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(TemaCliente t) {
        current = t;//(TemaCliente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/TemaCliente").getString("TemaClienteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/TemaCliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(TemaCliente t) {
        current =t;// (TemaCliente)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Tema Cliente eliminado Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/TemaCliente").getString("TemaClienteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/TemaCliente").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=TemaCliente.class)
    public static class TemaClienteControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TemaClienteController controller = (TemaClienteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "temaClienteController");
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
            if (object instanceof TemaCliente) {
                TemaCliente o = (TemaCliente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+TemaClienteController.class.getName());
            }
        }

    }

}
