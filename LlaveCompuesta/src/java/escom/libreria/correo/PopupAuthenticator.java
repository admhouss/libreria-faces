/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author Abraham de la Cruz
 */
public class PopupAuthenticator extends Authenticator{
    String username;
    String password;
    public PopupAuthenticator(String username,String password){
        this.username=username.trim();
        this.password=password.trim();
    }
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username,password);
    }
}
