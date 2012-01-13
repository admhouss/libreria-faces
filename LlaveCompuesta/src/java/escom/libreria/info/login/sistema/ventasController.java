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

    private ProveedorArticulo proveedorArticulo;
    private Almacen almacen;
    private AlmacenPedido almacenPedido;
    private AlmacenPedidoPK pk;

private String mensajeError;

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }


    public String update(){//select significa la compra
   int idPedido=0;

   mensajeError="";
   
        try{

            if(selected.getEstado().equalsIgnoreCase("COMPRADO"))
           {
                  selected.setEstado(selected.getEstado());

                 
                  idPedido= selected.getIdPedido();
                  
                  List<Pedido> pedidos=pedidoFacade.getListaPedidosByidPedios(idPedido);
                  
                  try{

                  for(Pedido pedido:pedidos){
                      //pedido

                       Articulo articulo=pedido.getArticulo();
                       almacenPedido=new AlmacenPedido();
                       pk=new AlmacenPedidoPK();
                       pk.setIdPedido(idPedido);
                       pk.setIdArticulo(articulo.getId());//inicializamos nuevo articulo
                       almacenPedido.setAlmacenPedidoPK(pk);
                       almacen=almacenFacade.find(articulo.getId());
                     if(almacen==null)
                     {
                           mensajeError+="El articulo no se encuentra en almacen Codigo Articulo:"+articulo.getCodigo()+"\n";
                           continue;
                     }
                       almacenPedido.setPedido(pedido);

                     if(almacen.getEnFirme()>0)
                     {
                            almacen.setEnFirme( almacen.getEnFirme()-1);
                            almacen.setExistencia(almacen.getEnFirme()+almacen.getEnConsigna());
                            almacenPedido.setProcAlmacen("ENFIRME");
                            almacenPedido.setProveedor(null);
                     }else
                     {
                            almacen.setEnConsigna( almacen.getEnConsigna()-1);
                            almacen.setExistencia(almacen.getEnConsigna());
                            try{
                                proveedorArticulo=proveedorArticuloFacade.getProveedorMenosConsumo(pedido.getArticulo().getId());
                            } catch (Exception e){
                                mensajeError+="Error al elegir Proveedor Articulo menos vendido"+"\n" ;
                                continue;
                            }

                            proveedorArticulo.setUltMof(new Date());
                            almacenPedido.setProcAlmacen("ENCONSIGNA");
                            almacenPedido.setProveedor(proveedorArticulo.getProveedor());
                            try{
                                proveedorArticuloFacade.edit(proveedorArticulo);
                            }catch(Exception e){
                                 mensajeError+="Error al actualizar Fecha del Proveedor Elegido"+proveedorArticulo.getProveedor().getNombre()+"\n";
                            }
                     }//else


                      pedido.setEstado("COMPRADO");
                     try
                      {
                        almacenFacade.edit(almacen);
                      }catch(Exception e)
                      {
                                 mensajeError+="Error al actualizar almacen de Articulo"+almacen.getArticulo().getCodigo()+"\n";
                      }

                      try
                      {
                        almacenPedidoFacade.create(almacenPedido);
                      }catch (Exception e)
                      {
                                 mensajeError+="Error al crear Almacen Pedido"+idPedido+"\n";
                      }
                      try{
                        pedidoFacade.edit(pedido);
                      }catch(Exception e)
                      {
                                 mensajeError+="Error al actualizar Estado pedido"+idPedido+"\n";
                      }
                      ///enviar_correo(pedido);
                      
                      System.out.println("Actualizamos almacen"+mensajeError);





                 }
               }catch(Exception e){
                    System.out.println("Error en el pedido "+idPedido);
               }
            }
           compraFacade.edit(selected);
           JsfUtil.addSuccessMessage("Compra Actualizada Satisfactoriamente");

        }catch(Exception e){
            e.printStackTrace();
          JsfUtil.addErrorMessage("Error al  Actualizar la compra");
        }
        return "Edit";
    }

    public void enviar_correo(Pedido pedido){

        String tipoEnvio=pedido.getTipoEnvio();
        //if(tipoEnvio.equalsIgnoreCase("ELECTRONICO") || tipoEnvio.equalsIgnoreCase("ELECTRÒNICO")){
            try {
                List<String> libroElectronico = new ArrayList<String>();
                if (pedido.getArticulo().getArchivo() == null) {
                    String archivoErroneo = new String();
                    archivoErroneo = "C:/apache-tomcat-6.0.29.zip";
                    libroElectronico.add(archivoErroneo);
                } else {
                    libroElectronico.add(pedido.getArticulo().getArchivo());
                }
                List<String> cliente = new ArrayList<String>();
                cliente.add(pedido.getCliente().getId());
                enviarMail.enviarLibroComprado("TFJV " + pedido.getArticulo().getTitulo(), libroElectronico, "LibroEnviadoSatisfactoriamente", cliente);
                InsertarEnvioElectronico(pedido);
                InsertarEnvioExitoso(pedido);
                //String Asunto,List<String> librosElectronicos,String Cuerpo,List<String> correos
                //pedido.getTipoEnvio().equalsIgnoreCase("FISICO")
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
            envioelectronico.setLigaDescarga("liga");
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


