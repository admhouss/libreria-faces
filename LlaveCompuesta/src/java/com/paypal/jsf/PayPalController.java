/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paypal.jsf;


import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.carrito.jsf.CarritoController;
import escom.libreria.info.cliente.Cliente;
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

@EJB private ProveedorArticuloFacade proveedorArticuloFacade;
@EJB private ZonaFacade  zonaFacade;
@EJB private PedidoFacade pedidoFacade;
@EJB private CompraFacade compraFacade;
@EJB private ArticuloFacade articuloFacade;


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
        BigDecimal gastEnvio=BigDecimal.ZERO;
        ProveedorArticulo proveedorArticulo=null;

        PedidoPK pkey=new PedidoPK();

        List<PublicacionDTO> carrito = carritoController.getListPedidosDTO();//Lo que tiene el carrito de compra;

      
       

            for(PublicacionDTO p:carrito)
            {


                Pedido pedido=new Pedido();
                pedido.setCliente(cliente);
                pedido.setFechaPedido(p.getFechaCompra());
                pedido.setEstado("PROCESANDO");
                pedido.setCategoria(p.getAsunto());
                pedido.setDescuento(p.getDesc());

              
             try{

               if(p.isTypePublicacion())
               {
                  articulo=articuloFacade.find( p.getIdSuscripcion());

                  pedido.setPrecioNeto(p.getPrecio());
                  pedido.setArticulo(articulo);
                  p.setArticulo(articulo);
                  p.setIdArticulo(articulo.getId());

                  pkey.setIdArticulo(articulo.getId());
                  System.out.println( "PRECIO TOTAL SUSCRIPCION:"+p.getTotal());
               }
               else
               {
                    pedido.setPrecioNeto(p.getPrecio());
                    pedido.setArticulo(p.getArticulo());
                    pkey.setIdArticulo(p.getArticulo().getId());

                    System.out.println( "PRECIO TOTAL PUBLICACION:"+p.getTotal());
               }

                
                tipo_fisico=tipoPedido_fisico(p.isTypePublicacion(), p.getArticulo().getFormato());

                if(tipo_fisico==0) //publicacion_fisica
                {
                   System.out.println("ANALIZANDO PUBLICACION FISICA");
                   proveedorArticulo=proveedorArticuloFacade.getProveedorMenosConsumo(p.getIdArticulo());
                   gastEnvio= calcularGastorEnvio(proveedorArticulo, direccion);
                     System.out.println(
                      "GASTOS DE ENVIO:"+gastEnvio+":peso"+proveedorArticulo.getPeso()
                     +"direccionEnvio"+direccion.getEstado().getIdZona());
                }
                //else if(tipo_fisico==1)
                //{
                  //  System.out.println("ANALIZANDO PUBLICACION FISICA");
                    //gastEnvio=calcularGastorEnvio(null, direccion) ;

                //}                
                pedido.setPrecioTotal(new  BigDecimal(p.getTotal()));
                pedido.setPrecioTotal(pedido.getPrecioTotal().add(gastEnvio));
                pedido.setTipoEnvio(p.getArticulo().getFormato());
                pedido.setImpuesto(p.getImpuesto());
                pedido.setNoArticuloCategoria(p.getCantidad());

                pkey.setIdPedido(pkey.getIdPedido());
                pedido.setPedidoPK(pkey);
                pedidoFacade.create(pedido);
                System.out.println("AGREGO UN ELEMENTO AL PEDIDO");
                if(bandera==false){
                 int idPedido= pedidoFacade.buscarIdPeidoMaximo(cliente.getId(),"PROCESANDO");
                  pkey.setIdPedido(idPedido);
                  System.out.println("IDPEDIDO MAXIMO"+idPedido);
                  bandera=true;
                }
            }catch(Exception e){

                e.printStackTrace();
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
    private boolean determinarTipoEnvioFisico(boolean type,String formato){

        if(type)
        {/*retorna true si es suscripcion*/
            if(formato.equalsIgnoreCase("FISICO"))/*suscripcion fisica*/
              return   true;

        }else{/*Es una publicacion */
            if(formato.equalsIgnoreCase("FISICO")) /*publicacion fisica*/
             return true;
        }
        return false;
    }

    public int tipoPedido_fisico(boolean type,String formato){ /*cero-suscripcion fisica*/
        int valor=-1;
        if(determinarTipoEnvioFisico(type, formato)) /*1:sucripcion_fisica,0:publicacion:fisico*/
            valor= type?1:0;
        return valor;
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

   

   /* @PostConstruct
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

                     JsfUtil.addSuccessMessage("Compra realizada Satisfactoriamente"+saludo);
                }catch(Exception e){
                    e.printStackTrace();
                 JsfUtil.addErrorMessage("Error al cancelar el pedido");
                }

              

             }

        }catch(Exception e){

        }
    }*/
}

