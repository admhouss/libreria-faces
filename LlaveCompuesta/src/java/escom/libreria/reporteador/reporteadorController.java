/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.reporteador;

import com.escom.info.temacliente.jsf.util.JsfUtil;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.cliente.ejb.ClienteFacade;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.proveedor.Proveedor;
import escom.libreria.jdbc.reporteador.Reportes;
import escom.libreria.jdbc.reporteador.dto.ArticuloDTO;
//import escom.libreria.jdbc.reporteador.Reportedor;
import escom.libreria.jdbc.reporteador.dto.CompraDTO;
import escom.libreria.jdbc.reporteador.dto.SuscripcionDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author xxx
 */

@ManagedBean (name="reporteadorController")
@SessionScoped
public class reporteadorController  implements Serializable{

    private static  Logger logger = Logger.getLogger(reporteadorController.class);
    private Cliente clienteArray[];
    private Articulo articuloArray[];
    private BigDecimal  descuento; /*Descuento */
    private List<Pedido> pedidosCliente; /**/
    private Proveedor reporteproveedor;
    private List<ArticuloDTO> articulosDTO; /*REPORTE ARTICULOS*/
    private List<SuscripcionDTO> suscrionesDTO; /*REPORTE SUSCRIPCIONES*/
    private List<CompraDTO>compraReporte;
    private   Cliente clienteSeleccionado;
    private Articulo articuloReporte;
    private java.util.Date fechaInicial,fechaFinal;
    private String estadoEnvio;



    @EJB private ClienteFacade clienteFacade;
    @EJB private ArticuloFacade articuloFacade;
    @EJB private PedidoFacade  pedidoFacade;

    public reporteadorController() {
    }

    public List<CompraDTO> getCompraReporte() {
        return compraReporte;
    }

    public void setCompraReporte(List<CompraDTO> compraReporte) {
        this.compraReporte = compraReporte;
    }


    public List<SuscripcionDTO> getSuscrionesDTO() {
        return suscrionesDTO;
    }

    public void setSuscrionesDTO(List<SuscripcionDTO> suscrionesDTO) {
        this.suscrionesDTO = suscrionesDTO;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }


    
    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }


    public Articulo getArticuloReporte() {
        return articuloReporte;
    }

    public void setArticuloReporte(Articulo articuloReporte) {
        this.articuloReporte = articuloReporte;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    


     public String  buscarSuscripciones(){
        try {
            
            Reportes reportes = new Reportes();
            //getArticuloReporte().getId() CAMBIAR POR 11
            Calendar calendario = GregorianCalendar.getInstance();
            Date fecha = calendario.getTime();
            System.out.println(fecha);
            //SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyy-MM-dd");
            //System.out.println(formatoDeFecha.format(getFechaInicial()));
            //System.out.println("BUSCAR SUSCRIPCION:" + getArticuloReporte());
            //System.out.println("CLIENTE SELECCIONADO:" + getClienteSeleccionado());
           // System.out.println("FECHA INICIAL" + formatoDeFecha.format(getFechaInicial()));
            //System.out.println("FECHAS FINAL" + formatoDeFecha.format(getFechaFinal()));
            //formatoDeFecha.parse("2012-01-1");
           suscrionesDTO = reportes.getReporteSuscripciones(getFechaInicial(),getFechaFinal(), clienteSeleccionado.getId(), 8, Integer.parseInt(getEstadoEnvio()));
           // logger.info("SUSCRIPCIONES SIZE:" + suscrionesDTO.size());
            return null;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(reporteadorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
     }
    public List<Pedido> getPedidosCliente() {
        return pedidosCliente;
    }

    public List<ArticuloDTO> getArticulosDTO() {
        return articulosDTO;
    }

    public void setArticulosDTO(List<ArticuloDTO> articulosDTO) {
        this.articulosDTO = articulosDTO;
    }

    public void setPedidosCliente(List<Pedido> pedidosCliente) {
        this.pedidosCliente = pedidosCliente;
    }

    public Proveedor getReporteproveedor() {
        return reporteproveedor;
    }

    public void setReporteproveedor(Proveedor reporteproveedor) {
        this.reporteproveedor = reporteproveedor;
    }

public String buscarReportArticulos(){

         StringBuilder buffer=new StringBuilder();
         for(int i=0;i<articuloArray.length;i++)
         {
             if(i==0)
                 buffer.append(String.valueOf(articuloArray[i].getId()));
             else
              buffer.append(",").append(String.valueOf(articuloArray[i].getId()));
         }
            Reportes reportes=new Reportes(buffer.toString(),reporteproveedor.getNombre(), descuento);
            articulosDTO=reportes.getReporteArticulos();
          
        return "/reporteador/ReporteArticulos";
    }



    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    


    public Articulo[] getArticuloArray() {
        return articuloArray;
    }

    public void setArticuloArray(Articulo[] articuloArray) {
        this.articuloArray = articuloArray;
    }



    public Cliente[] getClienteArray() {
        return clienteArray;
    }

    public void setClienteArray(Cliente[] clienteArray) {
        this.clienteArray = clienteArray;
    }
/*METODO QUE SE ENCARGA DE GENERAR REPORTE CLIENTES */

    private List<escom.libreria.jdbc.reporteador.dto.ClienteDTO> clientesReporte;

    public List<escom.libreria.jdbc.reporteador.dto.ClienteDTO> getClientesReporte() {
        return clientesReporte;
    }

    public void setClientesReporte(List<escom.libreria.jdbc.reporteador.dto.ClienteDTO> clientesReporte) {
        this.clientesReporte = clientesReporte;
    }

    public String generarReporteParaClientes(){

try{
      Reportes reporteCliente=new Reportes(clienteArray);
      clientesReporte=reporteCliente.getReporteCliente();
        }catch(Exception e){
           logger.error("error en reporte", e);
        }
  
        return null;
    }

    
private String tipoPago,estadoCompra;

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }


    public String buscarReporteCompra(){
          Reportes reportes=new Reportes();
          System.out.println("CLIENTE:"+clienteSeleccionado.getId());
          System.out.println("Fecha Inicio:"+getFechaInicial());
          System.out.println("Fecha Final:"+getFechaFinal());
          System.out.println("Estado Envio:"+estadoEnvio);
          System.out.println("Tipo Compra:"+tipoPago);
          System.out.println("Estado compra:"+estadoCompra);
          compraReporte=reportes.getReporteCompra(getFechaInicial(), getFechaFinal(), clienteSeleccionado.getId(), estadoCompra, tipoPago, estadoEnvio, estadoEnvio);
          JsfUtil.addSuccessMessage("RESULTADO DE LA COMPRA");


          return null;
          

    }



}
