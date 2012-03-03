/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generarFactura;


import escom.libreria.facturacion.proveedor.mysuitemex.TAllowanceChargeType;
import escom.libreria.facturacion.proveedor.mysuitemex.TAsignacion;
import escom.libreria.facturacion.proveedor.mysuitemex.TComprobanteEx;
import escom.libreria.facturacion.proveedor.mysuitemex.TComprobanteEx.DatosDeNegocio;
import escom.libreria.facturacion.proveedor.mysuitemex.TComprobanteEx.ImportesDesglosados;
import escom.libreria.facturacion.proveedor.mysuitemex.TComprobanteEx.TerminosDePago;
import escom.libreria.facturacion.proveedor.mysuitemex.TConceptoEx;
import escom.libreria.facturacion.proveedor.mysuitemex.TCountryCode;
import escom.libreria.facturacion.proveedor.mysuitemex.TCurrencyCode;
import escom.libreria.facturacion.proveedor.mysuitemex.TDescuentosYRecargos;
import escom.libreria.facturacion.proveedor.mysuitemex.TDictionaries;
import escom.libreria.facturacion.proveedor.mysuitemex.TDictionary;
import escom.libreria.facturacion.proveedor.mysuitemex.TDictionary.Entry;
import escom.libreria.facturacion.proveedor.mysuitemex.TDiscountOrRecharge;
import escom.libreria.facturacion.proveedor.mysuitemex.TDomicilioMexicano;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Conceptos;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Conceptos.Concepto;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Emisor;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Identificacion;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Identificacion.AsignacionSolicitada;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Receptor;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Receptor.Domicilio;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Totales;
import escom.libreria.facturacion.proveedor.mysuitemex.TImportesDesglosados;
import escom.libreria.facturacion.proveedor.mysuitemex.TImpuestos;
import escom.libreria.facturacion.proveedor.mysuitemex.TNonNegativeAmount;
import escom.libreria.facturacion.proveedor.mysuitemex.TResumenDeDescuentosYRecargos;
import escom.libreria.facturacion.proveedor.mysuitemex.TResumenDeImpuestos;
import escom.libreria.facturacion.proveedor.mysuitemex.TSenderCountryCode;
import escom.libreria.facturacion.proveedor.mysuitemex.TSettlementType;
import escom.libreria.facturacion.proveedor.mysuitemex.TSpecialServicesType;
import escom.libreria.facturacion.proveedor.mysuitemex.TTax;
import escom.libreria.facturacion.proveedor.mysuitemex.TTaxContext;
import escom.libreria.facturacion.proveedor.mysuitemex.TTaxOperation;
import escom.libreria.facturacion.proveedor.mysuitemex.TTipoDeDocumento;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Difacturacion;
import escom.libreria.info.compras.Estado;
import escom.libreria.info.compras.FacturaGeneral;
import escom.libreria.info.compras.Pedido;

