/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo;

import escom.libreria.comun.GeneradorHTML;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.servidorConfiguracion.Propiedades;
import escom.libreria.servidorConfiguracion.ServidorCorreoConf;
import escom.libreria.servidorConfiguracion.ejb.ServidorCorreoConfFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author 
 */
@Stateless
@LocalBean
public class ProcesoJMail {
    @EJB private ServidorCorreoConfFacade sConfFacade;
    private List<Propiedades> listPropiedades;

    public void enviaCorreo(MensajeCorreoDTO mensaje){

        ServidorCorreoConf serverMail=sConfFacade.find(1);//VARIABLE QUE CAMBIA EL DOMINIO DE CORREO
        listPropiedades=serverMail.getPropiedadesList();
        Properties props = new Properties();
        for(Propiedades propiedad:listPropiedades)
         props.put(propiedad.getLlave(),propiedad.getValor());

         Session session = Session.getInstance(props,new PopupAuthenticator(serverMail.getUsuario(),serverMail.getContrasenia()));
        //Session session = Session.getInstance(props,new PopupAuthenticator("yamildelgado99@yahoo.com","refigerador"));
         session.setDebug(true);
       
        MimeMessage msg = new MimeMessage(session);

        

        try {
            msg.setSubject(mensaje.getAsunto());
            msg.setFrom(new InternetAddress(serverMail.getUsuario()));

            InternetAddress direcciones=null;
           for(String destinatario:mensaje.getDestinatarioList()){
               direcciones=new InternetAddress(destinatario);
               msg.setRecipient(Message.RecipientType.TO, direcciones);
              
                Multipart multipart = new MimeMultipart();
                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setContent(mensaje.getCuerpo(), mensaje.getTipoMensaje());

                multipart.addBodyPart(messageBodyPart);

            for(String adjunto:mensaje.getAdjuntoList()){
                File f = new File(adjunto);
                DataSource source = new FileDataSource(f);
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(f.getName());
                multipart.addBodyPart(messageBodyPart);
            }

            msg.setContent(multipart);
            try{
            Transport.send(msg);
               }catch(Exception e){
                  // e.printStackTrace();
                 System.out.println(e);
               }



           }
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Correos Enviados con exito", ""));
        } catch (MessagingException ex) {
            System.out.println(ex);

        }
         
    }


    
         
    public void enviarCorreo(String Asunto,String Cuerpo,List<String> correos) {
    MensajeCorreoDTO mensaje = new MensajeCorreoDTO();
    mensaje.setAsunto(Asunto);

   // String cadena="<html><head></head><body><p>"+Cuerpo+"</p></body></html>";
    mensaje.setCuerpo(Cuerpo);
    //mensaje.setAdjuntoList(c);
    
    for (String s : correos) {
    mensaje.getDestinatarioList().add(s);
     }
    //mensaje.setDestinatarioList(correos);
    mensaje.setTipoMensaje("text/html");
    enviaCorreo(mensaje);
    }

    public void enviarLibroComprado(String Asunto,List<String> librosElectronicos,String Cuerpo,List<String> correos) {
        MensajeCorreoDTO mensaje = new MensajeCorreoDTO();
        mensaje.setAsunto(Asunto);

   // String cadena="<html><head></head><body><p>"+Cuerpo+"</p></body></html>";
        mensaje.setCuerpo(Cuerpo);
        mensaje.setAdjuntoList(librosElectronicos);

        for (String s : correos) {
         mensaje.getDestinatarioList().add(s);
        }
    //mensaje.setDestinatarioList(correos);
       mensaje.setTipoMensaje("text/html");
       enviaCorreo(mensaje);
   }
    public void enviaCorreo(MensajeCorreoDTO mensaje,Map<EnumTokensMail,String> tokensValor){
//        for(EnumTokensMail t:EnumTokensMail.values()){
//            mensaje.setCuerpo(
//                mensaje.getCuerpo().replaceAll("<"+t.toString()+">", tokensValor.get(t)));
//            mensaje.setAsunto(
//                mensaje.getAsunto().replaceAll("<"+t.toString()+">", tokensValor.get(t)));
//        }
//        mensaje.setCuerpo(mensaje.getCuerpo().replaceAll("<"+"fechaEnvio"+">" ,Calendar.getInstance(Locale.ENGLISH).getTime().toString()));
//        mensaje.setCuerpo(mensaje.getCuerpo().replaceAll("<"+"nombreFactura"+">" ,"AE1F_111_a2010331.pdf"));

        enviaCorreo(mensaje);
    }
    
    private String query=null,baseURL;

    private StringBuffer prepareUrl(String ContextoPath){
         StringBuffer buffer= buffer=new StringBuffer();
         buffer.append("http://");
        // buffer.append("localhost:8080");
         buffer.append("www.libreria-tfjfa.com");
         buffer.append( ContextoPath);
         buffer.append("/faces/login/Create.xhtml");
         return buffer;
    }
    private StringBuffer buffer=null;
    public void EnviarConfimarCorreo(Cliente cliente){
        String nombreCliente="";
    if(cliente!=null){
        nombreCliente=cliente.getNombre() +" "+cliente.getPaterno()+" "+cliente.getMaterno();
        try{

           
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            buffer=prepareUrl(external.getRequestContextPath());
            List<String> cliList=new ArrayList<String>();
            List<String> ckey=new ArrayList<String>();
            cliList.add(cliente.getId());
            Map<String,List<String>> map=new HashMap<String, List<String>>();
            List<String> clientemap=new ArrayList<String>();
            clientemap.add(cliente.getId());
            ckey.add(1+"");
            map.put("correo",clientemap);
            map.put("bandera",ckey);
            baseURL=external.encodeRedirectURL(buffer.toString(), map);
            GeneradorHTML generadorHTML=new GeneradorHTML();
            query= generadorHTML.generdarHTMLConfirmar(cliente.getId(),cliente.getPassword(),nombreCliente, baseURL);
            enviarCorreo("Activar Registro", query, cliList);
        }catch(Exception e){e.printStackTrace();}
        }
    }
    



}

