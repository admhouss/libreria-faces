/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.login.ejb;

import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**
 *
 * @author xxx
 */
@Stateless
public class SistemaFacade {
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public  CarritoCompraTemporalLocal getObtenerBandejaTemporal(){
        CarritoCompraTemporalLocal bandeja=null;
        try {
            InitialContext contex = new InitialContext();
            bandeja = (CarritoCompraTemporalLocal) contex.lookup("java:global/LibreriaTFJV/CarritoCompraTemporal!escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal");
            return bandeja;
        } catch (NamingException ex) {
            ex.printStackTrace();
            System.out.println("No se cargo la bandeja");
          // ex.printStackTrace();
        }
        return bandeja;
    }
 
}