import escom.libreria.info.conversioNumeroToLetra.ConversionImp;
import escom.libreria.info.descuentos.Descuento;
import escom.libreria.info.descuentos.DescuentoArticulo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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



    public Receptor getReceptor(Difacturacion difacturacion) {
        Receptor receptor=new Receptor();
        receptor.setCdgPaisReceptor(TCountryCode.MX);
        receptor.setRFCReceptor(difacturacion.getRfc());
        receptor.setNombreReceptor(difacturacion.getRazonSocial());
        receptor.setDomicilio(getRecepcion(difacturacion));

        return receptor;
    }

    public Emisor getDireccionEmisorAndFiscal(){
             Emisor emisor=getDireccionEmisor();

            
             return emisor;
    }

    /*AQUI ESTA EL NODO CENTRAL*/
   public  Totales getTotales(Compra compra,List<Pedido> pedidos,Conceptos conceptos) {
        BigDecimal bruto=BigDecimal.ZERO;
        BigDecimal  subtotal=BigDecimal.ZERO;
        BigDecimal totalDescuento=BigDecimal.ZERO;
        BigDecimal totalImpuesto=BigDecimal.ZERO;
        Totales total=new Totales();

        ConversionImp conversionImp=new  ConversionImp();
        total.setMoneda(TCurrencyCode.MXN);
        total.setTipoDeCambioVenta(new BigDecimal("1.0000"));
       
       
        for(Concepto c:conceptos.getConcepto()){
            bruto=bruto.add(c.getConceptoEx().getImporteLista().getValue());
            subtotal=subtotal.add(c.getValorUnitario().getValue());
            List<TDiscountOrRecharge> descuetntoArticulo = c.getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo();
            TImpuestos impuestoArticulo = c.getConceptoEx().getImpuestos();
            totalDescuento=totalDescuento.add(descuetntoArticulo.get(0).getMonto().getValue());
            totalImpuesto=totalImpuesto.add(impuestoArticulo.getImpuesto().get(0).getMonto().getValue());

        }
        total.setSubTotal(getImporte(subtotal));
        total.setSubTotalBruto(getImporte(bruto));
        String decimal=compra.getPagoTotal().toString();
        String letra=conversionImp.convertirNumeroToLetra(decimal);
        total.setTotalEnLetra(letra);//TOTAL EN LETRA
        total.setTotal(getImporte(compra.getPagoTotal()));
        total.setResumenDeDescuentosYRecargos(getResumenDeDescuentosYCargos(totalDescuento));//ES EL TOTLA DE LOS DESCUENTOS
        total.setDescuentosYRecargos(getDescuentoYrecargosConceptos(compra)); //EL DETALLE DE LOS DESCUENTOS
        total.setResumenDeImpuestos(getResumenDeImpuestos(totalImpuesto));
        total.setImpuestos(getTimpuestoPedidos(conceptos));
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
        entry4.setV("jcmc09@gmail.com.mx");

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
     /*DATOS CONSTANTES  EN LA FACTURA INFORMACION PROPIA DEL TRIBUNAL*/
    public  Identificacion getIdentificacion() {
        Identificacion identificacion=new Identificacion();
        identificacion.setCdgPaisEmisor(TSenderCountryCode.MX);
        identificacion.setTipoDeComprobante(TTipoDeDocumento.FACTURA);
        identificacion.setRFCEmisor("TFJ360831MTA");
        identificacion.setRazonSocialEmisor("TRIBUNAL FEDERAL DE JUSTICIA FISCAL Y ADMINISTRATIVA");
        identificacion.setUsuario("INSURGENTES");
        identificacion.setNumeroInterno("FACTURA ELECTRONICA LJV FLJ-000001");
        
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
            TDescuentosYRecargos descuentoORecargo = miconepto.getConceptoEx().getDescuentosYRecargos();
            TDiscountOrRecharge descuento = descuentoORecargo.getDescuentoORecargo().get(0);

            miconepto.setCantidad(new BigDecimal(pedido.getNoArticuloCategoria()));
            miconepto.setUnidadDeMedida(pedido.getArticulo().getUnidad());
            miconepto.setImporte(getImporte(miconepto.getConceptoEx().getPrecioLista().getValue().subtract(descuento.getMonto().getValue()))); //valor unitario = importe
            miconepto.setValorUnitario(getImporte(miconepto.getImporte().getValue()));//valor unitario = importe
            miconepto.setCodigo(pedido.getArticulo().getCodigo());
            miconepto.setDescripcion(removeAcentos(pedido.getArticulo().getCodigo()+"-"+pedido.getArticulo().getTitulo()));
            miconepto.getConceptoEx().setImpuestos(getTimpuesto(miconepto.getValorUnitario().getValue(),pedido.getImpuesto()));

            listconcepto.add(miconepto);
         }
        return crear_concepto;
    }

     public  String removeAcentos(String input) {
    // Cadena de caracteres original a sustituir.
    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ&";
    // Cadena de caracteres ASCII que reemplazarán los originales.
    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCy";
    String output = input;
    for (int i=0; i<original.length(); i++) {
        // Reemplazamos los caracteres especiales.
        output = output.replace(original.charAt(i), ascii.charAt(i));
    }//for i
    return output;
}//remove1
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
        ex.setImporteLista(getImporte(pedido.getPrecioNeto().add(pedido.getGastosEnvio())));//debe ser iguales
        ex.setPrecioLista(getImporte(pedido.getPrecioNeto().add(pedido.getGastosEnvio())));//debe ser iguales
        ex.setDescuentosYRecargos(getDescuentoYrecargos(pedido));//DESCUENTOS
        ex.setImporteTotal(getImporte(pedido.getPrecioTotal()));//ESTA BIEN HASTA AKI
       // ex.setImpuestos(getTimpuesto(pedido));




        
        return ex;
    }
