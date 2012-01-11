/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "enviorealizado")
@NamedQueries({
    @NamedQuery(name = "Enviorealizado.findAll", query = "SELECT e FROM Enviorealizado e"),
    @NamedQuery(name = "Enviorealizado.findByIdPedido", query = "SELECT e FROM Enviorealizado e WHERE e.idPedido = :idPedido"),
    @NamedQuery(name = "Enviorealizado.findByFechaRecibo", query = "SELECT e FROM Enviorealizado e WHERE e.fechaRecibo = :fechaRecibo"),
    @NamedQuery(name = "Enviorealizado.findByObservaciones", query = "SELECT e FROM Enviorealizado e WHERE e.observaciones = :observaciones")})
public class Enviorealizado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PEDIDO")
    private Integer idPedido;
    @Basic(optional = false)
    @Column(name = "FECHA_RECIBO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecibo;
    @Basic(optional = false)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @JoinColumns(
    {@JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID_PEDIDO", insertable = false, updatable = false),
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)
    })
    @OneToOne(optional = false)
    private Pedido pedido;
     @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private String artoculo;

    public String getArtoculo() {
        return artoculo;
    }

    public void setArtoculo(String artoculo) {
        this.artoculo = artoculo;
    }




    public Enviorealizado() {
    }

    public Enviorealizado(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Enviorealizado(Integer idPedido, Date fechaRecibo, String observaciones) {
        this.idPedido = idPedido;
        this.fechaRecibo = fechaRecibo;
        this.observaciones = observaciones;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFechaRecibo() {
        return fechaRecibo;
    }

    public void setFechaRecibo(Date fechaRecibo) {
        this.fechaRecibo = fechaRecibo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Enviorealizado)) {
            return false;
        }
        Enviorealizado other = (Enviorealizado) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.Enviorealizado[idPedido=" + idPedido + "]";
    }

}
