/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generarFactura;

import com.escom.info.compra.Compra;
import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.FacturaGeneral;
import com.escom.info.compra.Pedido;
import com.escom.libreria.mysuit.TAllowanceChargeType;
import com.escom.libreria.mysuit.TAsignacion;
import com.escom.libreria.mysuit.TComprobanteEx;
import com.escom.libreria.mysuit.TComprobanteEx.DatosDeNegocio;
import com.escom.libreria.mysuit.TComprobanteEx.TerminosDePago;
import com.escom.libreria.mysuit.TConceptoEx;
import com.escom.libreria.mysuit.TCountryCode;
import com.escom.libreria.mysuit.TCurrencyCode;
import com.escom.libreria.mysuit.TDescuentosYRecargos;
import com.escom.libreria.mysuit.TDictionaries;
import com.escom.libreria.mysuit.TDictionary;
import com.escom.libreria.mysuit.TDictionary.Entry;
import com.escom.libreria.mysuit.TDiscountOrRecharge;
import com.escom.libreria.mysuit.TDomicilioMexicano;
import com.escom.libreria.mysuit.TFactDocMX;
import com.escom.libreria.mysuit.TFactDocMX.Conceptos;
import com.escom.libreria.mysuit.TFactDocMX.Emisor;
import com.escom.libreria.mysuit.TFactDocMX.Identificacion;
import com.escom.libreria.mysuit.TFactDocMX.Identificacion.AsignacionSolicitada;
import com.escom.libreria.mysuit.TFactDocMX.Receptor;
import com.escom.libreria.mysuit.TFactDocMX.Receptor.Domicilio;
import com.escom.libreria.mysuit.TFactDocMX.Receptor.DomicilioDeRecepcion;
import com.escom.libreria.mysuit.TFactDocMX.Totales;
import com.escom.libreria.mysuit.TNonNegativeAmount;
import com.escom.libreria.mysuit.TResumenDeDescuentosYRecargos;
import com.escom.libreria.mysuit.TResumenDeImpuestos;
import com.escom.libreria.mysuit.TSenderCountryCode;
import com.escom.libreria.mysuit.TSettlementType;
import com.escom.libreria.mysuit.TSpecialServicesType;
import com.escom.libreria.mysuit.TTipoDeDocumento;
import escom.libreria.info.articulo.jpa.DescuentoArticulo;
import escom.libreria.info.articulo.jpa.Impuesto;
import escom.libreria.info.conversioNumeroToLetra.ConversionImp;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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


  /*  private  TDomicilioMexicano getDireccionFiscal() {

        TDomicilioMexicano domicilioEmision=new TDomicilioMexicano();
        domicilioEmision=getDireccionFiscal()
        domicilioEmision.setCalle(difacturacion.getCalle());
        domicilioEmision.setCodigoPostal(difacturacion.getCp()+"");
        domicilioEmision.setColonia(difacturacion.getColonia());
        domicilioEmision.setEstado(difacturacion.getEstado().getNombre());
        domicilioEmision.setNumeroExterior(difacturacion.getNoExterior());
        domicilioEmision.setNumeroInterior( difacturacion.getNoInterior());
        domicilioEmision.setMunicipio(difacturacion.getDelMunicipio());

        return domicilioEmision;

    }
*/

    public Receptor getReceptor(Difacturacion difacturacion) {
        Receptor receptor=new Receptor();
        receptor.setCdgPaisReceptor(TCountryCode.MX);
        receptor.setRFCReceptor(difacturacion.getRfc());
        receptor.setNombreReceptor(difacturacion.getRazonSocial());
        receptor.setDomicilio(getRecepcion(difacturacion));

        return receptor;
    }

    public Emisor getDireccionEmisorAndFiscal(Difacturacion difacturacion){
             Emisor emisor=getDireccionEmisor();

            
             return emisor;
    }


    public  Totales getTotales(Compra compra) {
        Totales total=new Totales();
        total.setMoneda(TCurrencyCode.MXN);
        total.setTipoDeCambioVenta(new BigDecimal("1.0000"));
        total.setSubTotal(getImporte(compra.getPagoTotal()));
        total.setSubTotalBruto(getImporte(compra.getPagoTotal()));
        String decimal=compra.getPagoTotal().toString();
        ConversionImp conversionImp=new  ConversionImp();
        String letra=conversionImp.convertirNumeroToLetra(decimal);
        total.setTotalEnLetra(letra);
        total.setTotal(getImporte(compra.getPagoTotal()));
        total.setResumenDeDescuentosYRecargos(getResumenDeImpuestos(compra));
        total.setDescuentosYRecargos(getDescuentoYrecargos(compra));
        total.setResumenDeImpuestos(getResumenDeImpuestos());
        total.setFormaDePago("PARCIALIDAD 1 DE 1");
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

        Entry entry4=new Entry();
        entry4.setK("bcc");
        entry4.setV("acisneros@uhthoff.com.mx");

        e.getEntry().add(entry);
        e.getEntry().add(entry2);
        e.getEntry().add(entry4);
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
        
        return identificacion;

    }

     private  Emisor getDireccionEmisor(){  /*Falta adjuntar la direccion del cliente*/
        Emisor emisor=new TFactDocMX.Emisor();
        emisor.setDomicilioFiscal(getDireccionFiscal());
        emisor.setDomicilioDeEmision(getDireccionEmision());
        return emisor;
      }


     public TDomicilioMexicano getDireccionFiscal(){
          TDomicilioMexicano domicilio=new TDomicilioMexicano();
      domicilio.setCalle("AV. INSURGENTES SUR");
      domicilio.setNumeroExterior("881");
      //domicilio.setLocalidad("MEXICO");
      domicilio.setColonia("NAPOLES");
      domicilio.setMunicipio("BENITO JUAREZ");
      domicilio.setCodigoPostal("03810");
      domicilio.setEstado("DISTRITO FEDERAL");
      domicilio.setPais("Mexico");
      domicilio.setNumeroExterior("881");
      return domicilio;
     }
     public TDomicilioMexicano getDireccionEmision(){
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
      return domicilio;
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
           miconepto.setConceptoEx(getConceptoEX(pedido));
            miconepto.setCantidad(new BigDecimal(pedido.getNoArticuloCategoria()));
            miconepto.setUnidadDeMedida(pedido.getArticulo().getUnidad());
            miconepto.setImporte(getImporte(pedido.getPrecioTotal()));
            miconepto.setCodigo(pedido.getArticulo().getCodigo());
            miconepto.setValorUnitario(getImporte(pedido.getPrecioNeto()));
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

    public Domicilio getRecepcion(Difacturacion d) {
        Domicilio domicilio=new Domicilio();
        TDomicilioMexicano domicilio_mexico=new TDomicilioMexicano();
        domicilio_mexico.setCalle(d.getCalle());
        domicilio_mexico.setNumeroExterior(d.getNoExterior());
        domicilio_mexico.setColonia(d.getColonia());
        domicilio_mexico.setLocalidad("MEXICO");
        domicilio_mexico.setMunicipio(d.getDelMunicipio());
        domicilio_mexico.setEstado(d.getEstado().getNombre());
        domicilio_mexico.setPais("MEXICO");
        domicilio_mexico.setCodigoPostal("0"+d.getCp());
        domicilio.setDomicilioFiscalMexicano(domicilio_mexico);
        return domicilio;
    }

    private TConceptoEx getConceptoEX(Pedido pedido) {
        TConceptoEx  ex=new TConceptoEx();
        ex.setImporteLista(getImporte(pedido.getPrecioTotal()));
        ex.setPrecioLista(getImporte(pedido.getPrecioTotal()));
        ex.setDescuentosYRecargos(getDescuentoYrecargos(pedido));
        ex.setImporteTotal(getImporte(pedido.getPrecioTotal()));
        return ex;
    }

    private TDescuentosYRecargos getDescuentoYrecargos(Pedido pedido) {

        TDescuentosYRecargos t=new TDescuentosYRecargos();
        List<TDiscountOrRecharge> lista = t.getDescuentoORecargo();
        TDiscountOrRecharge descuento=new TDiscountOrRecharge();

     //DescuentoArticulo descuento_articulo=pedido.getArticulo().getDescuentoArticulo();

       descuento.setOperacion(TAllowanceChargeType.DESCUENTO);
       descuento.setImputacion(TSettlementType.FUERA_DE_FACTURA);
       descuento.setServicio(TSpecialServicesType.DESCUENTO);
       descuento.setDescripcion(TSpecialServicesType.DESCUENTO.value());
       descuento.setBase(getImporte(pedido.getPrecioTotal()));//base variar
       descuento.setMonto((getImporte(BigDecimal.ZERO)));
       descuento.setTasa(BigDecimal.ZERO);
       lista.add(descuento);
       return t;



    }

    private TResumenDeImpuestos getResumenDeImpuestos() {

        TResumenDeImpuestos resumen=new TResumenDeImpuestos();
        resumen.setTotalIEPSTrasladado(getImporte(BigDecimal.ZERO));
        resumen.setTotalISRRetenido(getImporte(BigDecimal.ZERO));
        resumen.setTotalIVARetenido(getImporte(BigDecimal.ZERO));
        resumen.setTotalIVATrasladado(getImporte(BigDecimal.ZERO));
        resumen.setTotalRetencionesFederales(getImporte(BigDecimal.ZERO));
        resumen.setTotalRetencionesLocales(getImporte(BigDecimal.ZERO));
        resumen.setTotalTrasladosFederales(getImporte(BigDecimal.ZERO));
        resumen.setTotalTrasladosLocales(getImporte(BigDecimal.ZERO));
        return resumen;

    }

    private TResumenDeDescuentosYRecargos getResumenDeImpuestos(Compra compra) {
        TResumenDeDescuentosYRecargos d=new TResumenDeDescuentosYRecargos();
        d.setTotalDescuentos(getImporte(compra.getDescuento()));
        d.setTotalRecargos(getImporte(compra.getDescuento()));
        return d;

    }

     public TComprobanteEx getComprobanteEx() {

        TComprobanteEx comprobanteEX=new TComprobanteEx();
        comprobanteEX.setDatosDeNegocio(getDatosDeNegocio());
        comprobanteEX.setTerminosDePago(getTerminosPago());
        //comprobanteEX.s

        return comprobanteEX;

    }

    private DatosDeNegocio getDatosDeNegocio() {
        DatosDeNegocio d=new DatosDeNegocio();
        d.setSucursal("FISCALDOM");
        return d;

    }

    private TerminosDePago getTerminosPago() {
        TerminosDePago p=new TerminosDePago();
        p.setDiasDePago(0);
        XMLGregorianCalendar date=null;
         GregorianCalendar c = new GregorianCalendar();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         String lexical=formatter.format(c.getTime());
        try {
            date = DatatypeFactory.newInstance().newXMLGregorianCalendar(lexical);
        } catch (DatatypeConfigurationException ex) {
           // Logger.getLogger(GeneraraFacade.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio un error en generar facturar");
        }

        p.setFechaDePago(date);
        p.setMetodoDePago("efectivo, deposito, transferencia, cheque");
        return p;
    }

    private TDescuentosYRecargos getDescuentoYrecargos(Compra compra) {
        TDescuentosYRecargos t=new TDescuentosYRecargos();
        List<TDiscountOrRecharge> lista = t.getDescuentoORecargo();
        TDiscountOrRecharge descuento=new TDiscountOrRecharge();

     //DescuentoArticulo descuento_articulo=pedido.getArticulo().getDescuentoArticulo();

       descuento.setOperacion(TAllowanceChargeType.DESCUENTO);
       descuento.setImputacion(TSettlementType.FUERA_DE_FACTURA);
       descuento.setServicio(TSpecialServicesType.DESCUENTO);
       descuento.setDescripcion(TSpecialServicesType.DESCUENTO.value());
       descuento.setBase(getImporte(compra.getPagoTotal()));//base variar
       descuento.setMonto((getImporte(BigDecimal.ZERO)));
       descuento.setTasa(BigDecimal.ZERO);
       lista.add(descuento);
       return t;
    }
}
