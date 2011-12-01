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

    public String prepareEditPublicacion(PublicacionDTO item){
        publicaciondto=item;
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
           try{
         if(sistemaController.getCliente()!=null){
          PublicacionDTO publicacionDTO=procesarArticulo(publicacion);
          carritoCompraTemporalLocal=ObtenerCarrito();
          carritoCompraTemporalLocal.addPublicacion(publicacionDTO);
             JsfUtil.addSuccessMessage("Articulo agregado Satisfactoriamente");
             return "/carrito/Carrito";
         }else {
          JsfUtil.addErrorMessage("Lo sentimos,usuario  no registrado");
          return "/login/Create.xhtml";
         }

     }catch(Exception e){ e.printStackTrace();}

         return "/carrito/Carrito";
    }
    public void borrarArticulo(PublicacionDTO articulo){
          carritoCompraTemporalLocal.removePublicacion(articulo);
    }
    public List<PublicacionDTO> getListArticulos(){
        if(carritoCompraTemporalLocal!=null)
        listcarritoDTO=carritoCompraTemporalLocal.getListPublicacion();
        
        return listcarritoDTO;
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


    private PublicacionDTO procesarArticulo(Publicacion p){

        PublicacionDTO publicacionDTO=new PublicacionDTO();
        publicacionDTO.setIdArticulo(p.getArticulo().getId());
        publicacionDTO.setIdPublicacion(p.getIdDc());
        publicacionDTO.setEditorial(p.getEditorial());
        publicacionDTO.setTitulo(p.getArticulo().getTitulo());
        publicacionDTO.setAutor(p.getArticulo().getCreador());
        publicacionDTO.setAsunto(p.getArticulo().getAsunto());
        publicacionDTO.setCantidad(1);
        publicacionDTO.setFechaCompra(new Date());
        BigDecimal descuento=getDescuentoArticulo(publicacionDTO.getIdArticulo());
        publicacionDTO.setDesc(descuento);
        BigDecimal impuesto=getImpuesto(publicacionDTO.getIdArticulo());
        publicacionDTO.setImpuesto(impuesto);
        publicacionDTO.setPrecio(p.getArticulo().getPrecioUnitario());
        return publicacionDTO;
    }

    private  BigDecimal getDescuentoArticulo(int idArticulo){
          BigDecimal descuento=descuentoArticuloFacade.getDescuentoValidoArticulo(idArticulo);
          return descuento;
    }

    public BigDecimal getImpuesto(int idArticulo) {
        BigDecimal impuestoTOTAL=impuestoFacade.getImpuestoTotalArticulo(idArticulo);
        return impuestoTOTAL;
    }

    public BigDecimal getDescuentoCliente(){

         Cliente cliente=sistemaController.getCliente();
         BigDecimal descuento=descuentoClienteFacade.obtenerMaxioDescuento(cliente.getId());
         return descuento;
    }

     private CarritoCompraTemporalLocal carritoCompraTemporalLocal=ObtenerCarrito();//obetenemso carrito compra

}
