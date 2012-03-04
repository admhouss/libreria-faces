/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.profesor.jpa;

import escom.info.documento.jpa.TipoDocente;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "profesor")
@NamedQueries({
    @NamedQuery(name = "Profesor.findAll", query = "SELECT p FROM Profesor p"),
    @NamedQuery(name = "Profesor.findByIdBoleta", query = "SELECT p FROM Profesor p WHERE p.idBoleta = :idBoleta"),
    @NamedQuery(name = "Profesor.findByNombre", query = "SELECT p FROM Profesor p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Profesor.findByCorreo", query = "SELECT p FROM Profesor p WHERE p.correo = :correo"),
    @NamedQuery(name = "Profesor.findByEstatus", query = "SELECT p FROM Profesor p WHERE p.estatus = :estatus"),
    @NamedQuery(name = "Profesor.findByIdDepartamento", query = "SELECT p FROM Profesor p WHERE p.idDepartamento = :idDepartamento")})
public class Profesor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_BOLETA")
    private Integer idBoleta;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "CORREO")
    private String correo;
    @Basic(optional = false)
    @Column(name = "ESTATUS")
    private short estatus;
    @Basic(optional = false)
    @Column(name = "ID_DEPARTAMENTO")
    private int idDepartamento;
    @JoinColumn(name = "TIPO_DOCENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoDocente tipoDocente;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "profesor")
    private ProfesorDocente profesorDocente;

    public Profesor() {
    }

    public Profesor(Integer idBoleta) {
        this.idBoleta = idBoleta;
    }

    public Profesor(Integer idBoleta, String nombre, String correo, short estatus, int idDepartamento) {
        this.idBoleta = idBoleta;
        this.nombre = nombre;
        this.correo = correo;
        this.estatus = estatus;
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(Integer idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public short getEstatus() {
        return estatus;
    }

    public void setEstatus(short estatus) {
        this.estatus = estatus;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public TipoDocente getTipoDocente() {
        return tipoDocente;
    }

    public void setTipoDocente(TipoDocente tipoDocente) {
        this.tipoDocente = tipoDocente;
    }

    public ProfesorDocente getProfesorDocente() {
        return profesorDocente;
    }

    public void setProfesorDocente(ProfesorDocente profesorDocente) {
        this.profesorDocente = profesorDocente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBoleta != null ? idBoleta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profesor)) {
            return false;
        }
        Profesor other = (Profesor) object;
        if ((this.idBoleta == null && other.idBoleta != null) || (this.idBoleta != null && !this.idBoleta.equals(other.idBoleta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.egresado.jpa.Profesor[idBoleta=" + idBoleta + "]";
    }

}
