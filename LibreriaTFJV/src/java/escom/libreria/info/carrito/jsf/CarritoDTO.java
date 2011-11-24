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
import java.util.Date;
import java.util.List;

/**
 *
 * @author xxx
 */
public class CarritoDTO {

    private String descripcion;
    private int cantidad;
    private BigDecimal descuento;
    private BigDecimal impuesto;
    private Date  fechaActual;
    private BigDecimal subtotal;
    private Publicacion publicacion;
    private int indice;



    public CarritoDTO(int indice,int cantidad, BigDecimal descuentoMaxCliente, Publicacion publicacion) {
        this.cantidad = cantidad;
        this.fechaActual=new Date();
        this.publicacion = publicacion;
        impuesto=BigDecimal.TEN;
        this.descuento=BigDecimal.ZERO;//convieneDescuento(descuentoMaxCliente, publicacion.getArticulo().getDescuentoArticulo().getDescuento());
        this.indice=indice;
    }


    private  BigDecimal convieneDescuento(BigDecimal descuentoA,BigDecimal descuentoB){
        if(descuentoB==null)
          descuentoB=BigDecimal.ZERO;
        switch(descuentoA.compareTo(descuentoB)){
            case 0:return descuentoA; //==son iguales;
            case -1:return descuentoB; //
            case 1: return descuentoA;
        }
        return descuentoA;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

   

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public BigDecimal getImpuesto() {
        if(impuesto==null)
         impuesto=BigDecimal.ZERO;
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public BigDecimal getSubtotal() {
        subtotal=BigDecimal.TEN;
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }


    public BigDecimal getImpuesto(Articulo articulo) {
        List<Impuesto> listimpuestos=articulo.getImpuestoList();
        impuesto=BigDecimal.ZERO;
        for(Impuesto im:listimpuestos){
            if(im.getMontoImpuesto()!=null)
             impuesto=im.getMontoImpuesto();
        }

        return impuesto;
    }

      public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

   /* private Publicacion publicacion;
    private int cantidad;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private int indice;
    private  Cliente cliente;
    private Date fechaCompra;
    private Articulo articulo;

    

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }






    public CarritoDTO(Cliente e,Publicacion publicacion, int cantidad, BigDecimal subtotal, int indice) {
        this.publicacion = publicacion;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.indice = indice;
        this.cliente=e;


    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaCompra() {
        if(fechaCompra==null)
          fechaCompra=new Date();

        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

  

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public BigDecimal getsubtotal() {

       this.subtotal=doCalculosubtotal();
       this.subtotal=this.subtotal.setScale(2,BigDecimal.ROUND_UP);
       return this.subtotal;
    }

    public void setsubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }



    private BigDecimal doCalculosubtotal(){
       articulo =publicacion.getArticulo();
       BigDecimal costoArticulo=articulo.getCosto();
       BigDecimal impuestosubtotal=BigDecimal.ZERO;
       BigDecimal subtotalOperacion=BigDecimal.ZERO;
       BigDecimal  descuentoArticulo=BigDecimal.ONE;

       costoArticulo=costoArticulo.multiply(new BigDecimal(getCantidad()));
       
       subtotalOperacion=impuestosubtotal.add(costoArticulo);

        if(articulo.getDescuentoArticulo()!=null && articulo.getDescuentoArticulo().getDescuento()!=null){
            descuentoArticulo=articulo.getDescuentoArticulo().getDescuento();
            descuentoArticulo=doCalcularDescuento(subtotalOperacion,descuentoArticulo);
            subtotalOperacion=subtotalOperacion.subtract(descuentoArticulo);
        }

 
       return subtotalOperacion;
    }




    private BigDecimal doCalcularDescuento(BigDecimal to,BigDecimal descuento){

         to=to.multiply(descuento);
         to=to.divide(new BigDecimal(100));
         return to;
    }
*/


}
