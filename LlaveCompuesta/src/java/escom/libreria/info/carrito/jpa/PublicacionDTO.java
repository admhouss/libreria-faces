/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import escom.libreria.info.facturacion.Articulo;
import java.math.RoundingMode;

/**
 *
 * @author xxx
 */
public class PublicacionDTO implements Serializable{

    private String titulo;
    private String asunto;
    private String autor;
    private int cantidad;
    private String editorial;
    private BigDecimal precio;
    private BigDecimal desc;
    private BigDecimal gastosEnvio;
    private double total;
    private int idPublicacion,idArticulo;
    private BigDecimal impuesto;
    private Date fechaCompra;
    private int indice;
    private Articulo articulo;
    private boolean typePublicacion;
    private Integer idSuscripcion;

    public Integer getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(Integer idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }


    public boolean isTypePublicacion() {
        return typePublicacion;
    }

    public void setTypePublicacion(boolean typePublicacion) {
        this.typePublicacion = typePublicacion;
    }

    


    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }


    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public BigDecimal getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(BigDecimal gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }



    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }



    public String getAsunto() {
        return asunto;
    }


    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getDesc() {
        return desc;
    }

    public void setDesc(BigDecimal desc) {
        this.desc = desc;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getTotal() {
        BigDecimal big=new BigDecimal(total);
        big.setScale(2,BigDecimal.ROUND_UP);
        total=big.doubleValue();
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public PublicacionDTO() {
    }

    


}
