/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author xxx
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.login.sistema;


import escom.libreria.comun.GeneradorHTML;
import escom.libreria.correo.ProcesoJMail;
import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.AlmacenPedido;
import escom.libreria.info.articulo.AlmacenPedidoPK;
import escom.libreria.info.articulo.Promocion;
import escom.libreria.info.articulo.ejb.AlmacenFacade;
import escom.libreria.info.articulo.ejb.AlmacenPedidoFacade;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Direnvio;
import escom.libreria.info.compras.EnvioFisico;
import escom.libreria.info.compras.EnvioFisicoPK;
import escom.libreria.info.compras.Envioelectronico;
import escom.libreria.info.compras.EnvioelectronicoPK;
import escom.libreria.info.compras.Enviorealizado;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.ejb.DirenvioFacade;
import escom.libreria.info.compras.ejb.EnvioFisicoFacade;
import escom.libreria.info.compras.ejb.EnvioelectronicoFacade;
import escom.libreria.info.compras.ejb.EnviorealizadoFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.descuentos.Descuento;
import escom.libreria.info.encriptamientoMD5.EncriptamientoImp;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;
import escom.libreria.info.suscripciones.Suscripcion;
import escom.libreria.info.suscripciones.SuscripcionCliente;
import escom.libreria.info.suscripciones.SuscripcionClientePK;
import escom.libreria.info.suscripciones.SuscripcionElectronica;
import escom.libreria.info.suscripciones.SuscripcionElectronicaPK;
import escom.libreria.info.suscripciones.ejb.SuscripcionClienteFacade;
import escom.libreria.info.suscripciones.ejb.SuscripcionElectronicaFacade;
import escom.libreria.info.suscripciones.ejb.SuscripcionFacade;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.tools.ant.types.Parameter;



@ManagedBean (name="ventasController")
@SessionScoped
public class ventasController implements Serializable {


    private List<Cliente> seleccionClientes;
    private List<Compra> comprascliente;
    private List<Pedido> pedidoscliente;
    private List<Articulo> reporteArticuloList;

    private Articulo[] articuloList;
    private Cliente cliente;
    private Articulo articulo;
    private static final String dwonload="http://localhost:8080/Libreria/faces/download/Create.xhtml?key=";
    private Compra selected;
    private Date fechaInicio;
    private Date fechaFinal;
    private boolean editorialesTodos;
    private String editorial;

    
    @EJB private escom.libreria.info.compras.ejb.CompraFacade compraFacade;
    @EJB private escom.libreria.info.compras.ejb.PedidoFacade pedidoFacade;
    @EJB private ProveedorArticuloFacade proveedorArticuloFacade;
    @EJB private AlmacenPedidoFacade almacenPedidoFacade;
    @EJB private AlmacenFacade almacenFacade;
    @EJB private EnviorealizadoFacade enviorealizadoFacade;
    @EJB private EnvioelectronicoFacade envioElectronico;
    @EJB private EnvioFisicoFacade envioFisicoFacade;
    @EJB private ProcesoJMail enviarMail;
    @EJB private ArticuloFacade  articuloFacade;
    @EJB private DirenvioFacade direnvioFacade;
    @EJB private SuscripcionFacade suscripcionFacade;
    @EJB private SuscripcionClienteFacade suscripcionClienteFacade;
    @EJB private SuscripcionElectronicaFacade suscripcionElectronicaFacade;

    private int suscripcion;
    private Promocion promocion;
    private Descuento descuento;
    private static  Logger logger = Logger.getLogger(ventasController.class);

    public List<Articulo> getReporteArticuloList() {
        return reporteArticuloList;
    }

    public void setReporteArticuloList(List<Articulo> reporteArticuloList) {
        this.reporteArticuloList = reporteArticuloList;
    }

    
    public Articulo[] getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(Articulo[] articuloList) {
        this.articuloList = articuloList;
    }

   




    /*buscar reporte articulos*/

