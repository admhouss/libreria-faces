/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.comun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author xxx
 */
public class ValidarFechaFormat {


              public static String   getFechaFormateada(Date fecha)
              {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    return df.format(fecha);
              }

              public static boolean  getValidarFecha(Date d1,Date d2){


                  if (d1.equals(d2))
                        return true;
                 else if (d1.before(d2))
                        return true;
                 else
                      return false;

              }


}
