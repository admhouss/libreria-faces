/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

/**
 *
 * @author xxx
 */
@Stateful
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 30)
public class CarritoCompraTemporal implements CarritoCompraTemporalLocal {
    private List<Articulo> listaArticulo=new ArrayList<Articulo>();

    @Override
    public void addArticulo(Articulo articulo) {
        if(articulo!=null)
        listaArticulo.add(articulo);
        else
        throw new UnsupportedOperationException("Informacion del Articulo vacio.");
    }

    @Override
    public void removeArticulo(Articulo articulo) {

       if(articulo!=null){
        if(listaArticulo.contains(articulo)){
            listaArticulo.remove(articulo);
        }
        else{throw new UnsupportedOperationException("No se encontro una relacion con este articulo");
        }
    }//end if
      else
     throw new UnsupportedOperationException("Informacion del Articulo vacio.");
    }

    @Override
    public List<Articulo> getListArticulos() {

                 if(Emtity())
                  return null;
                 return listaArticulo;
        
    }

    @Override
    public boolean Emtity() {
         return listaArticulo.isEmpty();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
}