    public String buscarReportArticulo(){



         //String query="SELECT a FROM Articulo a JOIN a.proveedorArticuloList p JOIN a.promocionList pro WHERE a.id=:idArticulo  AND p.proveedorArticuloPK.idProveedor=:idProveedor AND pro.promocionPK.id=:idPromocion";

          List<Integer> snames=new ArrayList<Integer>();

         for(int i=0;i<articuloList.length;i++)
          snames.add(articuloList[i].getId());

         reporteArticuloList=articuloFacade.getReporteArticulo(snames);
            System.out.println("SI ME LO TRAJO JEJEJE ?"+articuloList.length);

        return "/ventaDetalle/List";
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }



    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }




    public int getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(int suscripcion) {
        this.suscripcion = suscripcion;
    }


    public List<Compra> getComprascliente() {
        return comprascliente;
    }

    public List<Pedido> getPedidoscliente() {
        return pedidoscliente;
    }

    public Compra getSelected() {
        return selected;
    }

    public void setSelected(Compra selected) {
        this.selected = selected;
    }



 public String prepareEdit(Compra c){
     selected=c;
     pedidoscliente=pedidoFacade.getListaPedidosByidPedios(c.getIdPedido());


     return "Edit";
 }
    public void setPedidoscliente(List<Pedido> pedidoscliente) {
        this.pedidoscliente = pedidoscliente;
    }

    public void setComprascliente(List<Compra> comprascliente) {
        this.comprascliente = comprascliente;
    }


    public String regresar(){
        return "List";
    }
    public String prepareView(Compra compra){
      pedidoscliente=pedidoFacade.getListaPedidosByidPedios(compra.getIdPedido());
      return "desglose";
    }

    public String buscarComprasCliente()
    {
         if(cliente!=null){
         comprascliente=compraFacade.getComprasByCliente(cliente.getId());
         return "/gestorCompras/List";
        }
         return null;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    private String mensajeError;

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }


    public String update(){//select significa la compra
        int idPedido=0;mensajeError="";
        //Articulo articulo=null;
        Almacen almacen=null;
        ProveedorArticulo proveedorArticulo;
        Articulo articulo2=null;
  
   
        try{

            if(selected.getEstado().equalsIgnoreCase("COMPRADO"))
           {
                  selected.setEstado(selected.getEstado());  
                  idPedido= selected.getIdPedido();
                  
                  List<Pedido> pedidos=pedidoFacade.getListaPedidosByidPedios(idPedido);




                  
                  try{

                  for(Pedido pedido:pedidos)
                  {
                      articulo2=pedido.getArticulo();
                      pedido.setArticulo(pedido.getArticulo());
                      pedido.getPedidoPK().setIdArticulo(pedido.getArticulo().getId());
                      pedido.getPedidoPK().setIdPedido(pedido.getPedidoPK().getIdPedido());
                      pedido.setPedidoPK(pedido.getPedidoPK());


                      if(!pedido.getEstado().equalsIgnoreCase("COMPRADO"))
                      {
                          
                           Integer tipoArticulo=determinaTipoArticulo( articulo2.getFormato(),articulo2.getTipoArticulo().getDescripcion());

                            if((tipoArticulo==2 || tipoArticulo==0 ) && articulo2.getFormato().equalsIgnoreCase("FISICO"))
                            {
                                almacen=validarArticuloAlmacen(articulo2);
                                proveedorArticulo=validarArticuloProveedorArticulo(articulo2);

                                if(proveedorArticulo==null || almacen==null){
                                    logger.error("EL ARTICULO NO CUENTA CON PROVEEDORES .. PROVEEDOR_ARTICULO");
                                    continue;
                                }
                                logger.info("COMENZADO EL PROCESO DECREMENTAR ARTICULO DEL INVENTARIO");
                                decrementarAlmacen(almacen, proveedor, pedido);// habilitar si ya todo esta terminado
                                if(tipoArticulo==0)//SUSCRIPCION FISICA
                                {
                                    logger.info("PROCESANDO SUSCRIPCION FISICA ");
                                    insertarSuscripcionFisica(articulo2.getId(), cliente);
                                     logger.info("SUSCRIPCION FISICA PROCESADA SATISFACTORIAMENTE ");
                                    

                                }else if(tipoArticulo==2){ //publicacion fisica

                                    logger.info("PROCESANDO PUBLICACION FISICA ");
                                    InsertarEnvioFisico(pedido, selected);
                                    logger.info("PUBLICACION FISICA PROCESADA SATISFACTORIAMENTE ");

                                }

                                
                                //Insert(pedido);

                           }

                               
                          else if(tipoArticulo==2 && articulo2.getFormato().equalsIgnoreCase("ELECTRONICO"))  /*PUBLICACION ELECTRONICA*/
                          {

                              logger.info("PUBLICACION ELECTRONICA COMENZANDO PROCESO");
                              enviar_correo(pedido);
                                    
                                           try{
                                            InsertarEnvioElectronico(pedido);
                                            
                                            }catch(Exception e)
                                            {
                                             mensajeError+="Error al crear EnvioElectronico \n";
                                             logger.error("INSERTAR ENVIO ELECTRONICO CAUSO ERROR",e);
                                            }

                                        try{
                                            InsertarEnvioExitoso(pedido);

                                         }catch(Exception e)
                                         {
                                             mensajeError+="Error al crear EnvioElectronico \n";
                                              logger.error("INSERTAR ENVIO EXITOSO CAUSO ERROR",e);
                                         }
                                  
                                  
                         }else if(tipoArticulo==1 && articulo2.getFormato().equalsIgnoreCase("ELECTRONICO")){ //SUSCRIPCION ELECTRONICA

                                       logger.info("PROCESAR SUSCRIPCION ELECTRONICA");
                                       insertarSuscripcionElectronica(cliente, articulo);
                                       logger.info("SUSCRIPCION ELECTRONICA PROCESADA SATISFACTORIAMENTE");


                         }

                           logger.info("COMENZANDO EL PROCESO DE ACTUALIZACION ESTADO PEDIDO");
                           /*ACTUALIZAMOS COMPRA*/
                           pedido.setEstado(selected.getEstado());
                           pedidoFacade.edit(pedido);
                           
                          

                   }
                      
                     
                      
                 }//for

               }catch(Exception e){

                    System.out.println("Error en el pedido "+idPedido);
                    logger.error("FALLO WHY?", e);
                    return null;
               }
            }//if- final del for
            
                selected.setEstado(selected.getEstado());
                compraFacade.edit(selected);
                 //enviar_correo(selected.getIdPedido());
                JsfUtil.addSuccessMessage("Compra realizada Satisfactoriamente");
           

        }catch(Exception e){
            //e.printStackTrace();
          JsfUtil.addErrorMessage("Error al  Actualizar la compra");
        }
        return "Edit";
    }





