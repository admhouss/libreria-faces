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
public class TelefonoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;

    public TelefonoPK() {
    }

    public TelefonoPK(int id, String idCliente) {
        this.id = id;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        hash += (int) id;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelefonoPK)) {
            return false;
        }
        TelefonoPK other = (TelefonoPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.cliente.jpa.TelefonoPK[id=" + id + ", idCliente=" + idCliente + "]";
    }

}
