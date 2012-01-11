/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.proveedor;

import escom.libreria.info.facturacion.Articulo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "proveedor_articulo")
@NamedQueries({
    @NamedQuery(name = "ProveedorArticulo.findAll", query = "SELECT p FROM ProveedorArticulo p"),
    @NamedQuery(name = "ProveedorArticulo.findByIdProveedor", query = "SELECT p FROM ProveedorArticulo p WHERE p.proveedorArticuloPK.idProveedor = :idProveedor"),
    @NamedQuery(name = "ProveedorArticulo.findByIdArticulo", query = "SELECT p FROM ProveedorArticulo p WHERE p.proveedorArticuloPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "ProveedorArticulo.findByCantidad", query = "SELECT p FROM ProveedorArticulo p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "ProveedorArticulo.findByUltMof", query = "SELECT p FROM ProveedorArticulo p WHERE p.ultMof = :ultMof")})
public class ProveedorArticulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProveedorArticuloPK proveedorArticuloPK;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "ULT_MOF")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultMof;
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;
    @Basic(optional = false)
    @Column(name = "PESO")
    private BigDecimal peso;

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }



    public ProveedorArticulo() {
    }

    public ProveedorArticulo(ProveedorArticuloPK proveedorArticuloPK) {
        this.proveedorArticuloPK = proveedorArticuloPK;
    }

    public ProveedorArticulo(ProveedorArticuloPK proveedorArticuloPK, int cantidad, Date ultMof) {
        this.proveedorArticuloPK = proveedorArticuloPK;
        this.cantidad = cantidad;
        this.ultMof = ultMof;
    }

    public ProveedorArticulo(int idProveedor, int idArticulo) {
        this.proveedorArticuloPK = new ProveedorArticuloPK(idProveedor, idArticulo);
    }

    public ProveedorArticuloPK getProveedorArticuloPK() {
        return proveedorArticuloPK;
    }

    public void setProveedorArticuloPK(ProveedorArticuloPK proveedorArticuloPK) {
        this.proveedorArticuloPK = proveedorArticuloPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getUltMof() {
        return ultMof;
    }

    public void setUltMof(Date ultMof) {
        this.ultMof = ultMof;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proveedorArticuloPK != null ? proveedorArticuloPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProveedorArticulo)) {
            return false;
        }
        ProveedorArticulo other = (ProveedorArticulo) object;
        if ((this.proveedorArticuloPK == null && other.proveedorArticuloPK != null) || (this.proveedorArticuloPK != null && !this.proveedorArticuloPK.equals(other.proveedorArticuloPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "articulo.ProveedorArticulo[proveedorArticuloPK=" + proveedorArticuloPK + "]";
    }

}
