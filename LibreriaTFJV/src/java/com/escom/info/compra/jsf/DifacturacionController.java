package com.escom.info.compra.jsf;

import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.jsf.util.JsfUtil;
import com.escom.info.compra.jsf.util.PaginationHelper;
import com.escom.info.compra.ejb.DifacturacionFacade;
import escom.libreria.info.cliente.jpa.Cliente;
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

@ManagedBean (name="difacturacionController")
@SessionScoped
public class DifacturacionController implements Serializable{

    private Difacturacion current;
    private DataModel items = null;
    @EJB private com.escom.info.compra.ejb.DifacturacionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    @EJB private com.escom.info.generarFactura.GeneraraFacade generaraFacade;


    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }


    public DifacturacionController() {
    }

private Cliente cliente;
    public List<Difacturacion> getLisDirfaDifacturacionsByCliente(){
         cliente=sistemaController.getCliente();
         List<Difacturacion> l=getFacade().getDireccionFacturaCliente(cliente.getId());
         return l;
    }

    public List<Difacturacion> getListDirFacturacion(){
        List<Difacturacion> l=getFacade().findAll();
        return l;
    }
    public Difacturacion getSelected() {
        if (current == null) {
            current = new Difacturacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DifacturacionFacade getFacade() {
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
        return "/difacturacion/List";
    }


    public String generarFactura(Difacturacion d){

        generaraFacade.getAsignacion(null);

        return "/carrito/Carrito";
    }

    public String prepareView(Difacturacion d) {
        current=d;
        //
        return "View";
    }

    public String prepareCreate() {
        current = new Difacturacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

            cliente=sistemaController.getCliente();
            current.setCliente(cliente);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Difacturacion Creada Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Facturacion").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Difacturacion d) {
        current=d;
        return "Edit";
    }

    public String update() {
        try {
            cliente=sistemaController.getCliente();
            current.setCliente(cliente);
            current.setRfc(current.getRfc());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Difacturacion Actualizada Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Facturacion").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Difacturacion d) {
        current=d;
        current.setCliente(d.getCliente());
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Direccion de Factura eliminada Satisfactoriamente");
        return "/difacturacion/List";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Facturacion").getString("DifacturacionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Facturacion").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Difacturacion.class)
    public static class DifacturacionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DifacturacionController controller = (DifacturacionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "difacturacionController");
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
            if (object instanceof Difacturacion) {
                Difacturacion o = (Difacturacion) object;
                return getStringKey(o.getRfc());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+DifacturacionController.class.getName());
            }
        }

    }

}
