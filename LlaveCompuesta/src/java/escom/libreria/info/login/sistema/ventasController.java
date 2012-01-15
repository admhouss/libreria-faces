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


import escom.libreria.correo.ProcesoJMail;
import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.AlmacenPedido;
import escom.libreria.info.articulo.AlmacenPedidoPK;
import escom.libreria.info.articulo.ejb.AlmacenFacade;
import escom.libreria.info.articulo.ejb.AlmacenPedidoFacade;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Envioelectronico;
import escom.libreria.info.compras.EnvioelectronicoPK;
import escom.libreria.info.compras.Enviorealizado;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.ejb.EnvioelectronicoFacade;
import escom.libreria.info.compras.ejb.EnviorealizadoFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.encriptamientoMD5.EncriptamientoImp;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;
import java.util.ArrayList;



@ManagedBean (name="ventasController")
@SessionScoped
public class ventasController implements Serializable {


    private List<Cliente> seleccionClientes;
    private List<Compra> comprascliente;
    private List<Pedido> pedidoscliente;
    private Cliente cliente;
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
    @EJB private ProcesoJMail enviarMail;



    private int suscripcion;

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

  /* private ProveedorArticulo proveedorArticulo;
    private Almacen almacen;
    private AlmacenPedido almacenPedido;
    private AlmacenPedidoPK pk;
*/
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
        Articulo articulo=null;
  
   
        try{

            if(selected.getEstado().equalsIgnoreCase("COMPRADO"))
           {
                  selected.setEstado(selected.getEstado());  
                  idPedido= selected.getIdPedido();
                  
                  List<Pedido> pedidos=pedidoFacade.getListaPedidosByidPedios(idPedido);




                  
                  try{

                  for(Pedido pedido:pedidos)
                  {
                      articulo=pedido.getArticulo();
                      pedido.setArticulo(pedido.getArticulo());
                      pedido.getPedidoPK().setIdArticulo(pedido.getArticulo().getId());
                      pedido.getPedidoPK().setIdPedido(pedido.getPedidoPK().getIdPedido());
                      pedido.setPedidoPK(pedido.getPedidoPK());


                      if(!pedido.getEstado().equalsIgnoreCase("COMPRADO"))
                      {
                          

                           //articulo=pedido.getArticulo();
                           almacen=validarArticuloAlmacen(articulo);
                           proveedorArticulo=validarArticuloProveedorArticulo(articulo);

                           if(proveedorArticulo==null || almacen==null)
                           continue;






                           if(pedido.getTipoEnvio().equalsIgnoreCase("ELECTRONICO") || pedido.getTipoEnvio().equalsIgnoreCase("ELECTRÒNICO"))
                           {
                                    enviar_correo(pedido);
                                    
                                  try{
                                            InsertarEnvioElectronico(pedido);
                                     }catch(Exception e)
                                    {
                                          mensajeError+="Error al crear EnvioElectronico \n";
                                    }
                                  
                          }
                         else
                          {
                             System.out.println("COMPRA CASO NO CONTEMPLADO");
                            //   continue;
                               // NECESITO DIRECCION DE ENVIO
                              // InsertarEnvioFisic
                          }
                           /*ACTUALIZAMOS COMPRA*/
                           pedido.setEstado(selected.getEstado());
                           pedidoFacade.edit(pedido);
                           decrementarAlmacen(almacen, proveedor, pedido);
                          

                   }
                      
                     
                      
                 }//for

               }catch(Exception e){

                    System.out.println("Error en el pedido "+idPedido);
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

    /*public boolean  processarPedido(Pedido  pedido){


          if(!pedido.getEstado().equalsIgnoreCase("COMPRADO"))
          {
                           Articulo articulo=pedido.getArticulo();

                           almacenArticulo=validarArticuloAlmacen(articulo);
                           proveedor=validarArticuloProveedorArticulo(articulo);

                           if(almacenArticulo==null || proveedor==null)
                             return false;
        }


                          
                    
                            pedido.setEstado("COMPRADO");
                     /* try
                      {
                        almacenPedidoFacade.create(almacenPedido);
                      }catch (Exception e)
                      {
                                error+="Error al crear Almacen Pedido"+pedido.getPedidoPK().getIdPedido()+"\n";
                      }
                      try{
                        pedidoFacade.edit(pedido);
                      }catch(Exception e)
                      {
                                 error+="Error al actualizar Estado pedido"+pedido.getPedidoPK().getIdPedido()+"\n";
                      }

                      /

                      }
          return true;
    }*/


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
                                }catch(Exception e)
                                {
                                    mensajeError+="Error al actualizr almacen "+almacen.getArticulo().getCodigo()+"-"+almacen.getArticulo().getTitulo()+"\n";
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
                                    mensajeError+="Error al actualizr Proveedor Articulo almacen "+almacen.getArticulo().getCodigo()+"-"+almacen.getArticulo().getTitulo() +"\n";
                                    exito=false;
                                }

                           }

                          almacenPedidoFacade.create(almacenPedido);


        return true;


    }

    private String dwonload="http://localhost:8080/Libreria/faces/download/Create.xhtml?key=";
    public void enviar_correo(Pedido pedido){


           EncriptamientoImp encriptamientoImp=new EncriptamientoImp();
           String idArticuloToidCliente=pedido.getArticulo().getId()+"|"+pedido.getCliente().getId();/* URL FORMADA POR CLIENTE Y PEDIDO*/
           String url=null;
        
            try {
            byte[] arrelo = encriptamientoImp.encrypt(idArticuloToidCliente);
            url=encriptamientoImp.convertToHex(arrelo);
            List<String> lista=new ArrayList<String>();
            lista.add(pedido.getCliente().getId());
            enviarMail.enviarCorreo("COMPRA EXITOSA",dwonload+url,lista);
            
            InsertarEnvioElectronico(pedido);
            InsertarEnvioExitoso(pedido);
                
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("Error al insertar envios electronico y exitoso");
                ex.printStackTrace();
                Logger.getLogger(ventasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
    }
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
    }
    public void  InsertarEnvioExitoso(Pedido pedido) throws Exception{
            Enviorealizado enviorealizado=new Enviorealizado();
            enviorealizado.setArtoculo(""+pedido.getArticulo().getId());
            enviorealizado.setFechaRecibo(new Date());
            enviorealizado.setIdPedido(pedido.getPedidoPK().getIdPedido());
            enviorealizado.setPedido(pedido);
            enviorealizado.setObservaciones("ENVIO REALIZADO");
            enviorealizadoFacade.create(enviorealizado);
    }

    public Almacen validarArticuloAlmacen(Articulo articulo){
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



    public String buscarPorEditorial(){

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


