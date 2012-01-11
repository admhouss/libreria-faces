/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;


import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.Impuesto;
import escom.libreria.info.articulo.Publicacion;
import escom.libreria.info.articulo.ejb.ImpuestoFacade;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Direnvio;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.compras.jsf.DirenvioController;
import escom.libreria.info.descuentos.DescuentoArticulo;
import escom.libreria.info.descuentos.DescuentoCliente;
import escom.libreria.info.descuentos.DescuentoClientePK;
import escom.libreria.info.descuentos.ejb.DescuentoArticuloFacade;
import escom.libreria.info.descuentos.ejb.DescuentoClienteFacade;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.login.ejb.SistemaFacade;

import escom.libreria.info.login.sistema.SistemaController;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @ManagedProperty("#{direnvioController}")
    private DirenvioController direnvioController;
    @EJB private  SistemaFacade sistemaFacade;
    @EJB private DescuentoClienteFacade descuentoClienteFacade;
    @EJB private DescuentoArticuloFacade descuentoArticuloFacade;
    @EJB private ImpuestoFacade  impuestoFacade;
    
    @EJB private PedidoFacade pedidoFacade;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenFacade almacenFacade;
    public Articulo articulo;

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }


    public DirenvioController getDirenvioController() {
        return direnvioController;
    }

    public void setDirenvioController(DirenvioController direnvioController) {
        this.direnvioController = direnvioController;
    }

