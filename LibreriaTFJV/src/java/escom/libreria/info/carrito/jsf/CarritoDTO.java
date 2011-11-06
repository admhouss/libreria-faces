/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.Impuesto;
import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.cliente.jpa.Cliente;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author xxx
 */
public class CarritoDTO {

    private Publicacion publicacion;
    private int cantidad;
    private BigDecimal total;
    private int indice;
    private  Cliente cliente;
    public CarritoDTO(Cliente e,Publicacion publicacion, int cantidad, BigDecimal total, int indice) {
        this.publicacion = publicacion;
        this.cantidad = cantidad;
        this.total = total;
        this.indice = indice;
        this.cliente=e;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

       this.total=doCalculoTotal();
       this.total=this.total.setScale(2,BigDecimal.ROUND_UP);
       return this.total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
 private Articulo articulo;
    private BigDecimal doCalculoTotal(){
       articulo =publicacion.getArticulo();
       BigDecimal costoArticulo=articulo.getCosto();
       BigDecimal impuestoTotal=BigDecimal.ZERO;
       BigDecimal totalOperacion=BigDecimal.ZERO;
       BigDecimal  descuentoArticulo=BigDecimal.ONE;

       costoArticulo=costoArticulo.multiply(new BigDecimal(getCantidad()));
       List<Impuesto> listimpuestos=articulo.getImpuestoList();
        for(Impuesto im:listimpuestos){
            if(im.getMontoImpuesto()!=null)
            impuestoTotal=impuestoTotal.add(im.getMontoImpuesto());
        }
       totalOperacion=impuestoTotal.add(costoArticulo);

        if(articulo.getDescuentoArticulo()!=null && articulo.getDescuentoArticulo().getDescuento()!=null){
            descuentoArticulo=articulo.getDescuentoArticulo().getDescuento();
            descuentoArticulo=doCalcularDescuento(totalOperacion,descuentoArticulo);
            totalOperacion=totalOperacion.subtract(descuentoArticulo);
        }

 
       return totalOperacion;
    }


    private BigDecimal doCalcularDescuento(BigDecimal to,BigDecimal descuento){

         to=to.multiply(descuento);
         to=to.divide(new BigDecimal(100));
         return to;
    }



}
