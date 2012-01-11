/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones;

import escom.libreria.info.facturacion.Articulo;

import escom.libreria.info.cliente.Cliente;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "suscripcion_cliente")
@NamedQueries({
    @NamedQuery(name = "SuscripcionCliente.findAll", query = "SELECT s FROM SuscripcionCliente s"),
    @NamedQuery(name = "SuscripcionCliente.findByIdCliente", query = "SELECT s FROM SuscripcionCliente s WHERE s.suscripcionClientePK.idCliente = :idCliente"),
    @NamedQuery(name = "SuscripcionCliente.findByIdSuscripcion", query = "SELECT s FROM SuscripcionCliente s WHERE s.suscripcionClientePK.idSuscripcion = :idSuscripcion"),
    @NamedQuery(name = "SuscripcionCliente.findByEstadoEnvio", query = "SELECT s FROM SuscripcionCliente s WHERE s.estadoEnvio = :estadoEnvio"),
    @NamedQuery(name = "SuscripcionCliente.findByFechaEnvio", query = "SELECT s FROM SuscripcionCliente s WHERE s.fechaEnvio = :fechaEnvio")})
public class SuscripcionCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SuscripcionClientePK suscripcionClientePK;
    @Basic(optional = false)
    @Column(name = "ESTADO_ENVIO")
    private boolean estadoEnvio;
    @Basic(optional = false)
    @Column(name = "FECHA_ENVIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Articulo articulo;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
   @JoinColumns(
    {@JoinColumn(name = "ID_SUSCRIPCION", referencedColumnName = "ID_SUSCRIPCION", insertable = false, updatable = false),
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private Suscripcion suscripcion;

    public SuscripcionCliente() {
    }

    public SuscripcionCliente(SuscripcionClientePK suscripcionClientePK) {
        this.suscripcionClientePK = suscripcionClientePK;
    }

    public SuscripcionCliente(SuscripcionClientePK suscripcionClientePK, boolean estadoEnvio, Date fechaEnvio) {
        this.suscripcionClientePK = suscripcionClientePK;
        this.estadoEnvio = estadoEnvio;
        this.fechaEnvio = fechaEnvio;
    }

    public SuscripcionCliente(String idCliente, int idSuscripcion) {
        this.suscripcionClientePK = new SuscripcionClientePK(idCliente, idSuscripcion);
    }

    public SuscripcionClientePK getSuscripcionClientePK() {
        return suscripcionClientePK;
    }

    public void setSuscripcionClientePK(SuscripcionClientePK suscripcionClientePK) {
        this.suscripcionClientePK = suscripcionClientePK;
    }

    public boolean getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(boolean estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        hash += (suscripcionClientePK != null ? suscripcionClientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionCliente)) {
            return false;
        }
        SuscripcionCliente other = (SuscripcionCliente) object;
        if ((this.suscripcionClientePK == null && other.suscripcionClientePK != null) || (this.suscripcionClientePK != null && !this.suscripcionClientePK.equals(other.suscripcionClientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.SuscripcionCliente[suscripcionClientePK=" + suscripcionClientePK + "]";
    }

}
