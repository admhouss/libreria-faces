/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paypal.jsf;

import com.escom.info.compra.Compra;
import com.escom.info.compra.Pedido;
import com.escom.info.compra.PedidoPK;
import com.escom.info.compra.jsf.DifacturacionController;
import com.escom.info.compra.jsf.PedidoController;
import com.escom.info.compra.jsf.util.JsfUtil;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.carrito.jsf.CarritoController;
import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.contacto.jsf.DirenvioController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author xxx
 */import java.util.List;
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

    @EJB com.escom.info.compra.ejb.PedidoFacade pedidoFacade;

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

    
    public String procesarPago(){

       Cliente cliente=carritoController.getSistemaController().getCliente();

        if(direnvioController.getListaDirEnvioCliente()==null || direnvioController.getListaDirEnvioCliente().isEmpty()){
        
              JsfUtil.addErrorMessage("Es requerido agregar una direccion de envio");
              return "/cliente/modulo";
   
        }
        if(carritoController.getListPedidosDTO()==null || carritoController.getListPedidosDTO().isEmpty()){
            JsfUtil.addErrorMessage("No existen publicaciones en su carrito de compra");
            return "/carrito/Carrito";
        }else {
            List<PublicacionDTO> carrito = carritoController.getListPedidosDTO();

            PedidoPK pkey;
            Date pedidoshoy=pedidoFacade.getHoy();
            try{
            for(PublicacionDTO p:carrito){
               
                Pedido pedidoTemp=pedidoFacade.getListPedidoHotByCliernteOne(cliente.getId(),pedidoshoy);
                pkey=pedidoTemp==null?new PedidoPK():pedidoTemp.getPedidoPK();

                Pedido pedido=new Pedido();
                pedido.setCliente(cliente);
                pedido.setFechaPedido(p.getFechaCompra());
                pedido.setCategoria(p.getAsunto());
                pedido.setDescuento(p.getDesc());
                pedido.setPrecioNeto(p.getPrecio());
                pedido.setPrecioTotal(new  BigDecimal(p.getTotal()));
                pedido.setTipoEnvio("ELECTRONICO");
                pedido.setImpuesto(p.getImpuesto());
                pedido.setNoArticuloCategoria(p.getCantidad());
                pedido.setArticulo(p.getArticulo());
                pkey.setIdPedido(pkey.getIdPedido());
                pkey.setIdArticulo(p.getIdArticulo());
                pedido.setPedidoPK(pkey);
               
                pedidoFacade.create(pedido);
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
       
        
        return "/compra/Create";
        
    }
   

}

