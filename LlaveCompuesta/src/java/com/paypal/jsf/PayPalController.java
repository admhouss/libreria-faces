/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paypal.jsf;


import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.carrito.jsf.CarritoController;
import escom.libreria.info.cliente.Cliente;

import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.PedidoPK;
import escom.libreria.info.compras.ejb.CompraFacade;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.compras.jsf.DifacturacionController;
import escom.libreria.info.compras.jsf.DirenvioController;
import escom.libreria.info.compras.jsf.PedidoController;
import escom.libreria.info.login.sistema.SistemaController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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




    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }



    @EJB private PedidoFacade pedidoFacade;
    @EJB private CompraFacade compraFacade;

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


    public String procesarDeposito(){


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
        }else{
          Cliente cliente=sistemaController.getCliente();
          if(crearPedido(cliente))
          return "/compra/Deposito";
          else{
              return "/carrito/Carrito";
          }
        }
       
    }

    public  boolean crearPedido(Cliente cliente){
       List<PublicacionDTO> carrito = carritoController.getListPedidosDTO();//Lo que tiene el carrito de compra;
       PedidoPK pkey=new PedidoPK();
       boolean bandera=false;
       

            for(PublicacionDTO p:carrito){


 try{

                            
                  

                Pedido pedido=new Pedido();
                pedido.setCliente(cliente);
                pedido.setFechaPedido(p.getFechaCompra());
                pedido.setEstado("PROCESANDO");
                pedido.setCategoria(p.getAsunto());
                pedido.setDescuento(p.getDesc());
                pedido.setPrecioNeto(p.getPrecio());
                pedido.setPrecioTotal(new  BigDecimal(p.getTotal()));
                pedido.setTipoEnvio(p.getArticulo().getFormato());
                pedido.setImpuesto(p.getImpuesto());
                pedido.setNoArticuloCategoria(p.getCantidad());
                pedido.setArticulo(p.getArticulo());
                pkey.setIdPedido(pkey.getIdPedido());
                pkey.setIdArticulo(p.getIdArticulo());
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

    public String procesarPago(){

       Cliente cliente=carritoController.getSistemaController().getCliente();
     

        if(direnvioController.getDireccionEnvioSelected()==null){
              JsfUtil.addErrorMessage("Es requerido seleccionar una direccion de envio,");
              return "/carrito/Carrito";
   
        }
        if(carritoController.getListPedidosDTO()==null || carritoController.getListPedidosDTO().isEmpty()){
            JsfUtil.addErrorMessage("No existen publicaciones en su carrito de compra");
            return "/carrito/Carrito";
        }else{
            if(crearPedido(cliente)){
                return "/compra/Create";
            }else
             return "/carrito/Carrito";
        }

     
            
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

        try{

            if(getPedidoComprado()!=null && !getPedidoComprado().trim().equals("")){

               //&& getCorreo()!=null && !getCorreo().trim().equals("")   ){

                try{
                     idPedido=Integer.parseInt(getPedidoComprado());
                     //&&comente dos lineeas
                    // pedidoFacade.cambiarEstadoPedidoAll(idPedido,"COMPRADO");
                    // compraFacade.cambiarEstadoCompra(idPedido,"COMPRADO");

                     JsfUtil.addSuccessMessage("Compra realizada Satisfactoriamente");
                }catch(Exception e){
                 JsfUtil.addErrorMessage("Error al cancelar el pedido");
                }

              

             }

        }catch(Exception e){

        }
    }
}

