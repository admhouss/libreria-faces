/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
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
//import nexttech.facto.mail.jpa.EnumTokensMail;
//import nexttech.facto.mail.jpa.Propiedades;
//import nexttech.facto.mail.jpa.ServidorCorreoConf;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author 
 */
@Stateless
@LocalBean
public class ProcesoJMail {
    //@EJB nexttech.gal.mail.ejb.ServidorCorreoConfFacade sConfFacade;

    public void enviaCorreo(MensajeCorreoDTO mensaje){
        Properties props = new Properties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.host","");
        props.put("mail.smtp.auth","true");
        //props.put("mail.smtp.port","25");
        String from="libreria";

        String password="new123";




        
        System.out.println("Haber si llega hasta aki" + props);
        ///ServidorCorreoConf sConf = sConfFacade.find(correoConfId);

        //for(Propiedades p:sConf.getPropiedadesList()) {
          //  props.put(p.getLlave(), p.getValor());
        //}


        Session session = Session.getInstance(props,new PopupAuthenticator(from,password));
        session.setDebug(true);
        System.out.println("session creada"+session);
        MimeMessage msg = new MimeMessage(session);
        

        try {
            msg.setSubject(mensaje.getAsunto());
            msg.setFrom(new InternetAddress(from));

        InternetAddress direcciones=null;
           for(String destinatario:mensaje.getDestinatarioList()){
               direcciones=new InternetAddress(destinatario);
               msg.setRecipient(Message.RecipientType.TO, direcciones);
               System.out.println(destinatario);
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

    String cadena="<html><head></head><body><p>"+Cuerpo+"</p>"+"<A HREF=\"http://www.google.com\">Notificacion de correo</A></p></body></html>";
    mensaje.setCuerpo(cadena);
    //mensaje.setAdjuntoList(c);
    
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
}

