/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.comun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xxx
 */
public class ValidarNumero {

    private String enteredEmail;
       public boolean validarNumero(String value){

           boolean matchFound =false;
           enteredEmail = value.trim();

        if(enteredEmail.length()!=0){
                //Set the   Numero pattern string
                    Pattern p = Pattern.compile("[0-9]*");
                //Match the given string with the pattern
                    Matcher m = p.matcher(enteredEmail);
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

}
