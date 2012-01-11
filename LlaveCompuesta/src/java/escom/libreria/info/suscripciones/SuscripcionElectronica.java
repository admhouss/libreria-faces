/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones;

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
@Table(name = "suscripcion_electronica")
@NamedQueries({
    @NamedQuery(name = "SuscripcionElectronica.findAll", query = "SELECT s FROM SuscripcionElectronica s"),
    @NamedQuery(name = "SuscripcionElectronica.findByIdSuscripcion", query = "SELECT s FROM SuscripcionElectronica s WHERE s.suscripcionElectronicaPK.idSuscripcion = :idSuscripcion"),
    @NamedQuery(name = "SuscripcionElectronica.findByIdCliente", query = "SELECT s FROM SuscripcionElectronica s WHERE s.suscripcionElectronicaPK.idCliente = :idCliente"),
    @NamedQuery(name = "SuscripcionElectronica.findByNoLicencias", query = "SELECT s FROM SuscripcionElectronica s WHERE s.noLicencias = :noLicencias"),
    @NamedQuery(name = "SuscripcionElectronica.findByFechaInicio", query = "SELECT s FROM SuscripcionElectronica s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "SuscripcionElectronica.findByFechaFin", query = "SELECT s FROM SuscripcionElectronica s WHERE s.fechaFin = :fechaFin")})
public class SuscripcionElectronica implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SuscripcionElectronicaPK suscripcionElectronicaPK;
    @Basic(optional = false)
    @Column(name = "NO_LICENCIAS")
    private int noLicencias;
    @Basic(optional = false)
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumns(
    {@JoinColumn(name = "ID_SUSCRIPCION", referencedColumnName = "ID_SUSCRIPCION", insertable = false, updatable = false)
    ,@JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private Suscripcion suscripcion;
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private String articulo;

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }



    public SuscripcionElectronica() {
    }

    public SuscripcionElectronica(SuscripcionElectronicaPK suscripcionElectronicaPK) {
        this.suscripcionElectronicaPK = suscripcionElectronicaPK;
    }

    public SuscripcionElectronica(SuscripcionElectronicaPK suscripcionElectronicaPK, int noLicencias, Date fechaInicio, Date fechaFin) {
        this.suscripcionElectronicaPK = suscripcionElectronicaPK;
        this.noLicencias = noLicencias;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public SuscripcionElectronica(int idSuscripcion, String idCliente) {
        this.suscripcionElectronicaPK = new SuscripcionElectronicaPK(idSuscripcion, idCliente);
    }

    public SuscripcionElectronicaPK getSuscripcionElectronicaPK() {
        return suscripcionElectronicaPK;
    }

    public void setSuscripcionElectronicaPK(SuscripcionElectronicaPK suscripcionElectronicaPK) {
        this.suscripcionElectronicaPK = suscripcionElectronicaPK;
    }

    public int getNoLicencias() {
        return noLicencias;
    }

    public void setNoLicencias(int noLicencias) {
        this.noLicencias = noLicencias;
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
        hash += (suscripcionElectronicaPK != null ? suscripcionElectronicaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuscripcionElectronica)) {
            return false;
        }
        SuscripcionElectronica other = (SuscripcionElectronica) object;
        if ((this.suscripcionElectronicaPK == null && other.suscripcionElectronicaPK != null) || (this.suscripcionElectronicaPK != null && !this.suscripcionElectronicaPK.equals(other.suscripcionElectronicaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.SuscripcionElectronica[suscripcionElectronicaPK=" + suscripcionElectronicaPK + "]";
    }

}