private ProveedorArticulo proveedor;
private Almacen almacenArticulo;

private int determinaTipoArticulo(String formato,String tipoArticulo){ //SUSCRIPCION FISICA


       if(tipoArticulo.equalsIgnoreCase("SUSCRIPCION") || tipoArticulo.equalsIgnoreCase("SUSCRIPCIÒN")){
              if(formato.equalsIgnoreCase("FISCO") || formato.equalsIgnoreCase("FÌSICO"))
                  return 0;//SUSCRIPCION FIISCA
              else
                  return 1;
       }
       return 2;/*se ENTIENDE QUE ES UNA PUBLICACION  O UNA PROMOCION*/

}


   

    /*METO RESPONSABLE DE PODER DECREMENTAR DEL INVENTARIO A UN ARTICULO*/
    public boolean decrementarAlmacen(Almacen almacen,ProveedorArticulo p,Pedido pedido){
        boolean exito=true;

                         AlmacenPedido   almacenPedido=new AlmacenPedido();
                         AlmacenPedidoPK    pk=new AlmacenPedidoPK();

                         pk.setIdPedido(pedido.getPedidoPK().getIdPedido());
                         pk.setIdArticulo(almacen.getIdArticulo());
                         almacenPedido.setAlmacenPedidoPK(pk);
                         almacenPedido.setPedido(pedido);



                            if(almacen.getEnFirme()>0)
                            {
                                almacen.setEnFirme( almacen.getEnFirme()-1);
                                almacen.setExistencia(almacen.getEnFirme()+almacen.getEnConsigna());
                                almacenPedido.setProcAlmacen("ENFIRME");
                                almacenPedido.setProveedor(null);
                                try
                                {
                                    almacenFacade.edit(almacen);
                                    logger.info("ALAMACEN ACTUALIZADO SATISFACTORIAMENTE");
                                }catch(Exception e)
                                {
                                    mensajeError+="Error al actualizr almacen "+almacen.getArticulo().getCodigo()+"-"+almacen.getArticulo().getTitulo()+"\n";
                                    logger.error("error al actualizar almacen", e);
                                    exito=false;
                                }
                            }else
                            {
                                almacen.setEnConsigna( almacen.getEnConsigna()-1);
                                almacen.setExistencia(almacen.getEnConsigna());
                                p.setUltMof(new Date());
                                almacenPedido.setProcAlmacen("ENCONSIGNA");
                                almacenPedido.setProveedor(p.getProveedor());
                                try
                                {
                                    proveedorArticuloFacade.edit(p);
                                }catch(Exception e){
                                    logger.error("Error al actualizr Proveedor Articulo almacen", e);
                                    mensajeError+="Error al actualizr Proveedor Articulo almacen "+almacen.getArticulo().getCodigo()+"-"+almacen.getArticulo().getTitulo() +"\n";
                                    exito=false;
                                }

                           }

                          almacenPedidoFacade.create(almacenPedido);
                          logger.info("ALMACEN PEIDDO CREADO SATISFACTORIAMENTE");


        return true;


    }

    
    public void enviar_correo(Pedido pedido){

           Cliente clientePedido=pedido.getCliente();
           Articulo articuloPeidido=pedido.getArticulo();
           String cadenaEncripdata=null,html;
           EncriptamientoImp encriptamientoImp=new EncriptamientoImp();
           String idArticuloToidCliente=articuloPeidido.getId()+"|"+clientePedido.getId();
           /* URL FORMADA POR CLIENTE Y PEDIDO ,CONTIENE EL ID DEL ARTICULO COMPRADO Y EL CLIENTE QUE LO COMPRO SEPARADOS POR |*/
           try{
                byte[] arrelo = encriptamientoImp.encrypt(idArticuloToidCliente);
                cadenaEncripdata=encriptamientoImp.convertToHex(arrelo);
            }catch(Exception e){
                logger.error("ERROR AL INTENTAR ENCRIPTAR MD5 PARAMETROS ARTICULO | CLIENTE", e);
            }
            List<String> lista=new ArrayList<String>();
            lista.add(clientePedido.getId());
            /*CONTINE UN HTML QUE SE ENVIA A LA BANDEJA DEL CLIENTE HOTMAIL*/
            html=getFormatCompraOutHTML(clientePedido, articuloPeidido,dwonload+cadenaEncripdata);
        
            try {
                enviarMail.enviarCorreo("COMPRA EXITOSA",html,lista);
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("Error al insertar envios electronico y exitoso");
                logger.error("NO SE  PUDO ENVIAR EL CORREO",ex);
            }
        //}
    }
    /*GENERA HTML PROPORCIONANDO INFORMACION DEL CLIENTE ,ARTICULO Y LA URL */
    private String getFormatCompraOutHTML(Cliente cliente,Articulo articulo,String url){
        String HTML=null;
        GeneradorHTML gernarHTML=new GeneradorHTML();
        StringBuilder builder=new StringBuilder();
        builder.append(cliente.getNombre()).append(" ");
        builder.append(cliente.getPaterno()).append(" ");
        builder.append(cliente.getMaterno()).append(" ");
        HTML=gernarHTML.compraExitosa(builder.toString(), articulo.getTitulo(), url, articulo.getImagen());
        return HTML;

    }

