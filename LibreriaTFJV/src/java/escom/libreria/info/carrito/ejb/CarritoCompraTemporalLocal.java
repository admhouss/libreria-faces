/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author xxx
 */
@Local
public interface CarritoCompraTemporalLocal {
    public void addArticulo(Articulo articulo);//Agregar un articulo al carrito de compra
    public void removeArticulo(Articulo articulo); //borra un articulo del carrito de compra
    public List<Articulo> getListArticulos(); //Retorna una lista de los articulos agregados al carrito
    public boolean Emtity();//Si la lista esta vacia
    
}
