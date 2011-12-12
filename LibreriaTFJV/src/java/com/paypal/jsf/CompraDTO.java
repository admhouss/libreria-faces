/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paypal.jsf;

import java.math.BigDecimal;

/**
 *
 * @author xxx
 */
public class CompraDTO {

    private BigDecimal impuesto;
    private BigDecimal descuento ;
    private BigDecimal totalMonto;

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(BigDecimal totalMonto) {
        this.totalMonto = totalMonto;
    }





    

}
