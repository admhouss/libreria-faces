/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.conversioNumeroToLetra;

/**
 *
 * @author xxx
 */
public class ConversionImp implements Conversion{

    public String convertirNumeroToLetra(String numeroPresicion) {
        Numero_a_Letra num = new Numero_a_Letra();
        return num.Convertir(numeroPresicion, band());

    }

     private static boolean band(){
        if ( Math.random() > .5) {
            return true;
        }else{
            return false;
        }
    }
}
