/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generarFactura;

import com.escom.info.compra.Compra;
import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.FacturaGeneral;
import com.escom.info.compra.Pedido;
import com.escom.libreria.mysuit.TAsignacion;
import com.escom.libreria.mysuit.TCountryCode;
import com.escom.libreria.mysuit.TCurrencyCode;
import com.escom.libreria.mysuit.TDictionaries;
import com.escom.libreria.mysuit.TDictionary;
import com.escom.libreria.mysuit.TDictionary.Entry;
import com.escom.libreria.mysuit.TDomicilioMexicano;
import com.escom.libreria.mysuit.TFactDocMX;
import com.escom.libreria.mysuit.TFactDocMX.Conceptos;
import com.escom.libreria.mysuit.TFactDocMX.Emisor;
import com.escom.libreria.mysuit.TFactDocMX.Identificacion;
import com.escom.libreria.mysuit.TFactDocMX.Identificacion.AsignacionSolicitada;
import com.escom.libreria.mysuit.TFactDocMX.Receptor;
import com.escom.libreria.mysuit.TFactDocMX.Receptor.DomicilioDeRecepcion;
import com.escom.libreria.mysuit.TFactDocMX.Totales;
import com.escom.libreria.mysuit.TNonNegativeAmount;
import com.escom.libreria.mysuit.TSenderCountryCode;
import com.escom.libreria.mysuit.TTipoDeDocumento;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author xxx
 */
@Stateless
public class GeneraraFacade {


    private  TDomicilioMexicano getDireccionFiscal(Difacturacion difacturacion) {

        TDomicilioMexicano domicilioEmision=new TDomicilioMexicano();
        domicilioEmision.setCalle(difacturacion.getCalle());
        domicilioEmision.setCodigoPostal(difacturacion.getCp()+"");
        domicilioEmision.setColonia(difacturacion.getColonia());
        domicilioEmision.setEstado(difacturacion.getEstado().getNombre());
        domicilioEmision.setNumeroExterior(difacturacion.getNoExterior());
        domicilioEmision.setNumeroInterior( difacturacion.getNoInterior());
        domicilioEmision.setMunicipio(difacturacion.getDelMunicipio());

        return domicilioEmision;

    }


    public Receptor getReceptor() {
        Receptor receptor=new Receptor();
        receptor.setCdgPaisReceptor(TCountryCode.MX);
        receptor.setRFCReceptor("UGV820101TI0");
        receptor.setNombreReceptor("UHTHOFF GOMEZ VEGA & UHTHOFF SC");
        receptor.setDomicilioDeRecepcion(getRecepcion());
        return receptor;
    }

    public Emisor getDireccionEmisorAndFiscal(Difacturacion difacturacion){
             Emisor emisor=getDireccionEmisor();
             TDomicilioMexicano direccionFiscal = getDireccionFiscal(difacturacion);
             emisor.setDomicilioFiscal(direccionFiscal);
             return emisor;
    }


    public  Totales getTotales(Compra compra) {
        Totales total=new Totales();
        total.setMoneda(TCurrencyCode.MXN);
        total.setTipoDeCambioVenta(new BigDecimal("0.0000"));
        total.setSubTotal(getImporte(compra.getPagoTotal()));
        //total.setTotalEnLetra("CIENTO VEINTITRES PESOS 00/100 MN");
        //total.setResumenDeImpuestos();
        //total.setFormaDePago(null);
        return total;
    }
    public  TDictionaries getProcesamiento() {
        TDictionaries dictionaries=new TDictionaries();
        TDictionary e=new TDictionary();
        e.setName("email");
        Entry entry=new Entry();
        entry.setK("from");
        entry.setV("ACCOUNT_OWNER");
        Entry entry2=new Entry();
        entry2.setK("to");
        entry2.setV("libreria.insurgentes.tfjfa@gmail.com");
        Entry entry3=new Entry();
        entry3.setK("formats");
        entry3.setV("xml pdf");

        e.getEntry().add(entry);
        e.getEntry().add(entry2);
        e.getEntry().add(entry3);

        dictionaries.getDictionary().add(e);
        return dictionaries;
    }

