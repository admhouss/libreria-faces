/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.proveedor;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class ProveedorArticuloPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PROVEEDOR")
    private int idProveedor;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private int idArticulo;

    public ProveedorArticuloPK() {
    }

    public ProveedorArticuloPK(int idProveedor, int idArticulo) {
        this.idProveedor = idProveedor;
        this.idArticulo = idArticulo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
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
        hash += (int) idProveedor;
        hash += (int) idArticulo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProveedorArticuloPK)) {
            return false;
        }
        ProveedorArticuloPK other = (ProveedorArticuloPK) object;
        if (this.idProveedor != other.idProveedor) {
            return false;
        }
        if (this.idArticulo != other.idArticulo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "articulo.ProveedorArticuloPK[idProveedor=" + idProveedor + ", idArticulo=" + idArticulo + "]";
    }

}
