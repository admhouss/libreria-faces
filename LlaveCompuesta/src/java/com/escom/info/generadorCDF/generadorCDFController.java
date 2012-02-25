/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generadorCDF;

import com.escom.info.generadorCFD.unmarshalCDI.UnmarshalCDI;
import com.escom.info.generarFactura.GeneraraFacade;
import escom.libreria.correo.ProcesoJMail;
import escom.libreria.facturacion.proveedor.mysuitemex.FactDocsMX;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Conceptos;
import escom.libreria.facturacion.proveedor.mysuitemex.TFactDocMX.Emisor;
import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.compras.Compra;
import escom.libreria.info.compras.Difacturacion;
import escom.libreria.info.compras.FacturaGeneral;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.ejb.DifacturacionFacade;
import escom.libreria.info.compras.ejb.FacturaGeneralFacade;
import escom.libreria.info.compras.ejb.PedidoFacade;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import webservicesFacturacionMysuit.facturafromDenegateImp;

/**
 *
 * @author xxx
 */


@ManagedBean (name="generadorCDFController")
@SessionScoped

public class generadorCDFController implements Serializable
{
    //private static final int DEFAULT_BUFFER_SIZE =9024;
    @EJB private GeneraraFacade generaraFacade;
    @EJB private DifacturacionFacade direccionFacturacionFacade;
    @EJB private PedidoFacade pedidoFacade;
    @EJB private ProcesoJMail enviarCorreo;
    @EJB private FacturaGeneralFacade  facturaGeneralFacade;


    private static  Logger logger = Logger.getLogger(generadorCDFController.class);
    private String msgError;

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }


    


    private void generarFacturaDeCompra(Compra idCompra){
      logger.info("---COMENZANDO PROCESO DE CREACION CFD-------") ;
      Difacturacion direccionFactura=direccionFacturacionFacade.getDireccionFacturaClienteByID(idCompra.getIdCliente());
      logger.info("---OBTENIENDO DIRECCION DE FACTURACION DEL CLIENTE----");
      Emisor direccionEmisor = generaraFacade.getDireccionEmisorAndFiscal(direccionFactura);
    //  List<Pedido> pedidos=pedidoFacade.getAllpedidosByid(idCompra.getIdPedido());
     // Conceptos conceptos = generaraFacade.crearConceptos(pedidos);
      logger.info("COMENZANO PROCESO DE CREADO ARCHIVO CFD");
      crear_objeto_xml(direccionEmisor,idCompra,direccionFactura);
      
        //return "facturar";
    }


    public String crearFacturara(Compra c){

       // if(bandera)  {
                generarFacturaDeCompra(c);/*METODO QUE CREA CFD PARA EL SERVICIO MYSUIT BY YAMIL*/
                String nombreFacturaCFD=ConstantesFacturacion.FACTURA_NOMBRE+c.getIdPedido()+".xml";
                logger.info("EL CFD FUE  CREADO SATISFACTORIAMENTE:"+nombreFacturaCFD);
                logger.info("LE VOY A MANDAR A MYSUIT EL SIGUIENTE CFD:"+ConstantesFacturacion.RUTA_REPOSITORIO_CFD+nombreFacturaCFD);
                facturafromDenegateImp facturacion=new facturafromDenegateImp();
                try{
                         logger.info("LE VOY A MANDAR A MYSUIT EL SIGUIENTE CFD"+ConstantesFacturacion.RUTA_REPOSITORIO_CFD+nombreFacturaCFD);
                         msgError+= facturacion.generaFactura(ConstantesFacturacion.RUTA_REPOSITORIO_CFD, nombreFacturaCFD);
                        if(facturacion.getUbicacionCFDI()!=null)
                        {
                            guardarFactura(c, facturacion.getUbicacionCFDI()+".xml");
                         
                          logger.info("PROCESO INVERSO REALIZADO SATISFACTORAMENTE");
                    /*DESCOMENTAR ESTA LINEA SI ES IMPORTANTE ,LA COMENTE POR PROBAR**/
                            enviarFacturaToCliente(c.getIdCliente(),facturacion.getUbicacionCFDI());
                        }
                         else{
                           logger.info("LA UBICACION ES NULA"+msgError);
                           JsfUtil.addErrorMessage(msgError);
                         }
                } catch (Exception ex) {
                   logger.error("Error de comunicacion mysuit",ex);
                   // msgError+="Error de comunicacion servicio mysuit";
                    JsfUtil.addErrorMessage(msgError);
                }
         //   }

        return null;
    }


    public void guardarFactura(Compra c,String rutaCFDI){
        try{

                         UnmarshalCDI unmarshalCDI=new UnmarshalCDI();
                         FacturaGeneral factor = unmarshalCDI.prepareCreateCFI_To_Object(c, rutaCFDI);
                         facturaGeneralFacade.create(factor);
                         logger.info("FACTURA GUARDADA SATISFACTORIAMENTE");
        }catch(Exception e){
            logger.error("error al persisitir");
        }

    }
   /* public boolean generarXMLFactura(Difacturacion d,Compra c){
        Emisor direccionEmisor = generaraFacade.getDireccionEmisorAndFiscal(d);
        //c.setIdPedido(c.getIdPedido());
        List<Pedido> pedidos=pedidoFacade.getAllpedidosByid(c.getPedido());
        Conceptos concepto = generaraFacade.crearConceptos(pedidos);
       if(crear_objeto_xml(direccionEmisor,concepto,c,d))
        return true;


       return false;

    }*/


