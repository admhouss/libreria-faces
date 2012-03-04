/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.departamento.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "responsable")
@NamedQueries({
    @NamedQuery(name = "Responsable.findAll", query = "SELECT r FROM Responsable r"),
    @NamedQuery(name = "Responsable.findByIdResponsable", query = "SELECT r FROM Responsable r WHERE r.idResponsable = :idResponsable"),
    @NamedQuery(name = "Responsable.findByNombre", query = "SELECT r FROM Responsable r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Responsable.findByFechaModificacion", query = "SELECT r FROM Responsable r WHERE r.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Responsable.findByEstatus", query = "SELECT r FROM Responsable r WHERE r.estatus = :estatus"),
    @NamedQuery(name = "Responsable.findByCorreo", query = "SELECT r FROM Responsable r WHERE r.correo = :correo"),
    @NamedQuery(name = "Responsable.findByCargo", query = "SELECT r FROM Responsable r WHERE r.cargo = :cargo"),
    @NamedQuery(name = "Responsable.findByFechaAlta", query = "SELECT r FROM Responsable r WHERE r.fechaAlta = :fechaAlta")})
public class Responsable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_RESPONSABLE")
    private Integer idResponsable;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(name = "ESTATUS")
    private short estatus;
    @Basic(optional = false)
    @Column(name = "CORREO")
    private String correo;
    @Basic(optional = false)
    @Column(name = "CARGO")
    private String cargo;
    @Basic(optional = false)
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "responsable")
    private Departamento departamento;

    public Responsable() {
    }

    public Responsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }

    public Responsable(Integer idResponsable, String nombre, Date fechaModificacion, short estatus, String correo, String cargo, Date fechaAlta) {
        this.idResponsable = idResponsable;
        this.nombre = nombre;
        this.fechaModificacion = fechaModificacion;
        this.estatus = estatus;
        this.correo = correo;
        this.cargo = cargo;
        this.fechaAlta = fechaAlta;
    }

    public Integer getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public short getEstatus() {
        return estatus;
    }

    public void setEstatus(short estatus) {
        this.estatus = estatus;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idResponsable != null ? idResponsable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Responsable)) {
            return false;
        }
        Responsable other = (Responsable) object;
        if ((this.idResponsable == null && other.idResponsable != null) || (this.idResponsable != null && !this.idResponsable.equals(other.idResponsable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.departamento.jpa.Responsable[idResponsable=" + idResponsable + "]";
    }

}