public String RegresarCarrito(){
    return "/carrito/Carrito";
}


    private List<PublicacionDTO> listcarritoDTO;//se usa para mostrar los pedidos en el carrito de compra
    private PublicacionDTO publicaciondto;

    private List<PublicacionDTO> listcarritoDTOTemporal;

    public List<PublicacionDTO> getListcarritoDTOTemporal() {
        return listcarritoDTOTemporal;
    }

    public void setListcarritoDTOTemporal(List<PublicacionDTO> listcarritoDTOTemporal) {
        this.listcarritoDTOTemporal = listcarritoDTOTemporal;
    }

    public List<Impuesto>  impuestosArticulo;
    public List<DescuentoArticulo> descuentoArticulos;
    public List<DescuentoCliente> descuentoClientes;

    public List<Impuesto> getImpuestosArticulo() {
        return impuestosArticulo;
    }

    public void setImpuestosArticulo(List<Impuesto> impuestosArticulo) {
        this.impuestosArticulo = impuestosArticulo;
    }

    public List<DescuentoArticulo> getDescuentoArticulos() {
        return descuentoArticulos;
    }

    public void setDescuentoArticulos(List<DescuentoArticulo> descuentoArticulos) {
        this.descuentoArticulos = descuentoArticulos;
    }



    public String prepararSeleccionDesglose(PublicacionDTO publicacionDTO){

          try{
            articulo=publicacionDTO.getArticulo();
            impuestosArticulo=impuestoFacade.buscarImpuestoByarticulo(articulo);
            descuentoArticulos=descuentoArticuloFacade.getDescuentoValidoListArticulo(articulo.getId());
        }catch(Exception e){
            JsfUtil.addErrorMessage("Error al intentar mostrar desglose");
            e.printStackTrace();;
        }


        return "/desgloseArticulo/List";
    }

    public BigDecimal getCostoEnvio(){
        Direnvio direccionEnvio = direnvioController.getDireccionEnvioSelected();
        return direccionEnvio.getEstado().getZona().getPeso();
    }

   /* public List<Pedido> getListcarritoDTOTemporal() {
        return listcarritoDTOTemporal;
    }

    public void setListcarritoDTOTemporal(List<Pedido> listcarritoDTOTemporal) {
        this.listcarritoDTOTemporal = listcarritoDTOTemporal;
    }
*/

    public String prepareEditPublicacion(PublicacionDTO item){
        publicaciondto=item;
       //publicaciondto.setArticulo(item.getArticulo());
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
private Date getHoy(){
        try {
            Date date = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
            String cadena = formato2.format(date);
            Date fechaOtra = formato2.parse(cadena);

            String cadenaToday = formato.format(fechaOtra);
            Date hoy = formato2.parse(cadenaToday);
            return hoy;
        } catch (ParseException ex) {
            Logger.getLogger(CarritoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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

     Articulo articulo=publicacion.getArticulo();
     Almacen almacen=almacenFacade.find(articulo.getId());
     PublicacionDTO temporalDTO=null;//
     if(almacen==null || almacen.getExistencia()<=0){

         JsfUtil.addErrorMessage("La publicacion que desea no se encuentra disponible");
         return null;

     }else{


        
        Cliente clienteOperando=sistemaController.getCliente();//
    

           try{
                 if(clienteOperando!=null){

                    carritoCompraTemporalLocal=ObtenerCarrito();
                    temporalDTO=carritoCompraTemporalLocal.buscarPublicacion(publicacion);

                      if(temporalDTO==null){
                             temporalDTO=procesarArticulo(publicacion,1);
                             agregarCarrito(temporalDTO);
                             JsfUtil.addSuccessMessage("Publicacion agregada Satisfactoriamente");
                             return "/carrito/Carrito";
                      }else{
                            JsfUtil.addErrorMessage("Ya encuentra esta publicacion en su carrtio");
                            return "/carrito/Carrito";
                      }

         
                   }//if

          
         
                else {
                    JsfUtil.addErrorMessage("Lo sentimos,usuario  no registrado");
                    return "/login/Create.xhtml";
                }

            }catch(Exception e){ }


     }

         return "/carrito/Carrito";
    }


 public List<PublicacionDTO> getListPedidosDTO(){
    if(carritoCompraTemporalLocal==null){
        carritoCompraTemporalLocal=ObtenerCarrito();

        List<PublicacionDTO> l=new ArrayList<PublicacionDTO>();
        return l;
    }
    return carritoCompraTemporalLocal.getListPublicacion();
 }

 public void agregarCarrito(PublicacionDTO p){
     int posicion=carritoCompraTemporalLocal.getCount();
     p.setIndice(posicion);
     carritoCompraTemporalLocal.addPublicacion(p);
 }
 /*

 public String agregarArticulo(Publicacion publicacion){
     PublicacionDTO temporal=null;
     Cliente clienteOperando=sistemaController.getCliente();
     Articulo articuloOperando=publicacion.getArticulo();

           try{
         if(clienteOperando!=null){
//
          Pedido pedidoDelDia= pedidoFacade.getListPedidoHotByCliernteOne(clienteOperando.getId(),getHoy());

          //carritoCompraTemporalLocal=ObtenerCarrito();
            //temporal=carritoCompraTemporalLocal.buscarPublicacion(publicacion);
         if(pedidoDelDia==null){

             Pedido pedido=new Pedido();
             PedidoPK pedidoPK=new PedidoPK();
             pedidoPK.setIdArticulo(publicacion.getArticulo().getId());
             pedido.setCliente(clienteOperando);
             pedido.setNoArticuloCategoria(1);
             pedido.setCategoria(articuloOperando.getAsunto());
             pedido.setPrecioNeto(articuloOperando.getPrecioUnitario());
             pedido.setTipoEnvio("Electronico");
             PublicacionDTO publicacionDTO=procesarArticulo(publicacion,1);
             pedido.setDescuento(publicacionDTO.getDesc());
             pedido.setFechaPedido(publicacionDTO.getFechaCompra());
             pedido.setImpuesto(publicacionDTO.getImpuesto());
             pedido.setPrecioTotal(new BigDecimal(publicacionDTO.getTotal()));
             pedido.setPedidoPK(pedidoPK);
             pedidoFacade.create(pedido);

         }
         else if(pedidoDelDia!=null){

                    int idPedido=pedidoDelDia.getPedidoPK().getIdPedido();
                    Pedido temporalPedido=pedidoFacade.getPedidoByCliente(clienteOperando.getId(),articuloOperando.getId(),idPedido);
                    if(temporalPedido!=null){
                        PublicacionDTO publicacionDTO=procesarArticulo(publicacion,temporalPedido.getNoArticuloCategoria()+1);
                        temporalPedido.setDescuento(publicacionDTO.getDesc());
                        temporalPedido.setImpuesto(publicacionDTO.getImpuesto());
                        temporalPedido.setPrecioTotal(new BigDecimal(publicacionDTO.getTotal()));
                        temporalPedido.setNoArticuloCategoria(temporalPedido.getNoArticuloCategoria()+1);
                        pedidoFacade.edit(temporalPedido);
                    }

                    else {

                        Pedido pedido=new Pedido();
                        PedidoPK pedidoPK=new PedidoPK();
                        pedidoPK.setIdArticulo(publicacion.getArticulo().getId());
                        pedidoPK.setIdPedido(pedidoDelDia.getPedidoPK().getIdPedido());
                        pedido.setCliente(clienteOperando);
                        pedido.setNoArticuloCategoria(1);
                        pedido.setCategoria(articuloOperando.getAsunto());
                        pedido.setPrecioNeto(articuloOperando.getPrecioUnitario());
                        pedido.setTipoEnvio("tipo envio");
                        PublicacionDTO publicacionDTO=procesarArticulo(publicacion,1);
                        pedido.setDescuento(publicacionDTO.getDesc());
                        pedido.setFechaPedido(publicacionDTO.getFechaCompra());
                        pedido.setImpuesto(publicacionDTO.getImpuesto());
                        pedido.setPrecioTotal(new BigDecimal(publicacionDTO.getTotal()));
                        pedido.setPedidoPK(pedidoPK);
                        pedidoFacade.create(pedido);

         }
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
*/
 /*
 public String editarCarritoCompra(PublicacionDTO editar){
     try{
           if(editar.getNoArticuloCategoria()>0){
                BigDecimal total=calcularTotal(editar.getPrecioTotal(), editar.getDescuento(),editar.getImpuesto(), editar.getNoArticuloCategoria());
                editar.setPrecioTotal(total);
                pedidoFacade.edit(editar);
                JsfUtil.addSuccessMessage("Carrito de compra Actualizado Satisfactoriamente");
            }else if(editar.getNoArticuloCategoria()==0){
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
 */
 public String editarCarritoCompra(PublicacionDTO editar){
     try{
         Almacen almacen=almacenFacade.find(editar.getArticulo().getId());
         if(editar.getCantidad()>almacen.getExistencia()){
             JsfUtil.addErrorMessage("El numero de publicaciones que usted desea no se encuentran disponibles en almacen ");
             return "/carrito/Edit";
         }
         else if(editar.getCantidad() > 0)
         {
                BigDecimal total=calcularTotal(new BigDecimal(editar.getTotal()), editar.getDesc(),editar.getImpuesto(), editar.getCantidad());
                editar.setTotal(total.doubleValue());
                int posicion=carritoCompraTemporalLocal.getPosArticulo(editar);
                carritoCompraTemporalLocal.actualizarArticulo(editar, posicion);
                JsfUtil.addSuccessMessage("Carrito de compra Actualizado Satisfactoriamente");
            }else if(editar.getCantidad()<=0){
                JsfUtil.addErrorMessage("Cantidad no puede ser cero");
            }
            else{
                   JsfUtil.addErrorMessage("Error,cantidad consigno negativo");
                    return "/carrito/Edit";
            }
     }catch(Exception e){
         JsfUtil.addErrorMessage("Error problemas al editar informacion");
     }
      return "/carrito/Carrito";
 }
 
    public String borrarArticulo(PublicacionDTO articulo){
         //pedidoFacade.remove(articulo);
         carritoCompraTemporalLocal.removePublicacion(articulo);
         listcarritoDTOTemporal=null;
         JsfUtil.addSuccessMessage("Articulo eliminado Sastisfactoriamente");
         return "/carrito/Carrito";
    }
    

    public BigDecimal getMontoTotal(){

    BigDecimal monto=BigDecimal.ZERO;
        try{
        carritoCompraTemporalLocal=ObtenerCarrito();
        monto= carritoCompraTemporalLocal.getMontoTotal();
        monto=monto.setScale(2,RoundingMode.HALF_DOWN);

        }catch(Exception e){
          e.printStackTrace();;
        }
    return monto;
    }

    

    public int getcountElement(){
        if(carritoCompraTemporalLocal!=null)
        return carritoCompraTemporalLocal.getCount();
        return 0;
    }


    private PublicacionDTO procesarArticulo(Publicacion p,int cantidad){
        PublicacionDTO publicacionDTO=new PublicacionDTO();
        publicacionDTO.setIdArticulo(p.getArticulo().getId());
        publicacionDTO.setArticulo(p.getArticulo());
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
        total=total.setScale(2,BigDecimal.ROUND_HALF_UP);

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