/*REGISTRA UNA PUBLICACION ELECTRONICA*/
    public void  InsertarEnvioElectronico(Pedido pedido) throws Exception{
            Envioelectronico envioelectronico=new Envioelectronico();
            EnvioelectronicoPK pk=new EnvioelectronicoPK();
            pk.setIdArticulo(pedido.getArticulo().getId());
            pk.setIdPedido(pedido.getPedidoPK().getIdPedido());

            envioelectronico.setEnvioelectronicoPK(pk);
            envioelectronico.setPedido(pedido);
            envioelectronico.setLigaDescarga("LIGA");
            envioelectronico.setObservaciones("ENVIO EXITOSO");
            envioelectronico.setArticulo(pedido.getArticulo());
            envioElectronico.create(envioelectronico);
            logger.info("CREO ENVIO ELECTRONICO SATISFACTORIAMENTE");
    }
  /*REGISTRA UN ENVIO EXITOSO */
    public void  InsertarEnvioExitoso(Pedido pedido) throws Exception{
            Enviorealizado enviorealizado=new Enviorealizado();
            enviorealizado.setArtoculo(String.valueOf(pedido.getArticulo().getId()));
            enviorealizado.setFechaRecibo(new Date());
            enviorealizado.setIdPedido(pedido.getPedidoPK().getIdPedido());
            enviorealizado.setPedido(pedido);
            enviorealizado.setObservaciones("ENVIO REALIZADO");
            enviorealizadoFacade.create(enviorealizado);
            logger.info("CREO ENVIO FISICO SATISFACTORIAMETNE");
    }
    /*REGISTRA UNA PUBLICACION FISICA*/
    public void InsertarEnvioFisico(Pedido pedido,Compra compra)throws  Exception {

        Direnvio direccionEnvioCliente=null;
        EnvioFisico enviofisico=new EnvioFisico();
        EnvioFisicoPK pk=new EnvioFisicoPK();
        pk.setIdPedido(pedido.getPedidoPK().getIdPedido());;
        pk.setIdArticulo(pedido.getPedidoPK().getIdArticulo());
        /*Intentando de obtener Dirrecion Fisica*/

        String direccionEnvio=compra.getDireccionEnvio();
        direccionEnvioCliente=direnvioFacade.getDireccionEnvioEspecifica(direccionEnvio,compra.getIdCliente());
        if(direccionEnvio==null)
        logger.error("OCURRIO UN ERROR AL INTENTAR OBTENER LA DIRECCION DE ENVIO DEL CLIENTE"+direccionEnvio);
        enviofisico.setDirenvio(direccionEnvioCliente);
        enviofisico.setArticulo(pedido.getArticulo());
        enviofisico.setNoGuia(String.valueOf(0));
        enviofisico.setObservaciones("observaciones");
        enviofisico.setPaqueteria("paqueteria");
        enviofisico.setPedido(pedido);
        enviofisico.setEnvioFisicoPK(pk);
        envioFisicoFacade.create(enviofisico);
        logger.info("ENVIO FISICO CREADO SATISFACTORIAMENTE");

    }
