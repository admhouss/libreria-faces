/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.jdbc.reporteador;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author xxx
 */
public class ArticuloDTO implements Serializable{

   private Integer existencia,consigna,firme,cantidad;
   private String proveedor,articulo;
   private BigDecimal	precio,impuesto,promocion,descuento;

    public ArticuloDTO(Integer almacen, Integer consigna, Integer firme, Integer cantidad, String proveedor, String articulo, BigDecimal precio, BigDecimal impuesto, BigDecimal promocion, BigDecimal descuento) {
        this.existencia = almacen;
        this.consigna = consigna;
        this.firme = firme;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
        this.articulo = articulo;
        this.precio = precio;
        this.impuesto = impuesto;
        this.promocion = promocion;
        this.descuento = descuento;
    }

    public ArticuloDTO() {

    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }


   

    
   

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getConsigna() {
        return consigna;
    }

    public void setConsigna(Integer consigna) {
        this.consigna = consigna;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Integer getFirme() {
        return firme;
    }

    public void setFirme(Integer firme) {
        this.firme = firme;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPromocion() {
        return promocion;
    }

    public void setPromocion(BigDecimal promocion) {
        this.promocion = promocion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }



}
