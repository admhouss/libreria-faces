/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.ejb;


import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.carrito.jsf.CarritoDTO;
import escom.libreria.info.cliente.jpa.Cliente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author xxx
 */
@Local
public interface CarritoCompraTemporalLocal {
    public void addPublicacion(Publicacion articulo);//Agregar un articulo al carrito de compra
    public void removePublicacion(Publicacion articulo); //borra un articulo del carrito de compra
    public List<CarritoDTO> getListPublicacion(); //Retorna una lista de los articulos agregados al carrito
    public boolean Emtity();//Si la lista esta vacia
    public int getCount();
    public CarritoDTO buscarArticulo(Publicacion p);//dado una publicacion retorna su DTO
    
    
}
