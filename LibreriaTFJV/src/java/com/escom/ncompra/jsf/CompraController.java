package com.escom.ncompra.jsf;

import com.escom.info.compra.Compra;
import com.escom.info.compra.Pedido;
import com.escom.info.compra.jsf.DifacturacionController;
import com.escom.info.compra.jsf.PedidoController;
import com.escom.ncompra.jsf.util.JsfUtil;
import com.escom.ncompra.jsf.util.PaginationHelper;
import com.escom.ncompra.ejb.CompraFacade;
import com.paypal.jsf.CompraDTO;
import escom.libreria.info.contacto.jsf.DirenvioController;
import escom.libreria.info.login.sistema.SistemaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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

@ManagedBean (name="compraController")
@SessionScoped
public class CompraController implements Serializable{

    private Compra current;
    private DataModel items = null;
    @EJB private com.escom.ncompra.ejb.CompraFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{direnvioController}")
    private DirenvioController direnvioController;
    @ManagedProperty("#{difacturacionController}")
    private DifacturacionController difacturacionController;
    @ManagedProperty("#{pedidoController}")
    private PedidoController pedidoController;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;

    public SistemaController getSistemaController() {
        return sistemaController;
    }

    private boolean requiereFactura;

    public boolean isRequiereFactura() {
        return requiereFactura;
    }

    public void setRequiereFactura(boolean requiereFactura) {
        this.requiereFactura = requiereFactura;
    }


    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }


    @EJB private com.escom.info.compra.ejb.PedidoFacade pedidoFacade;

    public DifacturacionController getDifacturacionController() {
        return difacturacionController;
    }

    public void setDifacturacionController(DifacturacionController difacturacionController) {
        this.difacturacionController = difacturacionController;
    }

    public DirenvioController getDirenvioController() {
        return direnvioController;
    }

    public void setDirenvioController(DirenvioController direnvioController) {
        this.direnvioController = direnvioController;
    }

    public PedidoController getPedidoController() {
        return pedidoController;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    
    public List<Compra> getListCompra(){

        List<Compra> l=getFacade().findAll();
        return l;
    }

    public CompraController() {
    }

    public Compra getSelected() {
        if (current == null) {
            current = new Compra();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CompraFacade getFacade() {
        return ejbFacade;
    }

    public List<Compra> getListCompraPorCliente(){
        String keyCliente=sistemaController.getCliente().getId();
        List<Compra> l=getFacade().buscarCompraByCliente(keyCliente);
        return l;
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

    public String prepareView(Compra c) {
        current = c;//Compra)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/compra/View";
    }

    public String prepareCreate() {
        current = new Compra();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

             Date hoy=pedidoFacade.getHoy();
             String keyCliente=sistemaController.getCliente().getId();

            Pedido Rootpedido=pedidoFacade.getListPedidoHotByCliernteOne(keyCliente, hoy);
            if(getFacade().buscarCompra(Rootpedido.getPedidoPK().getIdPedido())==false){

                CompraDTO result = pedidoFacade.getSuperTotal(Rootpedido.getPedidoPK().getIdPedido());

                current.setIdPedido(Rootpedido.getPedidoPK().getIdPedido());
                current.setDescuento(result.getDescuento());
                current.setImpuesto(result.getImpuesto());
                current.setPagoNeto(result.getTotalMonto());
                current.setPagoTotal(result.getTotalMonto());
                current.setFecha(new Date());
                current.setEstado(current.getEstado());
                current.setIdcliente(keyCliente);
                current.setTipoEnvio("CAMPO INUTIL");
                //current.setObservaciones("PROCESANDO");
                //current.setFechaEnvio(new Date());
                getFacade().create(current);
                JsfUtil.addSuccessMessage(("Finalise su compra dando Click en el boton Comprar Ahora"));
                return "/paypal/Create";
            }
            else{
            JsfUtil.addSuccessMessage(("Ya existe una compra registrada"));
            return "/paypal/Create";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/micompra").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Compra c) {
        current =c;// (Compra)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/compra/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/micompra").getString("CompraUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/micompra").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Compra)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/micompra").getString("CompraDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/micompra").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Compra.class)
    public static class CompraControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompraController controller = (CompraController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "compraController");
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
            if (object instanceof Compra) {
                Compra o = (Compra) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+CompraController.class.getName());
            }
        }

    }

}
