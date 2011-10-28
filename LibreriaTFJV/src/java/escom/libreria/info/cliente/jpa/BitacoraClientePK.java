/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class BitacoraClientePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_MOVIMIENTO")
    private int idMovimiento;
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private int idArticulo;

    public BitacoraClientePK() {
    }

    public BitacoraClientePK(int idMovimiento, String idCliente, int idArticulo) {
        this.idMovimiento = idMovimiento;
        this.idCliente = idCliente;
        this.idArticulo = idArticulo;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
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
        hash += (int) idMovimiento;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        hash += (int) idArticulo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraClientePK)) {
            return false;
        }
        BitacoraClientePK other = (BitacoraClientePK) object;
        if (this.idMovimiento != other.idMovimiento) {
            return false;
        }
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        if (this.idArticulo != other.idArticulo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.cliente.jpa.BitacoraClientePK[idMovimiento=" + idMovimiento + ", idCliente=" + idCliente + ", idArticulo=" + idArticulo + "]";
    }

}
