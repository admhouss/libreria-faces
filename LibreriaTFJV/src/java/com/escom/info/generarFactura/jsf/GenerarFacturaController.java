/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generarFactura.jsf;

import com.escom.info.compra.Compra;
import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.Pedido;
import com.escom.info.compra.ejb.UnmarshalPrueba;
import com.escom.info.compra.jsf.util.JsfUtil;
import com.escom.libreria.mysuit.FactDocsMX;
import com.escom.libreria.mysuit.TComprobanteEx;
import com.escom.libreria.mysuit.TComprobanteEx.DatosDeNegocio;
import com.escom.libreria.mysuit.TComprobanteEx.ImportesDesglosados;
import com.escom.libreria.mysuit.TComprobanteEx.TerminosDePago;
import com.escom.libreria.mysuit.TComprobanteEx.Transportistas.ConceptosDeCobro.Concepto;
import com.escom.libreria.mysuit.TFactDocMX;
import com.escom.libreria.mysuit.TFactDocMX.Conceptos;
import com.escom.libreria.mysuit.TFactDocMX.Emisor;
import com.escom.libreria.mysuit.TFactDocMX.Identificacion;
import com.escom.libreria.mysuit.TImportesDesglosados;
import com.sun.faces.facelets.util.FastWriter;
import escom.libreria.cdi.Comprobante;
import escom.libreria.info.subirArchivo.SubirFiles;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.Marshaller;
import webservicesFacturacionMysuit.facturafromDenegateImp;

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
     private static final int DEFAULT_BUFFER_SIZE =9024;

    @ManagedProperty("#{subirArchivo}")
    private SubirFiles subirFiles;

    public SubirFiles getSubirFiles() {
        return subirFiles;
    }

    public void setSubirFiles(SubirFiles subirFiles) {
        this.subirFiles = subirFiles;
    }


    public String ruta_cdi=  "/home/libreria/facturacion";//C:/Users/xxx/Desktop/facturasPrueba/";
    public String name_file;

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
        if(generarXMLFactura(clientef, comp)){
            JsfUtil.addSuccessMessage("CFD CREADO SATISFACTORIAMENTE");
        }else{
            JsfUtil.addErrorMessage("Error al crear CFE");
        }

        return "/compra/List";
    }

    public  void generarFactura_(Compra compra){
        //generaraFacade.
        Compra comp=compra;
        Difacturacion clientef;
        List<Difacturacion> direcion_facturacion_clientes=difacturacionFacade.getDireccionFacturaCliente(comp.getIdcliente());
        if(direcion_facturacion_clientes==null || direcion_facturacion_clientes.isEmpty()){
            JsfUtil.addSuccessMessage("Direccion de factura no encontrada");
        }
        clientef=direcion_facturacion_clientes.get(0);
        if(generarXMLFactura(clientef, comp)){
            JsfUtil.addSuccessMessage("CFD CREADO SATISFACTORIAMENTE");
            //facturar(comp);
        }else
        {
            JsfUtil.addErrorMessage("Error al crear CFE");
        }
    }


