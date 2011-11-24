/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.procesarEditorialXML;


import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ControladorError implements  ErrorHandler {

    public void warning(SAXParseException exception)  {
        exception.getMessage();
        System.out.println(exception.getLineNumber()+exception.getLocalizedMessage());
    }

    public void error(SAXParseException exception)  {
         System.out.println(exception.getLineNumber()+exception.getLocalizedMessage());
    }

    public void fatalError(SAXParseException exception)  {
          System.out.println(exception.getLineNumber()+exception.getLocalizedMessage());
    }

}