/*ya esta como me explicaron*/
    private TDescuentosYRecargos getDescuentoYrecargos(Pedido pedido) {

        TDescuentosYRecargos t=new TDescuentosYRecargos();
        List<TDiscountOrRecharge> lista = t.getDescuentoORecargo();
       TDiscountOrRecharge descuento=new TDiscountOrRecharge();
       descuento.setOperacion(TAllowanceChargeType.DESCUENTO);
       descuento.setImputacion(TSettlementType.FUERA_DE_FACTURA);
       descuento.setServicio(TSpecialServicesType.DESCUENTO);
       descuento.setDescripcion(TSpecialServicesType.DESCUENTO.value());
       descuento.setBase(getImporte(pedido.getPrecioNeto().add(pedido.getGastosEnvio())));//ESTA BIEN LA BASE
       descuento.setMonto(getImporte(getCalcularImpuesto(pedido.getDescuento(), pedido.getPrecioNeto().add(pedido.getGastosEnvio()))));
       descuento.setTasa(pedido.getDescuento());
       lista.add(descuento);
       return t;



    }


    /* RESUMEN DE IMPUESTOS AY QUE METERLE  MANO AQUI*/
    private TResumenDeImpuestos getResumenDeImpuestos(BigDecimal impuestos) {
         BigDecimal sumar=BigDecimal.ZERO;
        TResumenDeImpuestos resumen=new TResumenDeImpuestos();
        

        resumen.setTotalIVATrasladado(getImporte(impuestos));
        resumen.setTotalTrasladosFederales(getImporte(impuestos));

        resumen.setTotalIEPSTrasladado(getImporte(BigDecimal.ZERO));
        resumen.setTotalISRRetenido(getImporte(BigDecimal.ZERO));
        resumen.setTotalIVARetenido(getImporte(BigDecimal.ZERO));
        
        resumen.setTotalRetencionesFederales(getImporte(BigDecimal.ZERO));
        resumen.setTotalRetencionesLocales(getImporte(BigDecimal.ZERO));
        
        resumen.setTotalTrasladosLocales(getImporte(BigDecimal.ZERO));
        return resumen;

    }

    /* RESUMEN DE DESCUENTOS Y AY QUE METELER MANO*/
    private TResumenDeDescuentosYRecargos getResumenDeDescuentosYCargos(BigDecimal descuentoTotal) {
        TResumenDeDescuentosYRecargos d=new TResumenDeDescuentosYRecargos();

        d.setTotalDescuentos(getImporte(descuentoTotal));
        d.setTotalRecargos(getImporte(BigDecimal.ZERO));  //resuemen descuento recargos


        return d;

    }

     public TComprobanteEx getComprobanteEx() {

        TComprobanteEx comprobanteEX=new TComprobanteEx();
        comprobanteEX.setDatosDeNegocio(getDatosDeNegocio());
        comprobanteEX.setTerminosDePago(getTerminosPago());

      // comprobanteEX.setImportesDesglosados(getImporteDesglosados());//IMPORTES DEL DESGLOSE
        //comprobanteEX.setDatosDeNegocio(getDatosDeNegocio());

        return comprobanteEX;

    }

    private DatosDeNegocio getDatosDeNegocio() {
        DatosDeNegocio d=new DatosDeNegocio();
        d.setSucursal("LJV");
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
/** ESTE  MEDODO TIENE QUE SACAR EL DESCUENTO A TODO LOS ARITCULOS CON DESCUENTO **/
    private TDescuentosYRecargos getDescuentoYrecargos(List<Pedido> pedido) {
        TDescuentosYRecargos t=new TDescuentosYRecargos();

        List<TDiscountOrRecharge> lista = t.getDescuentoORecargo();

        for(Pedido p:pedido)
        {
            TDiscountOrRecharge descuento=new TDiscountOrRecharge();
            descuento.setOperacion(TAllowanceChargeType.DESCUENTO);
            descuento.setImputacion(TSettlementType.FUERA_DE_FACTURA);
            descuento.setServicio(TSpecialServicesType.DESCUENTO);
            descuento.setDescripcion("DESCUENTO");
            descuento.setBase(getImporte(p.getPrecioNeto().add(p.getGastosEnvio())));//base variar
            descuento.setMonto(getImporte(getCalcularImpuesto(p.getDescuento(),p.getPrecioNeto().add(p.getGastosEnvio()))));
            descuento.setTasa(p.getDescuento());
            lista.add(descuento);
        }
       return t;
    }

     private TImpuestos getTimpuesto(BigDecimal base,BigDecimal tasa) {
        TImpuestos t=new TImpuestos();
        //for(Pedido p:pedidos){

            TTax tax=new TTax();
            
            tax.setCodigo("IVA");
            tax.setContexto(TTaxContext.FEDERAL);
            tax.setOperacion(TTaxOperation.TRASLADO);
            /*ESTO ESTA BIEN LO DE ARRIBA*/
            tax.setBase(getImporte(base));
            tax.setTasa(tasa);

            BigDecimal calculo=getCalcularImpuesto(tasa,base);
            tax.setMonto(getImporte(calculo));
            t.getImpuesto().add(tax);
       // }
        return t;

    }

     private TImpuestos getTimpuestoPedidos(Conceptos conceptos) {
        TImpuestos t=new TImpuestos();
        
        
        for(Concepto p:conceptos.getConcepto()){
            TImpuestos impuestos = p.getConceptoEx().getImpuestos();
            TTax tax=impuestos.getImpuesto().get(0);

            /*TTax tax=new TTax();
            
            tax.setCodigo("IVA");
            tax.setContexto(TTaxContext.FEDERAL);
            tax.setOperacion(TTaxOperation.TRASLADO);
           
            tax.setBase(getImporte());
            tax.setTasa(p.getImpuesto());

            BigDecimal calculo=getCalcularImpuesto(p.getImpuesto(),p.getPrecioNeto());
            tax.setMonto(getImporte(calculo));*/
            t.getImpuesto().add(tax);
       }
        return t;

    }


     /*CALCULAMOS IMPUESTO*/
     private BigDecimal getCalcularImpuesto(BigDecimal impuesto,BigDecimal monto){
          BigDecimal resultado=BigDecimal.ZERO;
          resultado=impuesto.multiply(monto);
          resultado=resultado.divide(new BigDecimal(100));
          resultado=resultado.setScale(2,BigDecimal.ROUND_HALF_UP);
         return resultado;
     }

    /*private TDescuentosYRecargos getDescuentoYrecargosConceptos(Conceptos conceptos) {
          TDescuentosYRecargos descuento=new TDescuentosYRecargos();
          BigDecimal cero=new BigDecimal("0.00");
          List<TDiscountOrRecharge> decuentos = descuento.getDescuentoORecargo();
           TDiscountOrRecharge desc;
           for(Concepto p:conceptos.getConcepto()){
            List<TDiscountOrRecharge> decuentoArticulo = p.getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo();
            desc=decuentoArticulo.get(0);
            if(!desc.getTasa().equals(cero))
             decuentos.add(desc);


           }*/

private TDescuentosYRecargos getDescuentoYrecargosConceptos(Compra compra) {
          TDescuentosYRecargos descuento=new TDescuentosYRecargos();
        TDiscountOrRecharge descuentoTotal = new TDiscountOrRecharge();
        descuentoTotal.setOperacion(TAllowanceChargeType.DESCUENTO);
       descuentoTotal.setImputacion(TSettlementType.FUERA_DE_FACTURA);
       descuentoTotal.setServicio(TSpecialServicesType.DESCUENTO);
       descuentoTotal.setDescripcion(TSpecialServicesType.DESCUENTO.value());
        descuentoTotal.setBase(getImporte(compra.getPagoTotal()));
        descuentoTotal.setTasa(compra.getDescuento());
        descuentoTotal.setMonto(getImporte(getCalcularImpuesto(compra.getDescuento(), compra.getPagoTotal())));

            descuento.getDescuentoORecargo().add(descuentoTotal);

          return descuento;
    }

    public Difacturacion getDirrecionDefault() {
         Difacturacion difacturacion=new Difacturacion();
         Cliente cliente=new Cliente();
         Estado estado=new Estado();
         estado.setNombre("DISTRITO FEDERAL");
         cliente.setNombre("UHTHOFF GOMEZ VEGA Y UHTHOFF SC");

         difacturacion.setCalle("HAMBURGO");
         difacturacion.setColonia("JUAREZ");
         difacturacion.setCp(06600);
         difacturacion.setDelMunicipio("CUAUHTEMOC");
         difacturacion.setRfc("UGV820101TI0");
         difacturacion.setEstado(estado);
         difacturacion.setCliente(cliente);
         difacturacion.setRazonSocial("UHTHOFF GOMEZ VEGA Y UHTHOFF SC");

         return difacturacion;
    }



    
    /*private TImpuestos getTimpuesto(List<Pedido> pedidos) {
        TImpuestos t=new TImpuestos();
        for(Pedido p:pedidos){

            TTax tax=new TTax();
            tax.setBase(getImporte(p.getPrecioNeto()));
            tax.setCodigo(p.getArticulo().getCodigo());
            tax.setContexto(TTaxContext.LOCAL);
            tax.setOperacion(TTaxOperation.);
            tax.setTasa(BigDecimal.ZERO);
            tax.setMonto(getImporte(p.getImpuesto()));
            t.getImpuesto().add(tax);
        }
        return t;

    }*/

   /* private ImportesDesglosados getImporteDesglosados(List<Pedido> pedido) {
        ImportesDesglosados importesDesglosados=new ImportesDesglosados();
        List<TImportesDesglosados> importes = importesDesglosados.getImportes();
       TImportesDesglosados importeX=new TImportesDesglosados();
       importeX.


       return importesDesglosados;
       
    }*/
}
