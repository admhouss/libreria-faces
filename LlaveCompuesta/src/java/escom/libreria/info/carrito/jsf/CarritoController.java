/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;


import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.Impuesto;
import escom.libreria.info.articulo.Publicacion;
import escom.libreria.info.articulo.ejb.ImpuestoFacade;
import escom.libreria.info.articulo.ejb.PromocionFacade;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Direnvio;
import escom.libreria.info.compras.Pendiente;
import escom.libreria.info.compras.Zona;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.compras.ejb.PendienteFacade;
import escom.libreria.info.compras.jsf.DirenvioController;
import escom.libreria.info.descuentos.DescuentoArticulo;
import escom.libreria.info.descuentos.DescuentoCliente;
import escom.libreria.info.descuentos.DescuentoClientePK;
import escom.libreria.info.descuentos.ejb.DescuentoArticuloFacade;
import escom.libreria.info.descuentos.ejb.DescuentoClienteFacade;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.login.ejb.SistemaFacade;

import escom.libreria.info.login.sistema.SistemaController;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;
import escom.libreria.info.suscripciones.Suscripcion;
import escom.libreria.info.suscripciones.jsf.SuscripcionController;
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
import javax.ejb.EJB;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
    @ManagedProperty("#{suscripcionController}")
    private SuscripcionController suscripcionController;

    @EJB private  SistemaFacade sistemaFacade;
    @EJB private DescuentoClienteFacade descuentoClienteFacade;
    @EJB private DescuentoArticuloFacade descuentoArticuloFacade;
    @EJB private ImpuestoFacade  impuestoFacade;
    @EJB private ProveedorArticuloFacade proveedorArticuloFacade; 
    @EJB private PedidoFacade pedidoFacade;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenFacade almacenFacade;
    @EJB private PromocionFacade promocionFacade;
    @EJB private ArticuloFacade articuloFacade;
    @EJB private PendienteFacade pendienteFacade;

    public Articulo articulo;
    private static  Logger logger = Logger.getLogger(CarritoController.class);

    public SuscripcionController getSuscripcionController() {
        return suscripcionController;
    }

    public void setSuscripcionController(SuscripcionController suscripcionController) {
        this.suscripcionController = suscripcionController;
    }



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

    public String agregarSuscripcion(){
       Articulo idArticuloRoot = null;/*Ariculo que conforma a la suscripcion*/
       Cliente cliente=sistemaController.getCliente();
       PublicacionDTO root;


       if(cliente!=null)
       {
        List<Suscripcion> lista = suscripcionController.getListasuscripciones();
       

        PublicacionDTO articuloProcesadoTemporal = null,articuloProcesado;
        if(lista==null || lista.isEmpty()){
            JsfUtil.addErrorMessage("La lista de suscripciones esta basica");
            return null;
        }
        else
        {
                for(Suscripcion s:lista)
                {
                         Articulo articulo=s.getArticulo()  ;
                         articuloProcesado= procesarArticulo(articulo,1);
                         if(articuloProcesadoTemporal==null)
                         {
                             articuloProcesadoTemporal=articuloProcesado;//INICIALIZANDO ARTICULO PROCESADO
                             idArticuloRoot=articuloFacade.find(s.getSuscripcionPK().getIdSuscripcion());
                             root=procesarArticulo(idArticuloRoot,1);
                             articuloProcesadoTemporal.setDesc(articuloProcesadoTemporal.getDesc().add(root.getDesc()));
                             articuloProcesadoTemporal.setTotal(articuloProcesadoTemporal.getTotal()+ root.getTotal());
                             articuloProcesadoTemporal.setPrecio(articuloProcesadoTemporal.getPrecio().add(root.getPrecio()));
                             articuloProcesadoTemporal.setArticulo(idArticuloRoot);
                             articuloProcesadoTemporal.setIdArticulo(idArticuloRoot.getId());
                             articuloProcesadoTemporal.setTitulo(idArticuloRoot.getTitulo());
                             /*se teando el primer articulo de la suscripcion*/
                             logger.info("PROCESANDO ARTICULO Y ROOT ARTICULO  PRECIO TOTAL"+articuloProcesadoTemporal.getTotal());
                         }else
                         {
                          articuloProcesadoTemporal.setArticulo(idArticuloRoot);
                          articuloProcesadoTemporal.setIdArticulo(idArticuloRoot.getId());
                          articuloProcesadoTemporal.setDesc(articuloProcesadoTemporal.getDesc().add(articuloProcesado.getDesc()));
                          articuloProcesadoTemporal.setTotal(articuloProcesadoTemporal.getTotal()+ articuloProcesado.getTotal());
                          articuloProcesadoTemporal.setPrecio(articuloProcesadoTemporal.getPrecio().add(articuloProcesadoTemporal.getPrecio()));
                         }
                          
                          //articuloProcesadoTemporal.setIdPublicacion(s.getSuscripcionPK().getIdSuscripcion());
                         System.out.println("procesando");

                }
        }
        

          carritoCompraTemporalLocal=ObtenerCarrito();
          agregarCarrito(articuloProcesadoTemporal);
        }else{
           JsfUtil.addErrorMessage("Favor de registrarse");
           return "/login/Create";
        }

        return "/carrito/Carrito";
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
        //return direccionEnvio.getEstado().getZona().getPeso();
        return null;
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
            logger.info("ERROR AL CALCULA LA FECHA DE HOY");
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

   public void borrarCarrito(){
       carritoCompraTemporalLocal=null;
   }

   private void guardarClienteInsatisfecho(int count,Articulo artc,Cliente client){
       try{
            Pendiente pendiente=new Pendiente();
            pendiente.setArticulo(artc);
            pendiente.setCliente(client);

            pendiente.setFecha(new Date());
            pendiente.setIdArticulo(artc.getId());
            pendiente.setNoArtSolicitados(count);
            pendiente.setObservacones("EL ARTICULO NO SE ENCUENTRA EN ALMACEN");
            pendienteFacade.create(pendiente);
             logger.info("PENDIENTE INSATISFECHO CREADO SATISFACTORIAMENTE");
       }catch(Exception e){
           logger.error("ERROR AL INTENTAR  CREAR ARTICULOS PENDIENTE");
       }

   }
 public String agregarArticulo(Articulo art){

     Articulo articulo1=art;/*VALIDAR SI ESTE ARTICULO TIENE PROMOCION*/
     Almacen almacen=almacenFacade.find(articulo1.getId());
     Cliente clienteOperando=null;
     try{
     clienteOperando=sistemaController.getCliente();/*CLIENTE ACTUAL */
     }catch(Exception e){
       JsfUtil.addErrorMessage("Lo sentimos,usuario  no registrado");
       return "/login/Create.xhtml";
     }

     PublicacionDTO temporalDTO=null;//
     if(almacen==null || almacen.getExistencia()<=0)
    {       if(clienteOperando!=null)
             guardarClienteInsatisfecho(1,articulo1, clienteOperando);
         JsfUtil.addErrorMessage("La publicacion que desea no se encuentra en Almacen");
         return null;
     }else
     {

           try{
                 if(clienteOperando!=null){

                    carritoCompraTemporalLocal=ObtenerCarrito();
                    temporalDTO=carritoCompraTemporalLocal.buscarPublicacion(articulo1);

                      if(temporalDTO==null){

                             temporalDTO=procesarArticulo(articulo1,1);
                             temporalDTO.setEditorial( articulo1.getFormato());

                             //temporalDTO.setIdPublicacion(publicacion.getIdDc());
                            // temporalDTO.setTypePublicacion(false);
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

 public String editarCarritoCompra(PublicacionDTO editar){
     try{
         Cliente client=sistemaController.getCliente();
         Almacen almacen=almacenFacade.find(editar.getArticulo().getId());
         if(editar.getCantidad()>almacen.getExistencia())
         {
             guardarClienteInsatisfecho(editar.getCantidad(),editar.getArticulo(), client);
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

/*dado un articulo determinamos su costo real,tomando en cuenta IMPUESTOS,DESCUENTO_ARTICULO,DESCIENTO_CLIENTE,PROMOCION!*/
    private PublicacionDTO procesarArticulo(Articulo articulo,int cantidad){
        BigDecimal precioPublico;/*VARIABLE QUE SE SETTEA SI EL ARTICULO TIENE PROMOCION*/
        PublicacionDTO publicacionDTO=new PublicacionDTO();
        publicacionDTO.setIdArticulo(articulo.getId());
        publicacionDTO.setArticulo(articulo);
        publicacionDTO.setTitulo(articulo.getTitulo());
        publicacionDTO.setAutor(articulo.getCreador());
        publicacionDTO.setAsunto(articulo.getAsunto());
        publicacionDTO.setCantidad(cantidad);
        publicacionDTO.setFechaCompra(new Date());
        BigDecimal descuento=obtenerMayorDescuento(publicacionDTO.getIdArticulo());
        publicacionDTO.setDesc(descuento);
        BigDecimal impuesto=getImpuesto(publicacionDTO.getIdArticulo());  /*sumamos los impuestos del articulo*/
        publicacionDTO.setImpuesto(impuesto);
        publicacionDTO.setPrecio(articulo.getPrecioUnitario());
        
         precioPublico=calcularPromocionArticulo(articulo);
         if(!precioPublico.equals(BigDecimal.ZERO)){
         publicacionDTO.setPrecio(precioPublico);
         logger.info("EL ARTICULO TIENE PROMOCION "+precioPublico);
        }
         
        

        BigDecimal total=calcularTotal(publicacionDTO.getPrecio(), descuento, impuesto, publicacionDTO.getCantidad());
        total=total.setScale(2,BigDecimal.ROUND_HALF_UP);
        publicacionDTO.setTotal(total.doubleValue());
        return publicacionDTO;
    }

    private BigDecimal calcularTotal(BigDecimal precioUnitario,BigDecimal descuento,BigDecimal impuesto,int cantidad){
        BigDecimal total=BigDecimal.ZERO;
        BigDecimal impuestoPrecio=BigDecimal.ZERO;
        total=precioUnitario.multiply(new BigDecimal(cantidad));
        ///System.out.println("total + cantidad"+total);

        if(impuesto.compareTo(BigDecimal.ZERO)!=0){
            impuestoPrecio=getPrecioIVA(impuesto.doubleValue(), total.doubleValue());
           // System.out.print("impuestorecibido"+impuestoPrecio);
            total=impuestoPrecio.add(total);
            logger.info("EL ARTICULO TIENE IMPUESTO");
            //System.out.println("total+ impuesto"+total+"impuesto"+impuesto);
        }
        
       if(descuento.compareTo(BigDecimal.ZERO)!=0){
          total=getPrecioDescuentoAplicado(descuento.doubleValue(),total.doubleValue());
          logger.info("EL ARTICULO TIENE DESCUENTO");
        }
        //System.out.println("ANTES DEL total"+total);
        total=total.setScale(2,total.ROUND_UP);
         logger.info("FINALMENTE CALCULAMOS TOTAL");
        return total;
    }

    private BigDecimal obtenerMayorDescuento(int idArticulo){
        BigDecimal descuentoArticulo=getDescuentoArticulo(idArticulo);
        BigDecimal descuentoCliente=getDescuentoCliente();

        logger.info("VERIFICANDO QUE DESCUENTO ES MAYOR SI EL DEL CIENTE O EL ARTICULO");
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

    private boolean isAplicaPromocion(Articulo articulo) {
      String descripcion=articulo.getTipoArticulo().getDescripcion();
      String formato=articulo.getFormato();
         if(descripcion.equalsIgnoreCase("SUSCRIPCION") && ( formato.equalsIgnoreCase("FISICO") || formato.equalsIgnoreCase("ELECTRONICO"))){
             return false;
         }

      return true;

    }

    private BigDecimal calcularPromocionArticulo(Articulo articulo){
        BigDecimal promocion  =promocionFacade.getPromocionArticulo(articulo);
        return promocion;
    }

}
