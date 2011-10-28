/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;

import escom.libreria.info.articulo.jpa.Publicacion;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author xxx
 */
public class CarritoDTO {

    private Publicacion publicacion;
    private int cantidad;
    private BigDecimal total;
    private int indice;

    public CarritoDTO(Publicacion publicacion, int cantidad, BigDecimal total, int indice) {
        this.publicacion = publicacion;
        this.cantidad = cantidad;
        this.total = total;
        this.indice = indice;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public BigDecimal getTotal() {
       return this.total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void doCalculoTotal(){
       setTotal(BigDecimal.ZERO);
       BigDecimal total=publicacion.getArticulo().getCosto().multiply(new BigDecimal(getCantidad()));
       this.total.add(total);
    }





}
