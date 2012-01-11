/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.descuentos;



import escom.libreria.info.cliente.Cliente;
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
@Table(name = "descuento_cliente")
@NamedQueries({
    @NamedQuery(name = "DescuentoCliente.findAll", query = "SELECT d FROM DescuentoCliente d"),
    @NamedQuery(name = "DescuentoCliente.findByIdCliente", query = "SELECT d FROM DescuentoCliente d WHERE d.descuentoClientePK.idCliente = :idCliente"),
    @NamedQuery(name = "DescuentoCliente.findByIdDescuento", query = "SELECT d FROM DescuentoCliente d WHERE d.descuentoClientePK.idDescuento = :idDescuento"),
    @NamedQuery(name = "DescuentoCliente.findByFechaInicio", query = "SELECT d FROM DescuentoCliente d WHERE d.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "DescuentoCliente.findByFechaFin", query = "SELECT d FROM DescuentoCliente d WHERE d.fechaFin = :fechaFin")})
public class DescuentoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DescuentoClientePK descuentoClientePK;
    @Basic(optional = false)
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @JoinColumn(name = "ID_DESCUENTO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Descuento descuento;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public DescuentoCliente() {
    }

    public DescuentoCliente(DescuentoClientePK descuentoClientePK) {
        this.descuentoClientePK = descuentoClientePK;
    }

    public DescuentoCliente(DescuentoClientePK descuentoClientePK, Date fechaInicio, Date fechaFin) {
        this.descuentoClientePK = descuentoClientePK;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public DescuentoCliente(String idCliente, int idDescuento) {
        this.descuentoClientePK = new DescuentoClientePK(idCliente, idDescuento);
    }

    public DescuentoClientePK getDescuentoClientePK() {
        return descuentoClientePK;
    }

    public void setDescuentoClientePK(DescuentoClientePK descuentoClientePK) {
        this.descuentoClientePK = descuentoClientePK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (descuentoClientePK != null ? descuentoClientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DescuentoCliente)) {
            return false;
        }
        DescuentoCliente other = (DescuentoCliente) object;
        if ((this.descuentoClientePK == null && other.descuentoClientePK != null) || (this.descuentoClientePK != null && !this.descuentoClientePK.equals(other.descuentoClientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.DescuentoCliente[descuentoClientePK=" + descuentoClientePK + "]";
    }

}
