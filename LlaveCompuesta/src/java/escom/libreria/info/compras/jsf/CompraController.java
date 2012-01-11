package escom.libreria.info.compras.jsf;

import com.paypal.jsf.CompraDTO;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.compras.jsf.util.PaginationHelper;
import escom.libreria.info.compras.ejb.CompraFacade;
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
    @EJB private escom.libreria.info.compras.ejb.CompraFacade ejbFacade;
     @EJB private escom.libreria.info.compras.ejb.PedidoFacade pedidoFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
     @ManagedProperty("#{sistemaController}")
     private SistemaController sistemaController;
     @ManagedProperty("#{direnvioController}")
     private DirenvioController direnvioController;

    public DirenvioController getDirenvioController() {
        return direnvioController;
    }

    public void setDirenvioController(DirenvioController direnvioController) {
        this.direnvioController = direnvioController;
    }


    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }




    public List<Compra> getListCompras(){
       String idCliente=sistemaController.getCliente().getId();
       List<Compra> l=null;
       l=getFacade().getComprasByCliente(idCliente);
       return l;
    }
    private int  getIDPedidoByCliente(){
        int idPedido=0;
        try{
            Cliente cliente=sistemaController.getCliente();
            idPedido=pedidoFacade.buscarIdPeidoMaximo(cliente.getId(),"PROCESANDO");
           
        }catch(Exception e){

            JsfUtil.addErrorMessage("Error al obtener pedidos by cliente");
        }

        return idPedido;
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

    public List<Pedido> listpedioBycompra;

    public List<Pedido> getListpedioBycompra() {
        return listpedioBycompra;
    }

    public void setListpedioBycompra(List<Pedido> listpedioBycompra) {
        this.listpedioBycompra = listpedioBycompra;
    }


    public String prepareView(Compra p) {
        current = p;
        int idPedido=current.getIdPedido();
        listpedioBycompra=pedidoFacade.getListaPedidosByidPedios(idPedido);
        return "/compra/detalleCompra/Listp";
    }

    public String prepareCreate() {
        current = new Compra();
        selectedItemIndex = -1;
        return "Create";
    }

    public String comprar_deposito(){
 try{
          int pedido=getIDPedidoByCliente();
          String idCliente=sistemaController.getCliente().getId();
          CompraDTO compraTOTAL = pedidoFacade.getSuperTotal(pedido);

          Compra compra=new Compra();

          compra.setDescuento(compraTOTAL.getDescuento());
          compra.setImpuesto(compraTOTAL.getImpuesto());
          compra.setPagoNeto(compraTOTAL.getTotalMonto());
          compra.setPagoTotal(compraTOTAL.getTotalMonto());
          compra.setCostoEnvio(BigDecimal.ZERO);
          compra.setCuenta(current.getCuenta());
          compra.setIdPedido(pedido);
          compra.setFecha(new Date());
          compra.setIdCliente(idCliente);
         
          compra.setObservaciones("PROCESANDO");
          compra.setFechaEnvio(new Date());
          compra.setFechaEnvio(new Date());
          compra.setEstado("CONFIRMADO");
          compra.setCcd(current.getCcd());
          compra.setCuenta(current.getCuenta());
          compra.setCostoEnvio(BigDecimal.ZERO);
          compra.setNoAutorizacion(current.getNoAutorizacion());
          compra.setNoReferencia(current.getNoReferencia());
          compra.setTipoPago("DEPOSITO");
         
          getFacade().create(compra);
          pedidoFacade.cambiarEstadoPedidoAll(pedido, "CONFIRMADO");
          JsfUtil.addSuccessMessage("PEDIDO CONFIRMADO SATISFACTORIAMENTE");
        }catch(Exception e){
              JsfUtil.addErrorMessage("Error al confirmar pedido");
              return null;
        }

return "/compra/CompradoDeposito";
         
    }

    public String create() {
        try {
          int pedido=getIDPedidoByCliente();
          String idCliente=sistemaController.getCliente().getId();
          CompraDTO compraTOTAL = pedidoFacade.getSuperTotal(pedido);

          Compra compra=new Compra();

          compra.setDescuento(compraTOTAL.getDescuento());
          compra.setImpuesto(compraTOTAL.getImpuesto());
          compra.setPagoNeto(compraTOTAL.getTotalMonto());
          compra.setPagoTotal(compraTOTAL.getTotalMonto());
          compra.setCostoEnvio(BigDecimal.ZERO);//CAMBIAR COSTO DE ENVIO
          compra.setCuenta(" ");
          compra.setIdPedido(pedido);
          compra.setFecha(new Date());
          compra.setIdCliente(idCliente);

          compra.setObservaciones("PROCESANDO");
          compra.setFechaEnvio(new Date());
          compra.setFechaEnvio(new Date());
          compra.setEstado("CONFIRMADO");
          compra.setCcd(0);
          compra.setCuenta(" ");
          compra.setCostoEnvio(BigDecimal.ZERO);
          compra.setNoAutorizacion(" ");
          compra.setNoReferencia(" ");
          compra.setTipoPago("ELECTRONICO");
          getFacade().create(compra);
          pedidoFacade.cambiarEstadoPedidoAll(pedido, "CONFIRMADO");
          JsfUtil.addSuccessMessage("PEDIDO CONFIRMADO SATISFACTORIAMENTE");
      
          return "/paypal/Create";
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ("Error al confirmar pedido"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Compra)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/compras").getString("CompraUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/compras").getString("PersistenceErrorOccured"));
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/compras").getString("CompraDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/compras").getString("PersistenceErrorOccured"));
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
