/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class EnvioelectronicoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PEDIDO")
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private int idArticulo;

    public EnvioelectronicoPK() {
    }

    public EnvioelectronicoPK(int idPedido, int idArticulo) {
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
        if (!(object instanceof EnvioelectronicoPK)) {
            return false;
        }
        EnvioelectronicoPK other = (EnvioelectronicoPK) object;
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
        return "compras.EnvioelectronicoPK[idPedido=" + idPedido + ", idArticulo=" + idArticulo + "]";
    }

}
