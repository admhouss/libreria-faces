/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.reporteador;

import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.cliente.ejb.ClienteFacade;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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


    @EJB private ClienteFacade clienteFacade;
    @EJB private ArticuloFacade articuloFacade;
    @EJB private PedidoFacade  pedidoFacade;

    public reporteadorController() {
    }

    public List<Pedido> getPedidosCliente() {
        return pedidosCliente;
    }

    public void setPedidosCliente(List<Pedido> pedidosCliente) {
        this.pedidosCliente = pedidosCliente;
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

    public String generarReporteParaClientes(){

   logger.info("clientes"+clienteArray.length);
   logger.info("descuento"+descuento);


      pedidosCliente=pedidoFacade.getReporteParaCliente(clienteArray,descuento);
      logger.info("TAMAÑAO DEL ARREGLO "+pedidosCliente.size());


        return null;
    }

    




}
