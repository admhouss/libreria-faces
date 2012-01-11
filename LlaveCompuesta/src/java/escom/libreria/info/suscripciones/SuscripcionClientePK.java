/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class SuscripcionClientePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;
    @Basic(optional = false)
    @Column(name = "ID_SUSCRIPCION")
    private int idSuscripcion;

    public SuscripcionClientePK() {
    }

    public SuscripcionClientePK(String idCliente, int idSuscripcion) {
        this.idCliente = idCliente;
        this.idSuscripcion = idSuscripcion;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        hash += (int) idSuscripcion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionClientePK)) {
            return false;
        }
        SuscripcionClientePK other = (SuscripcionClientePK) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        if (this.idSuscripcion != other.idSuscripcion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.SuscripcionClientePK[idCliente=" + idCliente + ", idSuscripcion=" + idSuscripcion + "]";
    }

}
