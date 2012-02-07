/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paypal.jsf;


import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.carrito.jsf.CarritoController;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Direnvio;

import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.PedidoPK;
import escom.libreria.info.compras.Zona;
import escom.libreria.info.compras.ejb.CompraFacade;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.compras.ejb.ZonaFacade;
import escom.libreria.info.compras.jsf.DifacturacionController;
import escom.libreria.info.compras.jsf.DirenvioController;
import escom.libreria.info.compras.jsf.PedidoController;
import escom.libreria.info.encriptamientoMD5.EncriptamientoImp;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.login.sistema.SistemaController;
import escom.libreria.info.login.sistema.ventasController;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Date;

/**
 *
 * @author xxx
 */import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@ManagedBean(name = "paypalController")
@RequestScoped
public class PayPalController implements Serializable{

   
    @ManagedProperty("#{direnvioController}")
    private DirenvioController direnvioController;
    @ManagedProperty("#{difacturacionController}")
    private DifacturacionController difacturacionController;
    @ManagedProperty("#{pedidoController}")
    private PedidoController pedidoController;
    @ManagedProperty("#{carritoController}")
    private CarritoController carritoController;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    @ManagedProperty("#{ventasController}")
    private ventasController ventascontroller;


@EJB private ProveedorArticuloFacade proveedorArticuloFacade;
@EJB private ZonaFacade  zonaFacade;
@EJB private PedidoFacade pedidoFacade;
@EJB private CompraFacade compraFacade;
@EJB private ArticuloFacade articuloFacade;
private static  Logger logger = Logger.getLogger(PayPalController.class);

    public ventasController getVentascontroller() {
        return ventascontroller;
    }