     public  TAsignacion getAsignacion(FacturaGeneral facturaGeneral){
        XMLGregorianCalendar date=null ;
        TAsignacion asignacion=new TAsignacion();
        asignacion.setFolio(new BigInteger(facturaGeneral.getFolio()));
        asignacion.setAnoDeAprobacion(new Date().getYear());
        asignacion.setNumeroDeAprobacion(new BigInteger("1"));
        GregorianCalendar c = new GregorianCalendar();
        try {
            date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException ex) {
           // Logger.getLogger(GeneraraFacade.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio un error en generar facturar");
        }
        asignacion.setTiempoDeAutorizacion(date);
        asignacion.setTiempoDeEmision(date);
        return asignacion;
    }
    public  Identificacion getIdentificacion() {
        Identificacion identificacion=new Identificacion();
      //  identificacion.setAsignacionSolicitada(null);


        identificacion.setCdgPaisEmisor(TSenderCountryCode.MX);
        identificacion.setTipoDeComprobante(TTipoDeDocumento.FACTURA);
        identificacion.setRFCEmisor("TFJ360831MTA");
        identificacion.setRazonSocialEmisor("TRIBUNAL FEDERAL DE JUSTICIA FISCAL Y ADMINISTRATIVA");
        identificacion.setUsuario("INSURGENTES");
        identificacion.setNumeroInterno("FACTURA ELECTRONICA INSUR FIN-000041");
        //identificacion.setAsignacionSolicitada(getAsignacionSolicitada(facturaGeneral));


       
        return identificacion;

    }

     private  Emisor getDireccionEmisor(){  /*Falta adjuntar la direccion del cliente*/
      Emisor emisor=new TFactDocMX.Emisor();
      TDomicilioMexicano domicilio=new TDomicilioMexicano();
      domicilio.setCalle("AV. INSURGENTES SUR");
      domicilio.setNumeroExterior("881");
      domicilio.setLocalidad("MEXICO");
      domicilio.setColonia("NAPOLES");
      domicilio.setMunicipio("BENITO JUAREZ");
      domicilio.setCodigoPostal("03810");
      domicilio.setEstado("DISTRITO FEDERAL");
      domicilio.setPais("Mexico");
      domicilio.setNumeroExterior("881");
      emisor.setDomicilioDeEmision(domicilio);
      return emisor;
      }

    private AsignacionSolicitada getAsignacionSolicitada(FacturaGeneral facturaGeneral) {
         XMLGregorianCalendar date=null;
        AsignacionSolicitada asignacionSolicitada=new AsignacionSolicitada();
        asignacionSolicitada.setFolio(new BigInteger(facturaGeneral.getFolio()));
        asignacionSolicitada.setSerie(facturaGeneral.getNoSerie());
        GregorianCalendar c = new GregorianCalendar();
        try {
         date= DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException ex) {
           // Logger.getLogger(GeneraraFacade.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio un error en generar facturar");
        }
        asignacionSolicitada.setTiempoDeEmision(date);
        return asignacionSolicitada;
    }


     public  Conceptos crearConceptos(List<Pedido> pedidos) {
        Conceptos crear_concepto=new Conceptos();
        List<Conceptos.Concepto>listconcepto=crear_concepto.getConcepto();

        for(Pedido pedido:pedidos){

            Conceptos.Concepto miconepto=new Conceptos.Concepto();
            miconepto.setCantidad(new BigDecimal(pedido.getNoArticuloCategoria()));
            miconepto.setUnidadDeMedida(pedido.getArticulo().getUnidad());
            miconepto.setImporte(getImporte(pedido.getPrecioTotal()));
            miconepto.setCodigo(pedido.getArticulo().getCodigo());
            miconepto.setDescripcion(pedido.getArticulo().getTitulo());
            listconcepto.add(miconepto);
         }
        return crear_concepto;
    }
      private TNonNegativeAmount getImporte(BigDecimal importe_monto) {
        TNonNegativeAmount importe=new TNonNegativeAmount();
        importe.setValue(importe_monto);
        return importe;
    }

    public DomicilioDeRecepcion getRecepcion() {
        DomicilioDeRecepcion domicilio=new DomicilioDeRecepcion();
        TDomicilioMexicano domicilio_mexico=new TDomicilioMexicano();
        domicilio_mexico.setCalle("HAMBURGO");
        domicilio_mexico.setNumeroExterior("260");
        domicilio_mexico.setColonia("JUAREZ");
        domicilio_mexico.setLocalidad("MEXICO");
        domicilio_mexico.setMunicipio("CUAUHTEMOC");
        domicilio_mexico.setEstado("DISTRITO FEDERAL");
        domicilio_mexico.setPais("MEXICO");
        domicilio_mexico.setCodigoPostal("06600");
        domicilio.setDomicilioFiscalMexicano(domicilio_mexico);
        return domicilio;
    }
}
