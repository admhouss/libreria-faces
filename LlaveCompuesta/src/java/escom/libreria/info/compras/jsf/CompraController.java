package escom.libreria.info.compras.jsf;

import com.paypal.jsf.CompraDTO;
import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.AlmacenPedido;
import escom.libreria.info.articulo.AlmacenPedidoPK;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Direnvio;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.compras.jsf.util.PaginationHelper;
import escom.libreria.info.compras.ejb.CompraFacade;
import escom.libreria.info.encriptamientoMD5.EncriptamientoImp;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.login.sistema.SistemaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
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
import org.apache.log4j.Logger;

@ManagedBean (name="compraController")
@SessionScoped
public class CompraController implements Serializable{

    private Compra current;
    private DataModel items = null;
    private Integer facturar;
    @EJB private escom.libreria.info.compras.ejb.CompraFacade ejbFacade;
    @EJB private escom.libreria.info.compras.ejb.PedidoFacade pedidoFacade;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenFacade almacenFacade;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenPedidoFacade almacenPedidoFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
     @ManagedProperty("#{sistemaController}")
     private SistemaController sistemaController;
     @ManagedProperty("#{direnvioController}")
     private DirenvioController direnvioController;

private static  Logger logger = Logger.getLogger(CompraController.class);


    public DirenvioController getDirenvioController() {
        return direnvioController;
    }

    public void setDirenvioController(DirenvioController direnvioController) {
        this.direnvioController = direnvioController;
    }

    public Integer getFacturar() {
        return facturar;
    }

    public void setFacturar(Integer facturar) {
        this.facturar = facturar;
    }

    

    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }

private String pedidoCancelado;

    public String getPedidoCancelado() {
        return pedidoCancelado;
    }

    public void setPedidoCancelado(String pedidoCancelado) {
        this.pedidoCancelado = pedidoCancelado;
    }


public String borrar(){
    try{
    getFacade().cambiarEstadoCompra(pedidoID, "CANCELADO");
    return "/carrito/Carrito";
    }catch(Exception e){
        e.printStackTrace();
        JsfUtil.addErrorMessage("Error al cacnelar carrito");
    }
      return "/carrito/Carrito";
}