public void download(String path, String nameFile, String tipoArchivo) throws IOException {

      System.out.println("descargando el archivo"+path+nameFile);
        // Prepare.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        File file = new File(path,nameFile);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        //String url = "archivoPdf?path=" + path + "&fileName=" + nameFile + "&fileType=txt";
        try {
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

            // Init servlet response.
            response.reset();
            response.setContentType(tipoArchivo);

            response.setContentLength(Long.valueOf(file.length()).intValue());
            response.setHeader("Content-disposition", "attachment; filename=\"" + nameFile + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }
        facesContext.responseComplete();
    }
private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it. It may be useful to
                // know that this will generally only be thrown when the client aborted the download.
                e.printStackTrace();
            }
        }
    }
    public String facturar(Compra c){

        String super_cadena="";
        try {
            name_file = "FACTCFD0" + c.getId() ;
            facturafromDenegateImp faDenegateImp = new facturafromDenegateImp();
            String error= faDenegateImp.connectedMysuitFactor(ruta_cdi + name_file+".xml");
            if(faDenegateImp.isResult()){


                String documento=faDenegateImp.getRuta_final()+".xml";
                UnmarshalPrueba unma=new UnmarshalPrueba();
                Comprobante comprobante =unma.cargaArchivoComprobante(documento);

                
                super_cadena+=("Folio:"+comprobante.getFolio())+"\n";
                super_cadena+=("Serie:"+comprobante.getSerie())+"\n";
                super_cadena+=("No.Certificado:"+comprobante.getNoCertificado())+"\n";
                super_cadena+=("FECHA CERTIFICADO"+comprobante.getFecha())+"\n";

                super_cadena+=("CALLE:"+comprobante.getReceptor().getDomicilio().getCalle())+"\n";
                super_cadena+=("NO.INTERNO:"+comprobante.getEmisor().getDomicilioFiscal().getNoInterior())+"\n";
                super_cadena+=("PAIS:"+comprobante.getEmisor().getDomicilioFiscal().getPais())+"\n";
                super_cadena+=("MUNICIPIO:"+comprobante.getEmisor().getDomicilioFiscal().getMunicipio())+"\n";
                super_cadena+=("NO.EXTERIOR:"+comprobante.getEmisor().getDomicilioFiscal().getNoExterior())+"\n";
                super_cadena+=("COLONIA:"+comprobante.getEmisor().getDomicilioFiscal().getColonia())+"\n";
                super_cadena+=("RFC:"+comprobante.getEmisor().getRfc())+"\n";

                JsfUtil.addSuccessMessage("factura creada Satisfactoriamente");
                JsfUtil.addSuccessMessage(super_cadena);

                System.out.println(faDenegateImp.getDirectorio()+faDenegateImp.getName()+".pdf");

              download(faDenegateImp.getDirectorio(),faDenegateImp.getName()+".pdf", "application/pdf");

            }
            else
             JsfUtil.addSuccessMessage(error);

           
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.fillInStackTrace();
           JsfUtil.addErrorMessage("LO SENTIMOS HAY UN PROBLEMA DE COMUNICACION CON MYSUIT");
        }
        return "/compra/List";
    }

    private boolean generarXMLFactura(Difacturacion d,Compra c){
        Emisor direccionEmisor = generaraFacade.getDireccionEmisorAndFiscal(d);
        c.setIdPedido(c.getIdPedido());
        List<Pedido> pedidos=pedidoFacade.getAllpedidosByid(c.getIdPedido());
        Conceptos concepto = generaraFacade.crearConceptos(pedidos);
       if(crear_objeto_xml(direccionEmisor,concepto,c,d)){


          return true;

       }
      return false;

    }

    private boolean  crear_objeto_xml(Emisor e,Conceptos conceptos,Compra c,Difacturacion d){
        try {
            boolean bandera=false;
            FactDocsMX factDocsMX = new FactDocsMX();
            TFactDocMX factura = new TFactDocMX();
            factura.setIdentificacion(generaraFacade.getIdentificacion());
            factura.setProcesamiento(generaraFacade.getProcesamiento());
            factura.setEmisor(e);
            factura.setReceptor(generaraFacade.getReceptor(d));
            factura.setConceptos(conceptos);
            factura.setVersion(new BigInteger("4"));
            factura.setTotales(generaraFacade.getTotales(c));
            factura.setComprobanteEx(generaraFacade.getComprobanteEx());
            factDocsMX.getFactDocMX().add(factura);
            name_file="FACTCFD0"+c.getId()+".xml";
            bandera = crear_xml(factDocsMX);
            return bandera;
        } catch (IOException ex) {
            Logger.getLogger(GenerarFacturaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    private  boolean crear_xml(FactDocsMX  factDocsMX) throws IOException{

         try {
                javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance( factDocsMX.getClass().getPackage().getName());
                javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
               // marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
               // marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://www.fact.com.mx/schema/fx http://www.mysuitemex.com/fact/schema/fx_2010_c.xsd");
                MyNamespacePrefixMapper mapper = new MyNamespacePrefixMapper();
                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);
                //"com.sun.xml.bind.namespacePrefixMapper
                StringWriter sw = new StringWriter();
               // marshaller.marshal(factDocsMX,)
                 //       new File("C:/Users/xxx/Desktop/facturasPrueba/","facturaCFD0.xml"));


                 marshaller.marshal(factDocsMX,sw );
                 String archivo=sw.getBuffer().toString();
                 archivo=archivo.replace("<fx:FactDocMX>","");
                 archivo=archivo.replace("</fx:FactDocMX>","");
                 archivo=archivo.replace("<fx:FactDocsMX","<fx:FactDocMX ");
                 archivo=archivo.replace("</fx:FactDocsMX>","</fx:FactDocMX>");

                BufferedWriter escribirarchivo=new  BufferedWriter(new FileWriter(new File(ruta_cdi,name_file)));
                escribirarchivo.write(archivo);
                escribirarchivo.close();



    //marshaller.marshal(factDocsMX, System.out);
            } catch (javax.xml.bind.JAXBException ex) {
    // XXXTODO Handle exception
                ex.printStackTrace();
                System.out.println("Error al construir el XML"+ex);
                return false;
                
                //java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
            }

         return true;

}



}
