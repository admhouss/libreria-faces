/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicesFacturacionMysuit;

import com.escom.info.generadorCDF.ConstantesFacturacion;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;


import mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransactionResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mx.com.fact.www.schema.ws.FactWSFrontStub.FactResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.misc.BASE64Decoder;


/**
 *
 * @author xxx
 */
public class facturafromDenegateImp implements facturafromClient{


  //  public static String keyStore= "C:/mikeystore/" ;///home/libreria/mykeystore/";
    //public static String rutas_factura="C:/Users/xxx/Desktop/facturasPrueba/"; //"/home/libreria/facturacion/"; ///home/tribunal/Documentos/resultados/";
    private String serie,folio;
    private static  Logger logger = Logger.getLogger(facturafromDenegateImp.class);
    private String ubicacionCFDI=null;


    public facturafromDenegateImp() {
        ubicacionCFDI=null;
    }

    public String getUbicacionCFDI() {
        return ubicacionCFDI;
    }

    public void setUbicacionCFDI(String ubicacionCFDI) {
        this.ubicacionCFDI = ubicacionCFDI;
    }







     /*METODO QUE CONECTA CON EL SERVICIO MYSUIT*/
    @Override
    public mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransactionResponse connectedMysuitFactor(String xml_cfd) throws java.lang.Exception {

                    RequestTransactionResponse response  =null;
                   System.setProperty("javax.net.ssl.trustStore",ConstantesFacturacion.keyStore);
        	   System.setProperty("javax.net.ssl.keyStore", ConstantesFacturacion.keyStore);//keyStore+"keystore.jks");
        	   System.setProperty("javax.net.ssl.trustStorePassword", "mipassword");
        	   System.setProperty("javax.net.ssl.keyStorePassword", "mipassword");
                   System.setProperty("javax.net.debug", "ssl");


                  mx.com.fact.www.schema.ws.FactWSFrontStub stub =new mx.com.fact.www.schema.ws.FactWSFrontStub();//the default implementation should point to the right endpoint
                  mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction requestTransaction6=(mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction)getTestObject(mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction.class);

                  requestTransaction6.setRequestor("0bd637a1-7a6f-4248-842f-3adaf3fe145f");
                  requestTransaction6.setTransaction("CONVERT_NATIVE_XML");
                  requestTransaction6.setCountry("MX");
                  requestTransaction6.setEntity("TFJ360831MTA");
                  requestTransaction6.setUser("0bd637a1-7a6f-4248-842f-3adaf3fe145f");
                  requestTransaction6.setUserName("MX.TFJ360831MTA.TRIBUNALFISCAL");
                  requestTransaction6.setData1(procesarCFD(xml_cfd));
                  requestTransaction6.setData2("PDFHTML XML");
                  requestTransaction6.setData3("");
                  try{
                  response = stub.requestTransaction(requestTransaction6);
        }catch(Exception e){
         e.printStackTrace();
        }
                  return response;
                  

    }