private String pedidoTocandelar;
     @PostConstruct
    public void init() {
         int idPedido;
        String[] arreglo;

        

            if(getPedidoCancelado()!=null && !getPedidoCancelado().trim().equals("")){

              
               try{

                   System.out.println("repintando url"+getPedidoCancelado());
                         EncriptamientoImp encriptar=new EncriptamientoImp();
                         byte[] resultado=encriptar.hexToBytes(getPedidoCancelado());
                         String decodificado=encriptar.decrypt(resultado);

                         System.out.println("decodificado"+decodificado);

                         /*arreglo=decodificado.split("|");
                         pedidoTocandelar=arreglo[1];
                          * */
                         pedidoID=Integer.parseInt(decodificado);
                         pedidoFacade.cambiarEstadoPedidoAll(pedidoID,"CANCELADO");
                         getFacade().cambiarEstadoCompra(pedidoID,"CANCELADO");
                         JsfUtil.addSuccessMessage("Compra cancelada Satisfactoriamente");
                         
                }catch(Exception e){
                  
                 JsfUtil.addErrorMessage("Error al cancelar el pedido");
                }



             }


        
    }
    public List<Compra> getListCompras(){
       List<Compra> l=null;
       try{
       String idCliente=sistemaController.getCliente().getId();
       
       l=getFacade().getComprasByCliente(idCliente);
        }catch(Exception e){
         logger.info("Error cliente intento acceder a su compras pero no esta logeado");
        }
       return l;
    }

    public List<Compra> getListaComprasSistema(){
        List<Compra> l= getFacade().findAll();
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

    public int getPedidoID() {
        return pedidoID;
    }

    public void setPedidoID(int pedidoID) {
        this.pedidoID = pedidoID;
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
          Direnvio dienvio = direnvioController.getDireccionEnvioSelected();
          CompraDTO compraTOTAL = pedidoFacade.getSuperTotal(pedido);
          System.out.println("REQUIERE FACTURA"+getFacturar());

          Compra compra=new Compra();
          compra.setReqFactura(getFacturar());
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
          compra.setFechaEnvio(current.getFechaEnvio());
          compra.setEstado("CONFIRMADO");
          compra.setCcd(current.getCcd());
          compra.setCuenta(current.getCuenta());
          compra.setCostoEnvio(BigDecimal.ZERO);
          compra.setNoAutorizacion(current.getNoAutorizacion());
          compra.setNoReferencia(current.getNoReferencia());
          compra.setTipoPago("DEPOSITO");
          compra.setDireccionEnvio(String.valueOf(dienvio.getId()));
          getFacade().create(compra);
          pedidoFacade.cambiarEstadoPedidoAll(pedido, "CONFIRMADO");
          JsfUtil.addSuccessMessage("PEDIDO CONFIRMADO SATISFACTORIAMENTE");
        }catch(Exception e){
              JsfUtil.addErrorMessage("Error al confirmar pedido");
              return null;
        }

return "/compra/CompradoDeposito";
         
    }

        public void descontadorAlmacen(Articulo articulo){

              Almacen almacen=almacenFacade.find(articulo.getId());
              AlmacenPedido ap=new AlmacenPedido();
              AlmacenPedidoPK pk=new AlmacenPedidoPK();

              if(almacen.getExistencia()!=0){

               almacen.setExistencia(almacen.getExistencia()-1);
               almacenFacade.edit(almacen);
               almacenPedidoFacade.create(ap);
              }

    }


    private BigDecimal precioTotalFinal;
    private int pedidoID;

    public BigDecimal getPrecioTotalFinal() {
        return precioTotalFinal;
    }

    public void setPrecioTotalFinal(BigDecimal precioTotalFinal) {
        this.precioTotalFinal = precioTotalFinal;
    }

    public String create() {
        try {
           pedidoID=getIDPedidoByCliente();
          String idCliente=sistemaController.getCliente().getId();
          CompraDTO compraTOTAL = pedidoFacade.getSuperTotal(pedidoID);
          Direnvio direnvio=direnvioController.getDireccionEnvioSelected();

          Compra compra=new Compra();
          precioTotalFinal=compraTOTAL.getTotalMonto();
          compra.setDescuento(compraTOTAL.getDescuento());
          compra.setImpuesto(compraTOTAL.getImpuesto());
          compra.setPagoNeto(compraTOTAL.getTotalMonto());
          compra.setPagoTotal(compraTOTAL.getTotalMonto());
          compra.setCostoEnvio(compraTOTAL.getGastosEnvio());//CAMBIAR COSTO DE ENVIO
          compra.setIdPedido(pedidoID);
          compra.setFecha(new Date());
          compra.setIdCliente(idCliente);
          compra.setObservaciones("PROCESANDO");
          compra.setFechaEnvio(new Date());
          compra.setEstado("CONFIRMADO");
          compra.setCcd(0);
          compra.setCuenta(" ");
          compra.setCostoEnvio(BigDecimal.ZERO);
          compra.setNoAutorizacion(" ");
          compra.setNoReferencia(" ");
          compra.setTipoPago("ELECTRONICO");
          compra.setDireccionEnvio(String.valueOf(direnvio.getId()));
          getFacade().create(compra);
          pedidoFacade.cambiarEstadoPedidoAll(pedidoID, "CONFIRMADO");
          JsfUtil.addSuccessMessage("PEDIDO CONFIRMADO SATISFACTORIAMENTE");
      
          return "/paypal/Create";
        } catch (Exception e) {
            logger.error("ERROR AL CONFIRMAR PEDIDO", e);
            JsfUtil.addErrorMessage("Error al confirmar pedido");
            return null;
        }
    }

    public String prepareEdit(Compra c) {
        current = c;//(Compra)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("CompraUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("PersistenceErrorOccured"));
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
            JsfUtil.addSuccessMessage(("CompraDeleted"));
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
