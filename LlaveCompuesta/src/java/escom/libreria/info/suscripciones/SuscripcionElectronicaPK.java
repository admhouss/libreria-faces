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
public class SuscripcionElectronicaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_SUSCRIPCION")
    private int idSuscripcion;
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;

    public SuscripcionElectronicaPK() {
    }

    public SuscripcionElectronicaPK(int idSuscripcion, String idCliente) {
        this.idSuscripcion = idSuscripcion;
        this.idCliente = idCliente;
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSuscripcion;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionElectronicaPK)) {
            return false;
        }
        SuscripcionElectronicaPK other = (SuscripcionElectronicaPK) object;
        if (this.idSuscripcion != other.idSuscripcion) {
            return false;
        }
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.SuscripcionElectronicaPK[idSuscripcion=" + idSuscripcion + ", idCliente=" + idCliente + "]";
    }

}