  public String  generaFactura(String ruta,String XML_CFD)throws java.lang.Exception
  {
           String fxml,fhtml,fpdf,directorio;

          
                   RequestTransactionResponse response  =null;
                   System.setProperty("javax.net.ssl.trustStore", ConstantesFacturacion.keyStore);
        	   System.setProperty("javax.net.ssl.keyStore", ConstantesFacturacion.keyStore);
        	   System.setProperty("javax.net.ssl.trustStorePassword", "mipassword");
        	   System.setProperty("javax.net.ssl.keyStorePassword", "mipassword");
                   System.setProperty("javax.net.debug", "ssl");


                  mx.com.fact.www.schema.ws.FactWSFrontStub stub =new mx.com.fact.www.schema.ws.FactWSFrontStub();//the default implementation should point to the right endpoint
                  mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction requestTransaction6=(mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction)getTestObject(mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction.class);

                  requestTransaction6.setRequestor("0bd637a1-7a6f-4248-842f-3adaf3fe145f");
                  requestTransaction6.setTransaction("CONVERT_NATIVE_XML");
                  requestTransaction6.setCountry("MX");
                  requestTransaction6.setEntity("TFJ360831MTA");
                  requestTransaction6.setUser("0bd637a1-7a6f-4248-842f-3adaf3fe145f");
                  requestTransaction6.setUserName("MX.TFJ360831MTA.TRIBUNALFISCAL");
                  requestTransaction6.setData1(procesarCFD(ruta+XML_CFD));
                  requestTransaction6.setData2("PDFHTML XML");
                  requestTransaction6.setData3("");

                  response = stub.requestTransaction(requestTransaction6);
                  StringBuilder buffer=new StringBuilder();
                  buffer.append("proceso:").append(response.getRequestTransactionResult().getResponse().getProcessor()).append("\n").
                  append("codigo:").append(response.getRequestTransactionResult().getResponse().getCode()).append("\n").
                  append("data:").append(response.getRequestTransactionResult().getResponse().getData()).append("\n").
                  append("descripcion:").append(response.getRequestTransactionResult().getResponse().getDescription()).append("\n").
                  append("Resultado:").append(response.getRequestTransactionResult().getResponse().getResult());
         
        /*OBTENEMOS RESPUESTA POR PARTE DEL SERVICIO */
         boolean result = response.getRequestTransactionResult().getResponse().getResult();
       //TFJV las facturas se FORMAN CON TFJV
           if(result)
            {
                FactResponse transaccion = response.getRequestTransactionResult().getResponse();
                folio=transaccion.getIdentifier().getSerial();
                serie=transaccion.getIdentifier().getBatch().trim();
                directorio=crearDirectory(serie);
                fxml= response.getRequestTransactionResult().getResponseData().getResponseData1();
                fhtml = response.getRequestTransactionResult().getResponseData().getResponseData2();
                fpdf = response.getRequestTransactionResult().getResponseData().getResponseData3();
                /*RUTA COMPLETA: directorio,"TFJV"+serie+folio+"."+type */
                escribir_factura_document(directorio,fxml,"xml");
                escribir_factura_document(directorio,fhtml,"html");
                escribir_factura_document_Byte(directorio,fpdf);
                ubicacionCFDI=directorio+"TFJV"+serie+folio;
                //buffer=new StringBuilder("");
            }
           
            return buffer.toString();
           
    }
    
   

/*METODO QUE OBTIENE EL OBJETO SOAP*/
public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
 }
    @Override
    public void sendFacturaCFD(String XML) throws Exception {
           String dato3=procesarCFD(XML);

    }

    @Override
    public String procesarCFD(String XML_CDF) throws Exception {

       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = null;
			try {
				docBuilder = docFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				logger.info("valio madre en elr pocesamiento");
			}
    		try {
				Document doc = docBuilder.parse(XML_CDF);/*parseo!!*/
				Transformer transformer = null;
				try {
					transformer = TransformerFactory.newInstance().newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
                                    logger.info("valio madre en elr pocesamiento");
				} catch (TransformerFactoryConfigurationError e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
                                    logger.info("valio madre en elr pocesamiento");
				}
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				//initialize StreamResult with File object to save to file
				StreamResult result = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(doc);
				transformer.transform(source, result);

				String xmlString = result.getWriter().toString();
				return xmlString;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
                            logger.info("valio madre en elr pocesamiento");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
                            logger.info("valio madre en elr pocesamiento");
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
                            logger.info("valio madre en elr pocesamiento");
			}
           return "";
    }

    @Override
    public String decodificar(String entrada) throws IOException{
         return new String(new BASE64Decoder().decodeBuffer(entrada));
    }

    @Override
    public String crearDirectory(String serie) {
        String ruta=ConstantesFacturacion.RUTA_REPOSITORIO_CFD+serie+"/respaldo/";
        File file=new File(ruta);
        if(!file.exists()){
            file.mkdirs();
        }
        return ruta;



    }

    @Override
    public void escribir_factura_document(String directorio,String recurso,String type) {
        try {
            String archivoBASE = decodificar(recurso);
            File archivo=new File(directorio,"TFJV"+serie+folio+"."+type);
            BufferedWriter f=new BufferedWriter(new FileWriter(archivo));
            f.write(archivoBASE);
            f.close();
        } catch (IOException ex) {
            logger.error("Error al tratar de escribir la factura metodo escribir_factura_document", ex);
        }

    }

    @Override
    public void escribir_factura_document_Byte(String directorio, String recurso) {
        FileOutputStream out = null;
        try {
            File archivo = new File(directorio, "TFJV" + serie + folio + ".pdf");
            byte[] buffer = Base64.decodeBase64(recurso.getBytes());
            out = new FileOutputStream(archivo);
            out.write(buffer, 0, buffer.length);
            out.close();
        } catch (IOException ex) {
           logger.error("Error al escribir la factura formato PDF", ex);

        } finally {
            try {
                out.close();
            } catch (IOException ex) {
               logger.error("Error al cerrar la factura formato PDF", ex);
            }
        }
    }




}
