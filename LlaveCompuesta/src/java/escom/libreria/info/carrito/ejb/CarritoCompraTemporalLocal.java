/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.ejb;


import escom.libreria.info.articulo.Publicacion;
import escom.libreria.info.carrito.jpa.PublicacionDTO;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author xxx
 */
@Local
public interface CarritoCompraTemporalLocal {
    public void addPublicacion(PublicacionDTO articulo);//Agregar un articulo al carrito de compra
    public void removePublicacion(PublicacionDTO articulo); //borra un articulo del carrito de compra
    public List<PublicacionDTO> getListPublicacion(); //Retorna una lista de los articulos agregados al carrito
    public boolean Emtity();//Si la lista esta vacia
    public int getCount();
    public PublicacionDTO buscarArticulo(PublicacionDTO p);//dado una publicacion retorna su DTO
    public PublicacionDTO buscarPublicacion(Publicacion p);
    public BigDecimal getMontoTotal();
    public int getPosArticulo(PublicacionDTO p);
    public boolean actualizarArticulo(PublicacionDTO obj,int pos);
    
    
}
