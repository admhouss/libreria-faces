/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class PedidoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PEDIDO")
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private int idArticulo;

    public PedidoPK() {
    }

    public PedidoPK(int idPedido, int idArticulo) {
        this.idPedido = idPedido;
        this.idArticulo = idArticulo;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPedido;
        hash += (int) idArticulo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoPK)) {
            return false;
        }
        PedidoPK other = (PedidoPK) object;
        if (this.idPedido != other.idPedido) {
            return false;
        }
        if (this.idArticulo != other.idArticulo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.escom.info.compra.PedidoPK[idPedido=" + idPedido + ", idArticulo=" + idArticulo + "]";
    }

}
