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
public class SuscripcionEnviosPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PEDIDO")
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "ID_SUSCRIPCION")
    private int idSuscripcion;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private int idArticulo;

    public SuscripcionEnviosPK() {
    }

    public SuscripcionEnviosPK(int idPedido, int idSuscripcion, int idArticulo) {
        this.idPedido = idPedido;
        this.idSuscripcion = idSuscripcion;
        this.idArticulo = idArticulo;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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
        hash += (int) idPedido;
        hash += (int) idSuscripcion;
        hash += (int) idArticulo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionEnviosPK)) {
            return false;
        }
        SuscripcionEnviosPK other = (SuscripcionEnviosPK) object;
        if (this.idPedido != other.idPedido) {
            return false;
        }
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
        return "compras.SuscripcionEnviosPK[idPedido=" + idPedido + ", idSuscripcion=" + idSuscripcion + ", idArticulo=" + idArticulo + "]";
    }

}
