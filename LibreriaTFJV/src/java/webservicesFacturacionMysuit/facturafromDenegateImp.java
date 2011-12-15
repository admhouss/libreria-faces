/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicesFacturacionMysuit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransactionResponse;
import org.apache.axis2.AxisFault;
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

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.misc.BASE64Decoder;

/**
 *
 * @author xxx
 */
public class facturafromDenegateImp implements facturafromClient{


    public static String keyStore="C:/mikeystore/";
    public static String rutas_factura="C:/Users/xxx/Desktop/facturasPrueba/";///home/tribunal/Documentos/resultados/";
    public String cadenaExito;
    private String folio,serie;
    private boolean result=false;
    private String ruta_final;

    public String getRuta_final() {
        return ruta_final;
    }

    public void setRuta_final(String ruta_final) {
        this.ruta_final = ruta_final;
    }



    public  String getRutas_factura() {
        return rutas_factura;
    }

    public void setRutas_factura(String rutas_factura) {
        facturafromDenegateImp.rutas_factura = rutas_factura;
    }



    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }


    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }


    public String connectedMysuitFactor(String dato1) throws java.lang.Exception {
       
            System.setProperty("javax.net.ssl.trustStore", keyStore+"keystore.jks");
            System.setProperty("javax.net.ssl.keyStore", keyStore+"keystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "mipassword");
            System.setProperty("javax.net.ssl.keyStorePassword", "mipassword");

            mx.com.fact.www.schema.ws.FactWSFrontStub stub = new mx.com.fact.www.schema.ws.FactWSFrontStub(); //the default implementation should point to the right endpoint
            mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction requestTransaction6 = (mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction) getTestObject(mx.com.fact.www.schema.ws.FactWSFrontStub.RequestTransaction.class);

            requestTransaction6.setRequestor("0bd637a1-7a6f-4248-842f-3adaf3fe145f");
            requestTransaction6.setTransaction("CONVERT_NATIVE_XML");
            requestTransaction6.setCountry("MX");
            requestTransaction6.setEntity("TFJ360831MTA");
            requestTransaction6.setUser("0bd637a1-7a6f-4248-842f-3adaf3fe145f");
            requestTransaction6.setUserName("MX.TFJ360831MTA.TRIBUNALFISCAL");
            requestTransaction6.setData1(procesarCFD(dato1));
            requestTransaction6.setData2("PDFHTML XML");
            requestTransaction6.setData3("");

            RequestTransactionResponse response = stub.requestTransaction(requestTransaction6);
           cadenaExito="result tado proceso:"+response.getRequestTransactionResult().getResponse().getProcessor();
           cadenaExito="result tado codigo:"+response.getRequestTransactionResult().getResponse().getCode();
           cadenaExito="result tado: data"+response.getRequestTransactionResult().getResponse().getData();
           cadenaExito="result tado: descripcion"+response.getRequestTransactionResult().getResponse().getDescription();
           cadenaExito="result tado: result"+response.getRequestTransactionResult().getResponse().getResult();
           result=response.getRequestTransactionResult().getResponse().getResult();

            if(result){
               FactResponse transaccion = response.getRequestTransactionResult().getResponse();
                folio=transaccion.getIdentifier().getSerial();
                serie=transaccion.getIdentifier().getBatch();
                serie=serie.trim();
                String directorio=crearDirectory(serie);
                String fdato1 = response.getRequestTransactionResult().getResponseData().getResponseData1();
                String fdato2 = response.getRequestTransactionResult().getResponseData().getResponseData2();
                String fdato3 = response.getRequestTransactionResult().getResponseData().getResponseData3();
                ruta_final=directorio+"TFJ"+serie+folio;
                escribir_factura_document(directorio,fdato1,"xml");
                escribir_factura_document(directorio,fdato2,"html");
                escribir_factura_document_Byte(directorio,fdato3);
            }
           
            return cadenaExito;
    
    }



public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
 }
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
				e1.printStackTrace();
			}
    		try {
				Document doc = docBuilder.parse(XML_CDF);
				Transformer transformer = null;
				try {
					transformer = TransformerFactory.newInstance().newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerFactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           return "";
    }

    @Override
    public String decodificar(String entrada) throws IOException{
         return new String(new BASE64Decoder().decodeBuffer(entrada));
    }

    @Override
    public String crearDirectory(String serie) {
        String ruta=rutas_factura+serie+"/respaldo/";
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
            File archivo=new File(directorio,"TFJ"+serie+folio+"."+type);
            BufferedWriter f=new BufferedWriter(new FileWriter(archivo));
            f.write(archivoBASE);
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(facturafromDenegateImp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void escribir_factura_document_Byte(String directorio, String recurso) {
        FileOutputStream out = null;
        try {
            File archivo = new File(directorio, "TFJ" + serie + folio + ".pdf");
            byte[] buffer = Base64.decodeBase64(recurso.getBytes());
            out = new FileOutputStream(archivo);
            out.write(buffer, 0, buffer.length);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(facturafromDenegateImp.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(facturafromDenegateImp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
