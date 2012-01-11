/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones;

import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.compras.Pedido;
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
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "suscripcion_envios")
@NamedQueries({
    @NamedQuery(name = "SuscripcionEnvios.findAll", query = "SELECT s FROM SuscripcionEnvios s"),
    @NamedQuery(name = "SuscripcionEnvios.findByIdPedido", query = "SELECT s FROM SuscripcionEnvios s WHERE s.suscripcionEnviosPK.idPedido = :idPedido"),
    @NamedQuery(name = "SuscripcionEnvios.findByIdSuscripcion", query = "SELECT s FROM SuscripcionEnvios s WHERE s.suscripcionEnviosPK.idSuscripcion = :idSuscripcion"),
    @NamedQuery(name = "SuscripcionEnvios.findByIdArticulo", query = "SELECT s FROM SuscripcionEnvios s WHERE s.suscripcionEnviosPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "SuscripcionEnvios.findByEstadoEnvio", query = "SELECT s FROM SuscripcionEnvios s WHERE s.estadoEnvio = :estadoEnvio"),
    @NamedQuery(name = "SuscripcionEnvios.findByObservaciones", query = "SELECT s FROM SuscripcionEnvios s WHERE s.observaciones = :observaciones")})
public class SuscripcionEnvios implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SuscripcionEnviosPK suscripcionEnviosPK;
    @Basic(optional = false)
    @Column(name = "ESTADO_ENVIO")
    private boolean estadoEnvio;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    
    @JoinColumns(
    {@JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID_PEDIDO", insertable = false, updatable = false),
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;
    @JoinColumns(
    {@JoinColumn(name = "ID_SUSCRIPCION", referencedColumnName = "ID_SUSCRIPCION", insertable = false, updatable = false),
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private Suscripcion suscripcion;

    public SuscripcionEnvios() {
    }

    public SuscripcionEnvios(SuscripcionEnviosPK suscripcionEnviosPK) {
        this.suscripcionEnviosPK = suscripcionEnviosPK;
    }

    public SuscripcionEnvios(SuscripcionEnviosPK suscripcionEnviosPK, boolean estadoEnvio) {
        this.suscripcionEnviosPK = suscripcionEnviosPK;
        this.estadoEnvio = estadoEnvio;
    }

    public SuscripcionEnvios(int idPedido, int idSuscripcion, int idArticulo) {
        this.suscripcionEnviosPK = new SuscripcionEnviosPK(idPedido, idSuscripcion, idArticulo);
    }

    public SuscripcionEnviosPK getSuscripcionEnviosPK() {
        return suscripcionEnviosPK;
    }

    public void setSuscripcionEnviosPK(SuscripcionEnviosPK suscripcionEnviosPK) {
        this.suscripcionEnviosPK = suscripcionEnviosPK;
    }

    public boolean getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(boolean estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
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

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (suscripcionEnviosPK != null ? suscripcionEnviosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionEnvios)) {
            return false;
        }
        SuscripcionEnvios other = (SuscripcionEnvios) object;
        if ((this.suscripcionEnviosPK == null && other.suscripcionEnviosPK != null) || (this.suscripcionEnviosPK != null && !this.suscripcionEnviosPK.equals(other.suscripcionEnviosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.SuscripcionEnvios[suscripcionEnviosPK=" + suscripcionEnviosPK + "]";
    }

}
