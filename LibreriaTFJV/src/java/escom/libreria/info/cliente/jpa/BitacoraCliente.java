/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.jpa;

import escom.libreria.info.articulo.jpa.Articulo;
import java.io.Serializable;
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
@Table(name = "bitacora_cliente")
@NamedQueries({
    @NamedQuery(name = "BitacoraCliente.findAll", query = "SELECT b FROM BitacoraCliente b"),
    @NamedQuery(name = "BitacoraCliente.findByIdMovimiento", query = "SELECT b FROM BitacoraCliente b WHERE b.bitacoraClientePK.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "BitacoraCliente.findByIdCliente", query = "SELECT b FROM BitacoraCliente b WHERE b.bitacoraClientePK.idCliente = :idCliente"),
    @NamedQuery(name = "BitacoraCliente.findByIdArticulo", query = "SELECT b FROM BitacoraCliente b WHERE b.bitacoraClientePK.idArticulo = :idArticulo"),
    @NamedQuery(name = "BitacoraCliente.findByFecha", query = "SELECT b FROM BitacoraCliente b WHERE b.fecha = :fecha"),
    @NamedQuery(name = "BitacoraCliente.findByEstado", query = "SELECT b FROM BitacoraCliente b WHERE b.estado = :estado")})
public class BitacoraCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BitacoraClientePK bitacoraClientePK;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private boolean estado;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;

    public BitacoraCliente() {
    }

    public BitacoraCliente(BitacoraClientePK bitacoraClientePK) {
        this.bitacoraClientePK = bitacoraClientePK;
    }

    public BitacoraCliente(BitacoraClientePK bitacoraClientePK, Date fecha, boolean estado) {
        this.bitacoraClientePK = bitacoraClientePK;
        this.fecha = fecha;
        this.estado = estado;
    }

    public BitacoraCliente(int idMovimiento, String idCliente, int idArticulo) {
        this.bitacoraClientePK = new BitacoraClientePK(idMovimiento, idCliente, idArticulo);
    }

    public BitacoraClientePK getBitacoraClientePK() {
        return bitacoraClientePK;
    }

    public void setBitacoraClientePK(BitacoraClientePK bitacoraClientePK) {
        this.bitacoraClientePK = bitacoraClientePK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        hash += (bitacoraClientePK != null ? bitacoraClientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraCliente)) {
            return false;
        }
        BitacoraCliente other = (BitacoraCliente) object;
        if ((this.bitacoraClientePK == null && other.bitacoraClientePK != null) || (this.bitacoraClientePK != null && !this.bitacoraClientePK.equals(other.bitacoraClientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.cliente.jpa.BitacoraCliente[bitacoraClientePK=" + bitacoraClientePK + "]";
    }

}
