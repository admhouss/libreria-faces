/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.administracion;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class ActividadusuarioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_ACTIVIDAD")
    private int idActividad;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private String idUsuario;

    public ActividadusuarioPK() {
    }

    public ActividadusuarioPK(int idActividad, String idUsuario) {
        this.idActividad = idActividad;
        this.idUsuario = idUsuario;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idActividad;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadusuarioPK)) {
            return false;
        }
        ActividadusuarioPK other = (ActividadusuarioPK) object;
        if (this.idActividad != other.idActividad) {
            return false;
        }
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "administracion.ActividadusuarioPK[idActividad=" + idActividad + ", idUsuario=" + idUsuario + "]";
    }

}
