/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicesFacturacionMysuit;

import java.io.IOException;

/**
 *
 * @author xxx
 */
public interface facturafromClient {

    public abstract String  connectedMysuitFactor(String dato1) throws java.lang.Exception;
    public abstract  void sendFacturaCFD(String XML)  throws java.lang.Exception;
    public abstract  String procesarCFD(String XML_CDF)  throws java.lang.Exception;
    public abstract String decodificar(String x)throws IOException;
    public abstract String crearDirectory(String serie);
    public abstract void escribir_factura_document(String directorio,String recurso,String type);
    public abstract void escribir_factura_document_Byte(String directorio,String recurso);

  
}