/*REGISTRA UNA SUSCRIPCION FISICA PARA QUE POSTERIORMENTE SE LE DE SEGUIMIENTO*/
  private void insertarSuscripcionFisica(Integer idSuscripcion,Cliente cliente){

      SuscripcionCliente suscripcionCliente=new SuscripcionCliente();
      SuscripcionClientePK pk=new SuscripcionClientePK();
      List<Articulo> suscripcionArticulos=suscripcionFacade.getArticulosByID(idSuscripcion);
      logger.info("OBTENIENDO LOS ARTICULOS ASOSICADOS A ESTA SUSCRIPCION, SATISFACTORIAMENTE");
      pk.setIdCliente(cliente.getId());
      pk.setIdSuscripcion(idSuscripcion);


      for(Articulo a:suscripcionArticulos)
      {
          try{
                Suscripcion s=new Suscripcion(idSuscripcion,a.getId());
                suscripcionCliente.setArticulo(articulo);
                suscripcionCliente.setCliente(cliente);
                suscripcionCliente.setEstadoEnvio(false);
                suscripcionCliente.setSuscripcionClientePK(pk);
                suscripcionCliente.setSuscripcion(s);
                suscripcionClienteFacade.create(suscripcionCliente);
                logger.info("SUSCRIPCION CLIENTE ALAMACENADA SATISFACTORIAMENTE");

          }catch(Exception e){
              logger.error("NO PUDO CREAR SUSCRIPCION CLIENTE ERROR", e);
          }

      }
     logger.info("DATOS ALAMCENADOS SATISFACTORIAMENTE");

  }

  /*METDO RESPONSABLE PARA ALMACENAR LAS SUCRIPCIONES ELECTRONICAS DE UN CLIENTE ESPECIFICO*/
  private void insertarSuscripcionElectronica(Cliente cliente,Articulo idArticulo)throws Exception{

            SuscripcionElectronica suselectronica=new SuscripcionElectronica();
            SuscripcionElectronicaPK pk=new SuscripcionElectronicaPK();
            Suscripcion suscripcionElectronica=new Suscripcion(idArticulo.getId(), idArticulo.getId());
            pk.setIdCliente(cliente.getId());
            pk.setIdSuscripcion(idArticulo.getId());
            suselectronica.setSuscripcionElectronicaPK(pk);
            suselectronica.setCliente(cliente);
            suselectronica.setFechaFin(new Date());
            suselectronica.setFechaInicio(new Date());
            suselectronica.setNoLicencias(0);
            suselectronica.setSuscripcion(suscripcionElectronica);
            suscripcionElectronicaFacade.create(suselectronica);
            logger.info("SUSCRIPCION ELECTRONICA CREADA SATISFACTORIAMENTE,IT`S OK!");

  }



    private Almacen validarArticuloAlmacen(Articulo articulo){
        Almacen almacen=null;
        try{
           almacen=almacenFacade.find(articulo.getId());
        }catch(Exception e)
        {
            mensajeError+="El articulo"+articulo.getCodigo()+"-"+articulo.getTitulo()+" no se encuentra en almacen \n";
        }
        return almacen;
    }

    public ProveedorArticulo validarArticuloProveedorArticulo(Articulo articulo){
        ProveedorArticulo  proveedorArticulo=null;
        try{
           proveedorArticulo=proveedorArticuloFacade.getProveedorMenosConsumo(articulo.getId());
        }catch(Exception e1)
        {
            mensajeError+="El articulo:"+articulo.getCodigo()+"-"+articulo.getTitulo()+" no  tiene proveedores \n";
        }
        return  proveedorArticulo;
    }

    public boolean isEditorialesTodos() {
        return editorialesTodos;
    }

    public void setEditorialesTodos(boolean editorialesTodos) {
        this.editorialesTodos = editorialesTodos;
    }






    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }



    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public List<Cliente> getSeleccionClientes() {
        return seleccionClientes;
    }

    public void setSeleccionClientes(List<Cliente> seleccionClientes) {
        this.seleccionClientes = seleccionClientes;
    }


    public ventasController() {
    }



    public String buscarReporteArticulos(){

       Pedido p=new Pedido();

        //c.getIdPedido()

        String queryArmado="SELECT p FROM Pedido p WHERE p.pedidoPK.idArticulo ";





        System.out.println("Editorial"+editorial);
        System.out.println("fechas Inicio"+getFechaInicio());
        System.out.println("fechas Inicio"+getFechaFinal());
        System.out.println("Cliente"+getCliente());


       return null;
    }




}


