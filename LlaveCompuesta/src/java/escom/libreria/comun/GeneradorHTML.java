/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.libreria.comun;

//package generadorhtml;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jocelin
 */
public class GeneradorHTML {

    /**
     * @param args the command line arguments
     */
    private StringBuffer buffer=new StringBuffer();
    

    public GeneradorHTML() {
        buffer=new StringBuffer();
       
    }
    
   
    
    public String generdarHTMLOlvideContrasenia(String usuario,String password,String nombre){
       
        buffer.append("<html>");
        buffer.append("<body>");
        buffer.append("<head>");
        
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/> ");

        buffer.append("</head>");
        buffer.append("<br></br>");
       
        buffer.append("<table align=\"center\" bordercolor=\"#7919298d\"  style=\"border&#58;1px solid #71D22C;padding&#58;20px\" >");
        buffer.append("<tr>");
        buffer.append("<td  rowspan=\"1\" colspan=\"2\">");
        buffer.append("<img align=\"center\" src=\"http://libreria-tfjfa.com/articulos/logo.png\" width=\"100\"> ");
        buffer.append("</td>");
        buffer.append("<td  rowspan=\"1\" colspan=\"1\">");
        buffer.append("<p align=\"right\"><font color=\"#084B8A\" ><strong>RECUPERACION DE CONTRASE&Ntilde;A</strong></font></p>");
        buffer.append("</td>");
        buffer.append("<tr>");
        buffer.append("<tr>");
        buffer.append("<td  rowspan=\"1\" colspan=\"3\">");
        buffer.append("<br></br>");
        buffer.append("Hola <font color=\"#B40404\" > ").append(nombre).append("!!</font><br></br>");
        buffer.append("Bienvenido(a) a <a href=\"http://libreria-tfjfa.com/LibreriaTFJV/\">Libreria Virtual  </a> <br></br>");
        buffer.append("La cuenta con la que te registraste para ingresar a nuestros sitio y revisar el estatus de tus pedidos es");
        buffer.append("</td>");
        buffer.append("</tr>");
        buffer.append("<tr>");
        buffer.append("<td bgcolor=\"#71D22C\"  rowspan=\"1\" colspan=\"1\">Correo:");
        buffer.append("</td>");
        buffer.append("<td rowspan=\"1\" colspan=\"2\"><font color=\"gray\">").append(usuario).append("</font>");
        buffer.append("</td>");
        buffer.append("</tr>");
        buffer.append("<tr>");
        buffer.append("<td bgcolor=\"#71D22C\"  rowspan=\"1\" colspan=\"1\">");
        buffer.append("Contrase&ntilde;a:");
        buffer.append("</td>");
        buffer.append("<td  rowspan=\"1\" colspan=\"1\"><font color=\"gray\">").append(password).append("</font>");
        buffer.append("</td>");
        buffer.append("</tr>");
        buffer.append("<br></br>");
        buffer.append("<tr>");
        buffer.append("<td  rowspan=\"1\" colspan=\"3\">");
        buffer.append("Si deseas acceder ahora da click <a href=\"http://libreria-tfjfa.com/LibreriaTFJV/\">AQUI</a> <br></br>");
        buffer.append("<b>Gracias</b> por registrarte con nosotros");
        buffer.append("</td>");
        buffer.append("</tr>");
        buffer.append("</table>");
        buffer.append("</body>");
        buffer.append("</html>");
        
        return buffer.toString();
    }

    public String generdarHTMLConfirmar(String usuario,String password,String nombre,String urlEncoding){
        //buffer.
        buffer.append("<html>");//La idea es que genreres dinamicamente con los parametros que yo envie
        //la misma salida que la tienes!! me explico?'
      //si tienes dudas me dices



buffer.append("<head>");
buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/> ");
buffer.append("</head>");
buffer.append("<body>");
buffer.append("<br></br>");
buffer.append("<table align=\"center\" bordercolor=\"#71D22C\"  style=\"border&#58;1px solid #71D22C;padding&#58;20px\" >");
buffer.append("<tr>");
buffer.append("<td  rowspan=\"1\" colspan=\"2\">");
buffer.append("<img align=\"center\"  src=\"http://libreria-tfjfa.com/articulos/logo.png\" width=\"100\" >");
buffer.append("</td>");
buffer.append("<td  rowspan=\"1\" colspan=\"1\">");
buffer.append("<p align=\"right\"><font color=\"#084B8A\" ><strong> REGISTRO DE CLIENTE </strong></font></p>");
buffer.append("</td>");
buffer.append("<tr>");
buffer.append("<tr>");
buffer.append("<td  rowspan=\"1\" colspan=\"3\">");
buffer.append("<br></br>");
buffer.append("Hola <font color=\"#B40404\">").append(nombre).append("!!</font><br></br>");
buffer.append("Bienvenido(a) a <a href=\"http://libreria-tfjfa.com/LibreriaTFJV/\">Libreria Virtual  </a> <br></br>");
buffer.append("La cuenta con la que podras ingresar a nuestro sitio y revisar el estatus de tus pedidos es");
buffer.append("</td>");
buffer.append("</tr>");
buffer.append("<tr>");
buffer.append("<td bgcolor=\"#71D22C\"  rowspan=\"1\" colspan=\"1\">Correo:");
buffer.append("</td>");
buffer.append("<td rowspan=\"1\" colspan=\"2\"><font color=\"gray\">").append(usuario).append("</font>");
buffer.append("</td>");
buffer.append("</tr>");
buffer.append("<tr>");
buffer.append("<td bgcolor=\"#71D22C\"  rowspan=\"1\" colspan=\"1\">");
buffer.append("Contrase&ntilde;a:");
buffer.append("</td>");
buffer.append("<td  rowspan=\"1\" colspan=\"1\"><font color=\"gray\">").append(password).append("</font>");
buffer.append("</td>");
buffer.append("</tr>");
buffer.append("<br></br>");
buffer.append("<tr>");
buffer.append("<td  rowspan=\"1\" colspan=\"3\">");
buffer.append("Y la debes activar dando click <a href=\"").append(urlEncoding).append("\">AQUI</a> <br></br>");
buffer.append("Es muy importante que <b>no olvides</b> tu correo ni tu contrase&ntilde;a para que puedas gozar de los <br></br>");
buffer.append("beneficios que te ofrecemos.");
buffer.append("<br>");
buffer.append("</br>");
buffer.append("<b>Gracias</b> por registrarte con nosotros");
buffer.append("</td>");
buffer.append("</tr>");
buffer.append("</table>");
buffer.append("</body>");
buffer.append("</html>");

return buffer.toString();
    }



}
