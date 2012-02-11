/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generadorCFD.unmarshalCDI;

import escom.libreria.info.cdiv3.Comprobante;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.FacturaGeneral;
import escom.libreria.info.compras.ejb.FacturaGeneralFacade;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import javax.ejb.EJB;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;


/**
 *
 * @author xxx
 */

public class UnmarshalCDI {
    private static  Logger logger = Logger.getLogger(UnmarshalCDI.class);
    private Unmarshaller unmarshaller;
   

    private Comprobante cargaArchivoComprobante(String rutaCompletaXML) throws FileNotFoundException {

         logger.info("RUTA DEL CFDI COMPLEOT"+rutaCompletaXML);
            Class[] classes = new Class[10];
            classes[0] = escom.libreria.info.cdiv3.Comprobante.class;
            classes[1] = escom.libreria.info.cdiv3.Comprobante.Conceptos.class;
            classes[2] = escom.libreria.info.cdiv3.Comprobante.Emisor.class;
            classes[3] = escom.libreria.info.cdiv3.Comprobante.Impuestos.class;
            classes[4] = escom.libreria.info.cdiv3.Comprobante.Receptor.class;
            classes[5] = escom.libreria.info.cdiv3.TUbicacion.class;
            classes[6] = escom.libreria.info.cdiv3.TUbicacionFiscal.class;
            classes[7] = escom.libreria.info.cdiv3.Comprobante.Conceptos.Concepto.class;
            classes[8] = escom.libreria.info.cdiv3.Comprobante.Impuestos.Traslados.class;
            classes[9] = escom.libreria.info.cdiv3.Comprobante.Impuestos.Traslados.Traslado.class;

        FileInputStream fileInputStream = null;
        JAXBElement o = null;
        Comprobante comprobt3 = null;
        try {
            fileInputStream = new FileInputStream(rutaCompletaXML);
//            o = (JAXBElement) unmarshaller.unmarshal(fileInputStream);
                JAXBContext jaxbContext = JAXBContext.newInstance(classes);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                unmarshaller = jaxbContext.createUnmarshaller();
                comprobt3 = (Comprobante) unmarshaller.unmarshal(fileInputStream);
        } catch (JAXBException ex) {
           logger.error("error de cargaArchivoFiscal JAXB -->" + ex);
        } catch (FileNotFoundException ex) {
           logger.error("error de cargaArchivoFiscal I/O -->" + ex);
        }
        return comprobt3;
    }

    public FacturaGeneral prepareCreateCFI_To_Object(Compra compra,String RUTA_XML_CFDI){
        FacturaGeneral facturaGeneral=new FacturaGeneral();
        try {
            Comprobante comprobante = cargaArchivoComprobante(RUTA_XML_CFDI);

            if(comprobante!=null)
            {

                logger.info("SI ESTA LLENO");


                facturaGeneral.setCalle(comprobante.getReceptor().getDomicilio().getCalle());
                facturaGeneral.setCertEmi(comprobante.getCertificado());
                facturaGeneral.setColonia(comprobante.getReceptor().getDomicilio().getColonia());
                facturaGeneral.setCompra(compra);
                facturaGeneral.setComprobante(comprobante.getVersion());
                facturaGeneral.setCp(Integer.parseInt(comprobante.getReceptor().getDomicilio().getCodigoPostal()));
                facturaGeneral.setNoExterior(comprobante.getReceptor().getDomicilio().getNoExterior());
                facturaGeneral.setRazonSocial(comprobante.getReceptor().getNombre());
                facturaGeneral.setPais(comprobante.getReceptor().getDomicilio().getPais());
                facturaGeneral.setFechaCert(comprobante.getFecha().toGregorianCalendar().getTime());
                facturaGeneral.setFechaEmi(new Date());
                facturaGeneral.setNoSerie(String.valueOf(comprobante.getSerie()));
                facturaGeneral.setNoFolio(Integer.parseInt(comprobante.getFolio()));
                facturaGeneral.setIdEdo(0);
                facturaGeneral.setRfc(comprobante.getReceptor().getRfc());
                facturaGeneral.setFolio(comprobante.getFolio());
               
                
        /*System.out.println("Certificado:"+comprobante.getCertificado());
        System.out.println("Folio:"+comprobante.getFolio());
        System.out.println("Serie:"+comprobante.getSerie());
        System.out.println("No.Certificado:"+comprobante.getNoCertificado());
        System.out.println("FECHA CERTIFICADO"+comprobante.getFecha().toGregorianCalendar().getTime());

        System.out.println("CALLE:"+comprobante.getReceptor().getDomicilio().getCalle());
        System.out.println("NO.INTERNO:"+comprobante.getReceptor().getDomicilio().getNoInterior());
        System.out.println("PAIS:"+comprobante.getReceptor().getDomicilio().getPais());
        System.out.println("MUNICIPIO:"+comprobante.getReceptor().getDomicilio().getMunicipio());
        System.out.println("NO.EXTERIOR:"+comprobante.getReceptor().getDomicilio().getNoExterior());
        System.out.println("COLONIA:"+comprobante.getReceptor().getDomicilio().getColonia());
        System.out.println("RFC:"+comprobante.getReceptor().getRfc());
         * 
         */
            }else{
                logger.error("error no encontro el cfdi");
            }
                 

        } catch (FileNotFoundException ex) {
           logger.error("ERRORO AL OBTENER COMPROBAMENTE ", ex);
        }
        return   facturaGeneral;
    }

}
