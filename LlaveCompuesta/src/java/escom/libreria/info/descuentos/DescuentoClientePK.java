/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.descuentos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class DescuentoClientePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;
    @Basic(optional = false)
    @Column(name = "ID_DESCUENTO")
    private int idDescuento;

    public DescuentoClientePK() {
    }

    public DescuentoClientePK(String idCliente, int idDescuento) {
        this.idCliente = idCliente;
        this.idDescuento = idDescuento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(int idDescuento) {
        this.idDescuento = idDescuento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        hash += (int) idDescuento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DescuentoClientePK)) {
            return false;
        }
        DescuentoClientePK other = (DescuentoClientePK) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        if (this.idDescuento != other.idDescuento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.DescuentoClientePK[idCliente=" + idCliente + ", idDescuento=" + idDescuento + "]";
    }

}
