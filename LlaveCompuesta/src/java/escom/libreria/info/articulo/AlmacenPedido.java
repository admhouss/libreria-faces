/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo;

import escom.libreria.info.compras.Pedido;
import escom.libreria.info.proveedor.Proveedor;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "almacen_pedido")
@NamedQueries({
    @NamedQuery(name = "AlmacenPedido.findAll", query = "SELECT a FROM AlmacenPedido a"),
    @NamedQuery(name = "AlmacenPedido.findByIdPedido", query = "SELECT a FROM AlmacenPedido a WHERE a.almacenPedidoPK.idPedido = :idPedido"),
    @NamedQuery(name = "AlmacenPedido.findByIdArticulo", query = "SELECT a FROM AlmacenPedido a WHERE a.almacenPedidoPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "AlmacenPedido.findByProcAlmacen", query = "SELECT a FROM AlmacenPedido a WHERE a.procAlmacen = :procAlmacen")})
public class AlmacenPedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlmacenPedidoPK almacenPedidoPK;
    @Basic(optional = false)
    @Column(name = "PROC_ALMACEN")
    private String procAlmacen;
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumns({
        @JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID_PEDIDO", insertable = false, updatable = false),
        @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Pedido pedido;

    public AlmacenPedido() {
    }

    public AlmacenPedido(AlmacenPedidoPK almacenPedidoPK) {
        this.almacenPedidoPK = almacenPedidoPK;
    }

    public AlmacenPedido(AlmacenPedidoPK almacenPedidoPK, String procAlmacen) {
        this.almacenPedidoPK = almacenPedidoPK;
        this.procAlmacen = procAlmacen;
    }

    public AlmacenPedido(int idPedido, int idArticulo) {
        this.almacenPedidoPK = new AlmacenPedidoPK(idPedido, idArticulo);
    }

    public AlmacenPedidoPK getAlmacenPedidoPK() {
        return almacenPedidoPK;
    }

    public void setAlmacenPedidoPK(AlmacenPedidoPK almacenPedidoPK) {
        this.almacenPedidoPK = almacenPedidoPK;
    }

    public String getProcAlmacen() {
        return procAlmacen;
    }

    public void setProcAlmacen(String procAlmacen) {
        this.procAlmacen = procAlmacen;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (almacenPedidoPK != null ? almacenPedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlmacenPedido)) {
            return false;
        }
        AlmacenPedido other = (AlmacenPedido) object;
        if ((this.almacenPedidoPK == null && other.almacenPedidoPK != null) || (this.almacenPedidoPK != null && !this.almacenPedidoPK.equals(other.almacenPedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.AlmacenPedido[almacenPedidoPK=" + almacenPedidoPK + "]";
    }

}
