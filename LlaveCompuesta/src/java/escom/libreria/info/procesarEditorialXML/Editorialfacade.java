/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.procesarEditorialXML;

/**
 *
 * @author xxx
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import javax.ejb.Stateless;
/*
 *
 * @author xxx
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

@Stateless
public class Editorialfacade {


public List<String> getEditorialByXML(String rutaXML){
    List<String> editoriales=new ArrayList<String>();
        try {
            ControladorError handler=new ControladorError();
            SAXBuilder builder = new SAXBuilder(true);
            Document doc = builder.build(rutaXML);
            builder.setErrorHandler(handler);
            Element elemento = doc.getRootElement();
            List elementos = elemento.getChildren("Libro");

            Iterator iterator = elementos.iterator();
            while (iterator.hasNext()) {
                Element e = (Element) iterator.next();
                editoriales.add(e.getChildText("Editorial"));
            }
            return editoriales;
        } catch (JDOMException ex) {
            ex.printStackTrace();
           Menssage.mensajeError="XML NO VALIDO,FAVOR DE REVISAR SU EXTRUCTURA\n";
        } catch (IOException ex) {
            ex.printStackTrace();
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return editoriales;
    }


}