    public void setVentascontroller(ventasController ventascontroller) {
        this.ventascontroller = ventascontroller;
    }





    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }



    

    public CarritoController getCarritoController() {
        return carritoController;
    }

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }


    

    public PedidoController getPedidoController() {
        return pedidoController;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }



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


    
    public PayPalController() {
    }


    public String procesarCarritoPorDeposito(){

         Direnvio direccion=direnvioController.getDireccionEnvioSelected();
        if(carritoController==null){
            JsfUtil.addErrorMessage("error carrito de compra nullo");
            return null;
        }
       if(direnvioController.getDireccionEnvioSelected()==null){
              JsfUtil.addErrorMessage("Es requerido seleccionar una direccion de envio");
              return null;
        }
        if(carritoController.getListPedidosDTO()==null || carritoController.getListPedidosDTO().isEmpty()){
            JsfUtil.addErrorMessage("No existen publicaciones en su carrito de compra");
            return "/carrito/Carrito";
        }
        /*PROCEDEMOS A PROCESAR EL CARRITO DE COMPRA*/
        else{
          Cliente cliente=sistemaController.getCliente();

          if(crearPedido(cliente,direccion)){
              carritoController.borrarCarrito();
          return "/compra/Deposito";
          }
          else{
              return "/carrito/Carrito";
          }
        }
       
    }

    private Articulo articulo;
    public  boolean crearPedido(Cliente cliente,Direnvio direccion){

         
        int tipo_fisico=-1;
        boolean bandera=false;
        int idPedido=-1;
        BigDecimal gastEnvio=BigDecimal.ZERO;
        ProveedorArticulo proveedorArticulo=null;
        PedidoPK pkey=new PedidoPK();

       
        List<PublicacionDTO> carrito = carritoController.getListPedidosDTO();//Lo que tiene el carrito de compra;

      logger.info("COMENZANDO EL PROCESO VACIAMOS CARRITO DE COMPRA  PEDIDO! #"+carrito.size());

            for(PublicacionDTO p:carrito)
            {

               logger.info("articulo"+p.getArticulo().getTitulo()+"articulo id"+p.getIdArticulo());

                Pedido pedido=new Pedido();
                pedido.setCliente(cliente);
                pedido.setFechaPedido(p.getFechaCompra());
                pedido.setEstado("PROCESANDO");
                pedido.setCategoria(p.getAsunto());
                pedido.setDescuento(p.getDesc());
                pedido.setPrecioNeto(p.getPrecio());
                pedido.setArticulo(p.getArticulo());
                if(bandera!=false)
                {
                     pkey=new PedidoPK();
                     pkey.setIdArticulo(p.getArticulo().getId());
                     pkey.setIdPedido(idPedido);
                }

                //pedido.setPedidoPK()

                pkey.setIdArticulo(p.getArticulo().getId());
                tipo_fisico=determinacion_tipoArticulo(p.getArticulo().getTipoArticulo().getDescripcion(), p.getArticulo().getFormato());
               
             try{

               if(tipo_fisico==0) // SUSCRIPCION FISICA
               {
                   logger.info("PROCESANDO SUSCRIPCION FISICA");
                   pedido.setPrecioNeto(p.getPrecio());
                   pkey.setIdArticulo(p.getArticulo().getId());
                   pedido.setPedidoPK(pkey);

                 //  /*FALTA CONTEMPLAR GASTOS DE ENVIO SUSCRIPCION FISICA


                   logger.info("SUSCRIPCION FISICA");
               }
               else if(tipo_fisico==1) //SUSCRIPCION ELECTRONICA
               {

                   logger.info("PROCESANDO SUSCRIPCION ELECTRONICA");
                   pedido.setPrecioNeto(p.getPrecio());
                  
                   pkey.setIdArticulo(p.getArticulo().getId());
                   pedido.setPedidoPK(pkey);

                  //YA CALCULO EL COSTO TOTAL DE LA SUSCRIPCION ELECTRONICA POR CADA ARTICULO CONTEMPLANDO EL IDSUSCRIPCION
               }
               else if(tipo_fisico==2 && p.getArticulo().getFormato().toUpperCase().equalsIgnoreCase("FISICO"))
                {


                    try{
                        logger.info("ANALIZANDO PUBLICACION FISICA");
                        proveedorArticulo=proveedorArticuloFacade.getProveedorMenosConsumo(p.getArticulo().getId());
                        gastEnvio= calcularGastorEnvio(proveedorArticulo, direccion);
                        logger.info("GASTOS DE ENVIO:"+gastEnvio+":peso"+proveedorArticulo.getPeso()+"direccionEnvio"+direccion.getEstado().getIdZona());
                        proveedorArticulo=null;
                   }catch(Exception e){
                       logger.error("EL ARTICULO NO SE ENCUENTRA,EN LA TABLA PROVEEDOR_ARTICULO ", e);
                       logger.info("NO FUE POSIBLE CALCULAR GASTOS DE ENVIO");
                   }
                }
                           
                pedido.setPrecioTotal(new  BigDecimal(p.getTotal()));
                pedido.setPrecioTotal(pedido.getPrecioTotal().add(gastEnvio));
                pedido.setTipoEnvio(p.getArticulo().getFormato());
                pedido.setImpuesto(p.getImpuesto());
                pedido.setNoArticuloCategoria(p.getCantidad());
                pedido.setArticulo(p.getArticulo());
                pkey.setIdPedido(pkey.getIdPedido());
                //pkey.setIdArticulo(p.getArticulo().getId());
                pedido.setPedidoPK(pkey);
                pedidoFacade.create(pedido);
                logger.info("VOY A AGREGAR UN ELEMENTO AL PEDIDO");
                if(bandera==false)
                {
                  idPedido= pedidoFacade.buscarIdPeidoMaximo(cliente.getId(),"PROCESANDO");
                  logger.info("ID-PEDIDO MAXIMO"+idPedido);
                  bandera=true;
                }
            }catch(Exception e){

                logger.error("El pedido no pudo crearse por esta razon:",e);
                JsfUtil.addErrorMessage("Error al crear Pedido");
                return false;
            }


            }//for

        return true;
    }

    /*true suscripcion fisica  //tue publicacion fisica*/

    public BigDecimal calcularGastorEnvio(ProveedorArticulo proveedorArticulo,Direnvio direccionEnvio)
    {


          BigDecimal pesoKilogramo=proveedorArticulo.getPeso();

          int pesoEntero=pesoKilogramo.intValue();
          String idZona=direccionEnvio.getEstado().getIdZona();
          pesoKilogramo=zonaFacade.getTarifaByPeso(idZona,pesoEntero,pesoKilogramo);


          return pesoKilogramo;
    }
    

    public int determinacion_tipoArticulo(String tipoArticulo,String formato){ /*cero-suscripcion fisica*/

        tipoArticulo=tipoArticulo.toUpperCase();
        formato=formato.toUpperCase();
        if(tipoArticulo.equalsIgnoreCase("SUSCRIPCION") || tipoArticulo.equalsIgnoreCase("SUSCRIPCIÒN")){
            if(formato.equalsIgnoreCase("FISICO") || formato.equalsIgnoreCase("FÌSICO"))
             return 0;//SUSCRIPCION FISICA;
            if(formato.equalsIgnoreCase("ELECTRONICO") || formato.equalsIgnoreCase("ELÈCTRONICO"))
            return 1;  //SUSCRIPCION ELECTRONICO
        }
        return 2;/// PUBLICACICION


    }

    public String procesarPago(){

       Cliente cliente=carritoController.getSistemaController().getCliente();
       Direnvio direnvio=direnvioController.getDireccionEnvioSelected();
     try{

        if(direnvioController.getDireccionEnvioSelected()==null){
              JsfUtil.addErrorMessage("Es requerido seleccionar una direccion de envio,");
              return "/carrito/Carrito";
   
        }
        if(carritoController.getListPedidosDTO()==null || carritoController.getListPedidosDTO().isEmpty()){
            JsfUtil.addErrorMessage("No existen publicaciones en su carrito de compra");
            return "/carrito/Carrito";
        }else{
         if(crearPedido(cliente,direnvio)){
                return "/compra/Create";
            }else
             return "/carrito/Carrito";
         
        }

        }catch(Exception e){

        JsfUtil.addErrorMessage("Error ");
        }

       return null;
            
        }
       
        
       
        
    
   
private String pedidoComprado;

    public String getPedidoComprado() {
        return pedidoComprado;
    }

    public void setPedidoComprado(String pedidoComprado) {
        this.pedidoComprado = pedidoComprado;
    }

   

   @PostConstruct
    public void init() {
         int idPedido;
        byte[] byt;

        try{

            if(getPedidoComprado()!=null && !getPedidoComprado().trim().equals("")){
                String saludo;
               //&& getCorreo()!=null && !getCorreo().trim().equals("")   ){

                try{

                     EncriptamientoImp  encrip=new EncriptamientoImp ();
                     byt=encrip.hexToBytes(getPedidoComprado());
                     saludo=encrip.decrypt(byt);

                     try{
                       Integer pedido=Integer.parseInt(saludo);
                       Compra compra =compraFacade.getComprasIdPedido(pedido);
                       ventascontroller.setSelected(compra);
                       ventascontroller.comprarArticulo(pedido);
                     }catch(Exception e){
                         logger.error("OCURROO ERROR COMPRA PAYPAL",e);
                     }

                     //me llega el numero

                     JsfUtil.addSuccessMessage("Compra realizada Satisfactoriamente"+saludo);
                }catch(Exception e){
                    e.printStackTrace();
                 JsfUtil.addErrorMessage("Error al cancelar el pedido");
                }

              

             }

        }catch(Exception e){

        }
    }
}

