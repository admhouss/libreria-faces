/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.lang.Validate;

/**
 *
 * @author xxx
 */
public class ValidarNumero implements Validator{

    private String enteredNum;


      /* public boolean validarNumero(String value){

           boolean matchFound =false;
           enteredNum = value.trim();

        if(enteredNum.length()!=0){
                //Set the   Numero pattern string
                    Pattern p = Pattern.compile("[0-9]*");
                //Match the given string with the pattern
                    Matcher m = p.matcher(enteredNum);
                //Check whether match is found
                    matchFound = m.matches();
            }else{
                matchFound =true;
            }
        if (!matchFound) {
            System.out.println("No validar");
            matchFound=false;
        }
           return matchFound;
       }
*/
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        boolean matchFound =false;

        System.out.println("entre jjeje");
             try{
               Integer numero=   (Integer)value;
             }catch(Exception e){



        /*if(enteredNum.length()!=0){
                //Set the   Numero pattern string
                    Pattern p = Pattern.compile("[0-9]*");
                //Match the given string with the pattern
                    Matcher m = p.matcher(enteredNum);
                //Check whether match is found
                    matchFound = m.matches();
            }else{
                matchFound =true;
            }*/
        //if (!matchFound) {

            //System.out.println("No validar");
            FacesMessage message = new FacesMessage();
            message.setDetail(enteredNum+" Puede ser un numero contenido de 1 a màs digitos");
            message.setSummary(enteredNum+" Puede ser un numero contenido de 1 a màs digitos");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);

        }
          // return matchFound;
    }

}
