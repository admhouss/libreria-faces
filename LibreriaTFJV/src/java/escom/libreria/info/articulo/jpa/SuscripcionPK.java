/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class SuscripcionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_SUSCRIPCION")
    private int idSuscripcion;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private int idArticulo;

    public SuscripcionPK() {
    }

    public SuscripcionPK(int idSuscripcion, int idArticulo) {
        this.idSuscripcion = idSuscripcion;
        this.idArticulo = idArticulo;
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
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
        hash += (int) idSuscripcion;
        hash += (int) idArticulo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionPK)) {
            return false;
        }
        SuscripcionPK other = (SuscripcionPK) object;
        if (this.idSuscripcion != other.idSuscripcion) {
            return false;
        }
        if (this.idArticulo != other.idArticulo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.jpa.SuscripcionPK[idSuscripcion=" + idSuscripcion + ", idArticulo=" + idArticulo + "]";
    }

}
