/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo;

import escom.libreria.info.carrito.jsf.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author xxx
 */
public class ValidadorCorreo implements Validator{

    public ValidadorCorreo() {
    }



    private String enteredEmail;
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {



           boolean matchFound =false;
           enteredEmail = (String)value;

        if(enteredEmail.length()!=0){
                //Set the   email pattern string
                    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                //Match the given string with the pattern
                    Matcher m = p.matcher(enteredEmail);
                //Check whether match is found
                    matchFound = m.matches();
            }else{
                matchFound =true;
        }
    if (!matchFound) {
        System.out.println("No validar");
        FacesMessage message = new FacesMessage();
        message.setDetail("Email no valido");
        message.setSummary("Email no valido");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(message);
    }

    }

}