/*EL  NOMBRE DE LA FACTURA SE CONFORMA A PARTIR DE  FACTCFD0"+c.getPedido()+".xml";*/

    private boolean  crear_objeto_xml(Emisor e,Compra c,Difacturacion d) {
       List<Pedido> pedidos=pedidoFacade.getAllpedidosByid(c.getIdPedido());
       Conceptos conceptos = generaraFacade.crearConceptos(pedidos); //ESTA BIEN

       String nombreFacturaCFD=ConstantesFacturacion.FACTURA_NOMBRE+c.getIdPedido()+".xml";

        logger.info("NOMBRE DEL CFD QUE SE VA CREAR "+nombreFacturaCFD);
        try {
            boolean bandera=false;
            FactDocsMX factDocsMX = new FactDocsMX();
            TFactDocMX factura = new TFactDocMX();
            factura.setIdentificacion(generaraFacade.getIdentificacion()); //ESTA BIEN
            factura.setProcesamiento(generaraFacade.getProcesamiento()); // ESTA BIEN
            factura.setEmisor(e);  //ESTA BIEN
            factura.setReceptor(generaraFacade.getReceptor(d)); //ESTA BIEN
            factura.setConceptos(conceptos);
            factura.setVersion(new BigInteger("4")); //ESTA BIEN
            factura.setTotales(generaraFacade.getTotales(c,pedidos));
            factura.setComprobanteEx(generaraFacade.getComprobanteEx());

            factDocsMX.getFactDocMX().add(factura);

            
            bandera = crear_xml(factDocsMX,nombreFacturaCFD,c.getIdCliente());

            /*COMIENZA EL PROCESO DE FACTURACION*/
           
            

            return bandera;
        } catch (IOException ex) {
           logger.error("OCURRIO UN ERROR AL INTENTAR CREAR CFD",ex);
        }
        return false;
    }


    private  boolean crear_xml(FactDocsMX  factDocsMX,String nombreFactura,String correo) throws IOException{
                StringWriter sw = new StringWriter();
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
                marshaller.marshal(factDocsMX,sw );
                 /*sw.Buffer contiene el XML FORMATO CADENA*/
                 String archivo=sw.getBuffer().toString();
                 archivo=archivo.replace("<fx:FactDocMX>","");
                 archivo=archivo.replace("</fx:FactDocMX>","");
                 archivo=archivo.replace("<fx:FactDocsMX","<fx:FactDocMX ");
                 archivo=archivo.replace("</fx:FactDocsMX>","</fx:FactDocMX>");


                 escribirCFD(archivo, ConstantesFacturacion.RUTA_REPOSITORIO_CFD,nombreFactura);//ruta_cfd,nombre);
                 //ConstantesFacturacion.RUTA_REPOSITORIO_CFD+nombreFactura
      
            } catch (javax.xml.bind.JAXBException ex) {
    // XXXTODO Handle exception
                logger.error("Ocurrio un error al construir CFD", ex);
                return false;

                //java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
            }

         return true;

}


    private void enviarFacturaToCliente(String correo,String CFDI){
       try{
            String formatosEnviar[]={".xml",".pdf",".html"};
            List<String> facturaFormatos=new ArrayList<String>();
            List<String> clienteFactura=new ArrayList<String>();
            for(String formato:formatosEnviar)//TRAES LOS 3 DIFERENTES FORMATOS
            facturaFormatos.add((CFDI+formato));
            clienteFactura.add(correo);
            System.out.println("CFDI UBICACION:"+CFDI+formatosEnviar[0]);
            enviarCorreo.enviarLibroComprado("FACTURA GENERADA SATISFACOTIRAMENTE", facturaFormatos, "FACTURA GENERADA SATISFACTORIAMENTE", clienteFactura);
        }catch(Exception e){
            logger.error("ERROR AL ENVIAR FACTURAS", e);
        }
    }
    private void escribirCFD(String archivo,String ruta,String nombre)
    {
        BufferedWriter escribirarchivo = null;
        try {
            escribirarchivo = new BufferedWriter(new FileWriter(new File(ruta, nombre)));
            escribirarchivo.write(archivo);

        } catch (IOException ex) {
            ex.printStackTrace();
           // Logger.getLogger(generadorCDFController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                escribirarchivo.close();
            } catch (IOException ex) {
                ex.printStackTrace();
             //   Logger.getLogger(generadorCDFController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
