/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.DescuentoArticulo;
import escom.libreria.info.articulo.jpa.Impuesto;
import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.login.sistema.SistemaController;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author xxx
 */
@ManagedBean(name="carritoController")
@SessionScoped
public class CarritoController implements Serializable{

    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    @EJB private  escom.libreria.info.login.ejb.SistemaFacade sistemaFacade;
    @EJB private escom.libreria.info.cliente.ejb.DescuentoClienteFacade descuentoClienteFacade;
    @EJB private escom.libreria.info.articulo.ejb.DescuentoArticuloFacade descuentoArticuloFacade;
    @EJB private escom.libreria.info.articulo.ejb.ImpuestoFacade impuestoFacade;


    private List<PublicacionDTO> listcarritoDTO;
    private PublicacionDTO publicaciondto;
    private List<PublicacionDTO> listcarritoDTOTemporal;

    public List<PublicacionDTO> getListcarritoDTOTemporal() {
        return listcarritoDTOTemporal;
    }

    public void setListcarritoDTOTemporal(List<PublicacionDTO> listcarritoDTOTemporal) {
        this.listcarritoDTOTemporal = listcarritoDTOTemporal;
    }


    public String prepareEditPublicacion(PublicacionDTO item){
        publicaciondto=item;
        listcarritoDTOTemporal=new ArrayList<PublicacionDTO>();
        listcarritoDTOTemporal.add(item);
        return "/carrito/Edit";
    }

    public PublicacionDTO getPublicaciondto() {
        return publicaciondto;
    }

