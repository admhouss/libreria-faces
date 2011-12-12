/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generarFactura.jsf;

import com.escom.info.compra.Compra;
import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.Pedido;
import com.escom.info.compra.jsf.util.JsfUtil;
import com.escom.libreria.mysuit.FactDocsMX;
import com.escom.libreria.mysuit.TComprobanteEx.Transportistas.ConceptosDeCobro.Concepto;
import com.escom.libreria.mysuit.TFactDocMX;
import com.escom.libreria.mysuit.TFactDocMX.Conceptos;
import com.escom.libreria.mysuit.TFactDocMX.Emisor;
import com.escom.libreria.mysuit.TFactDocMX.Identificacion;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author xxx
 */
@ManagedBean (name="generarFacturaController")
@SessionScoped
public class GenerarFacturaController implements Serializable {
    
    @EJB private com.escom.info.generarFactura.GeneraraFacade generaraFacade;
    @EJB private com.escom.info.compra.ejb.DifacturacionFacade difacturacionFacade;
    @EJB private com.escom.info.compra.ejb.PedidoFacade pedidoFacade;

    public GenerarFacturaController() {
    }


    public  String generarFactura(Compra compra){
        //generaraFacade.
        Compra comp=compra;
        Difacturacion clientef;
        List<Difacturacion> direcion_facturacion_clientes=difacturacionFacade.getDireccionFacturaCliente(comp.getIdcliente());
        if(direcion_facturacion_clientes==null || direcion_facturacion_clientes.isEmpty()){
            JsfUtil.addSuccessMessage("Direccion de factura no encontrada");
            return "/cliente/modulo";
        }
        clientef=direcion_facturacion_clientes.get(0);
        generarXMLFactura(clientef, comp);
        return "/compra/List";
    }

    private void generarXMLFactura(Difacturacion d,Compra c){
        Emisor direccionEmisor = generaraFacade.getDireccionEmisorAndFiscal(d);
        c.setIdPedido(c.getIdPedido());
        List<Pedido> pedidos=pedidoFacade.getAllpedidosByid(c.getIdPedido());
        Conceptos concepto = generaraFacade.crearConceptos(pedidos);
       if(crear_objeto_xml(direccionEmisor,concepto,c)){
           System.out.println("HE QUE A TODA MADRE!!");

       }

    }

    private boolean  crear_objeto_xml(Emisor e,Conceptos conceptos,Compra c){
            boolean bandera;
        FactDocsMX factDocsMX=new FactDocsMX();
        TFactDocMX factura=new TFactDocMX();
        factura.setIdentificacion(generaraFacade.getIdentificacion());
        factura.setProcesamiento(generaraFacade.getProcesamiento());
        factura.setEmisor(e);
        factura.setReceptor(generaraFacade.getReceptor());
        factura.setConceptos(conceptos);
        factura.setVersion(new BigInteger("4"));
        factura.setTotales(generaraFacade.getTotales(c));
        factDocsMX.getFactDocMX().add(factura);
        bandera=crear_xml(factDocsMX);
        return bandera;
    }

    private  boolean crear_xml(FactDocsMX  factDocsMX){

         try {
                javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance( factDocsMX.getClass().getPackage().getName());
                javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
               // marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
               // marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(factDocsMX, new File("C:/Users/xxx/Desktop/facturasPrueba/","facturaCFD0.xml"));
    //marshaller.marshal(factDocsMX, System.out);
            } catch (javax.xml.bind.JAXBException ex) {
    // XXXTODO Handle exception
                System.out.println("Error al construir el XML"+ex.getMessage());
                
                //java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
            }

         return true;

}
}