    public void setPublicaciondto(PublicacionDTO publicaciondto) {
        this.publicaciondto = publicaciondto;
    }
    


    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }

    /** Creates a new instance of CarritoController */
    public CarritoController() {

        
    }

    private CarritoCompraTemporalLocal ObtenerCarrito(){
         if(carritoCompraTemporalLocal==null){
            try{
                carritoCompraTemporalLocal=sistemaFacade.getObtenerBandejaTemporal();
            }catch(Exception e){
                System.out.println("No carrito creado"+e.getMessage());
            }
        }
         return carritoCompraTemporalLocal;
    }
 public String agregarArticulo(Publicacion publicacion){
     PublicacionDTO temporal=null;
           try{
         if(sistemaController.getCliente()!=null){
          carritoCompraTemporalLocal=ObtenerCarrito();
          temporal=carritoCompraTemporalLocal.buscarPublicacion(publicacion);
         if(temporal==null){
             PublicacionDTO publicacionDTO=procesarArticulo(publicacion,1);
             carritoCompraTemporalLocal.addPublicacion(publicacionDTO);
         }else{
           int pos=carritoCompraTemporalLocal.getPosArticulo(temporal);
           int indice=temporal.getIndice();
           temporal=procesarArticulo(publicacion,temporal.getCantidad()+1);
           temporal.setIndice(indice);

           System.out.println("posicion"+indice);
           carritoCompraTemporalLocal.actualizarArticulo(temporal,indice-1);
         }
          JsfUtil.addSuccessMessage("Articulo agregado Satisfactoriamente");
          return "/carrito/Carrito";
         }else {
          JsfUtil.addErrorMessage("Lo sentimos,usuario  no registrado");
          return "/login/Create.xhtml";
         }

     }catch(Exception e){ e.printStackTrace();}

         return "/carrito/Carrito";
    }

 public String editarCarritoCompra(PublicacionDTO editar){
     try{
           if(editar.getCantidad()>0){
                BigDecimal total=calcularTotal(editar.getPrecio(), editar.getDesc(),editar.getImpuesto(), editar.getCantidad());
                editar.setTotal(total.doubleValue());
                carritoCompraTemporalLocal.actualizarArticulo(editar,editar.getIndice()-1);
                listcarritoDTOTemporal.clear();
                listcarritoDTOTemporal=null;
                JsfUtil.addSuccessMessage("Carrito de compra Actualizado Satisfactoriamente");
            }else if(editar.getCantidad()==0){
                JsfUtil.addErrorMessage("Cantidad no puede ser cero");
            }
            else{
                   JsfUtil.addErrorMessage("Error,cantidad consigno negativo");
                    return "/carrito/Editar";
            }
     }catch(Exception e){
         JsfUtil.addErrorMessage("Error problemas al editar informacion");
     }
      return "/carrito/Carrito";
 }
    public String borrarArticulo(PublicacionDTO articulo){
         carritoCompraTemporalLocal.removePublicacion(articulo);
         JsfUtil.addSuccessMessage("Articulo eliminado Sastisfactoriamente");
         return "/carrito/Carrito";
    }
    public List<PublicacionDTO> getListArticulos(){
        if(carritoCompraTemporalLocal!=null)
        listcarritoDTO=carritoCompraTemporalLocal.getListPublicacion();
        
        return listcarritoDTO;
    }

    public BigDecimal getMontoTotal(){
        carritoCompraTemporalLocal=ObtenerCarrito();
        BigDecimal monto= carritoCompraTemporalLocal.getMontoTotal();
        monto=monto.setScale(2,RoundingMode.HALF_DOWN);
        return monto;
    }

    public String destroy(PublicacionDTO  item){
         try{
             borrarArticulo(item);
            JsfUtil.addSuccessMessage("Articulo Eliminado satisfactoriamente!!");
            return "/carrito/Carrito";
        }catch(Exception e){}
        return "/carrito/Carrito";

    }

    public int getcountElement(){
        if(carritoCompraTemporalLocal!=null)
        return carritoCompraTemporalLocal.getCount();
        return 1;
    }


    private PublicacionDTO procesarArticulo(Publicacion p,int cantidad){

        PublicacionDTO publicacionDTO=new PublicacionDTO();
        publicacionDTO.setIdArticulo(p.getArticulo().getId());
        publicacionDTO.setIdPublicacion(p.getIdDc());
        publicacionDTO.setEditorial(p.getEditorial());
        publicacionDTO.setTitulo(p.getArticulo().getTitulo());
        publicacionDTO.setAutor(p.getArticulo().getCreador());
        publicacionDTO.setAsunto(p.getArticulo().getAsunto());
        publicacionDTO.setCantidad(cantidad);
        publicacionDTO.setFechaCompra(new Date());
        BigDecimal descuento=obtenerMayorDescuento(publicacionDTO.getIdArticulo());
        publicacionDTO.setDesc(descuento);
        BigDecimal impuesto=getImpuesto(publicacionDTO.getIdArticulo());
        publicacionDTO.setImpuesto(impuesto);
        publicacionDTO.setPrecio(p.getArticulo().getPrecioUnitario());
        BigDecimal total=calcularTotal(publicacionDTO.getPrecio(), descuento, impuesto, publicacionDTO.getCantidad());
        publicacionDTO.setTotal(total.doubleValue());
        return publicacionDTO;
    }

    private BigDecimal calcularTotal(BigDecimal precioUnitario,BigDecimal descuento,BigDecimal impuesto,int cantidad){
        BigDecimal total=BigDecimal.ZERO;
        BigDecimal impuestoPrecio=BigDecimal.ZERO;
        total=precioUnitario.multiply(new BigDecimal(cantidad));
        System.out.println("total + cantidad"+total);

        if(impuesto.compareTo(BigDecimal.ZERO)!=0){
            impuestoPrecio=getPrecioIVA(impuesto.doubleValue(), total.doubleValue());
            System.out.print("impuestorecibido"+impuestoPrecio);
            total=impuestoPrecio.add(total);
            System.out.println("total+ impuesto"+total+"impuesto"+impuesto);
        }
        
       if(descuento.compareTo(BigDecimal.ZERO)!=0){
          total=getPrecioDescuentoAplicado(descuento.doubleValue(),total.doubleValue());
        }
        System.out.println("ANTES DEL total"+total);
        total=total.setScale(2,total.ROUND_HALF_DOWN);
        System.out.println("total"+total);
        return total;
    }

    private BigDecimal obtenerMayorDescuento(int idArticulo){
        BigDecimal descuentoArticulo=getDescuentoArticulo(idArticulo);
        BigDecimal descuentoCliente=getDescuentoCliente();

        
         switch (descuentoArticulo.compareTo(descuentoCliente)){
             case 0: return descuentoArticulo;
             case -1: return descuentoCliente;
             case 1: return descuentoArticulo;
         }
         
        return descuentoArticulo;
    }

    private  BigDecimal getDescuentoArticulo(int idArticulo){
          BigDecimal descuento=descuentoArticuloFacade.getDescuentoValidoArticulo(idArticulo);
          return descuento;
    }

    public BigDecimal getImpuesto(int idArticulo) {
        BigDecimal impuestoTOTAL=impuestoFacade.getImpuestoTotalArticulo(idArticulo);
        return impuestoTOTAL;
    }

    private BigDecimal getPrecioDescuentoAplicado(double porcentaje,double precio){
        BigDecimal Total=new BigDecimal(precio);
        
       

        try{
         
         Total=Total.subtract(getPrecioIVA(porcentaje, precio));
         System.out.print("Total"+Total);
          
        }catch(Exception e){}
          return Total;
    }

    private BigDecimal getPrecioIVA(double porcentaje,double precio){
        BigDecimal Total=BigDecimal.ZERO;
        try{
        double paso1=porcentaje*precio;
        paso1=(paso1/100);
        Total=new BigDecimal(paso1);
        }catch(Exception e){}
        return Total;
    }

    public BigDecimal getDescuentoCliente(){
         Cliente cliente=sistemaController.getCliente();
         BigDecimal descuento=descuentoClienteFacade.obtenerMaxioDescuento(cliente.getId());
         return descuento;
    }

   public String prepareList(){

       return "/carrito/Carrito";
    }

     private CarritoCompraTemporalLocal carritoCompraTemporalLocal=ObtenerCarrito();//obetenemso carrito compra

}
