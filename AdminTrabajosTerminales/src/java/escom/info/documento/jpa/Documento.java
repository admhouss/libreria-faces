/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.documento.jpa;

import escom.info.profesor.jpa.ProfesorDocente;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "documento")
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d"),
    @NamedQuery(name = "Documento.findByNoTt", query = "SELECT d FROM Documento d WHERE d.noTt = :noTt"),
    @NamedQuery(name = "Documento.findByTitulo", query = "SELECT d FROM Documento d WHERE d.titulo = :titulo"),
    @NamedQuery(name = "Documento.findByDescripcion", query = "SELECT d FROM Documento d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "Documento.findByFechaCreacion", query = "SELECT d FROM Documento d WHERE d.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Documento.findByUltimaActualizacion", query = "SELECT d FROM Documento d WHERE d.ultimaActualizacion = :ultimaActualizacion")})
public class Documento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "NO_TT")
    private Integer noTt;
    @Basic(optional = false)
    @Column(name = "TITULO")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Lob
    @Column(name = "IMAGEN")
    private byte[] imagen;
    @Basic(optional = false)
    @Lob
    @Column(name = "ARCHIVO_RESUMEN")
    private byte[] archivoResumen;
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Lob
    @Column(name = "ARCHIVO_PROTOCOLO")
    private byte[] archivoProtocolo;
    @Basic(optional = false)
    @Column(name = "ULTIMA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaActualizacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento")
    private List<ProfesorDocente> profesorDocenteList;

    public Documento() {
    }

    public Documento(Integer noTt) {
        this.noTt = noTt;
    }

    public Documento(Integer noTt, String titulo, String descripcion, byte[] imagen, byte[] archivoResumen, Date fechaCreacion, byte[] archivoProtocolo, Date ultimaActualizacion) {
        this.noTt = noTt;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.archivoResumen = archivoResumen;
        this.fechaCreacion = fechaCreacion;
        this.archivoProtocolo = archivoProtocolo;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Integer getNoTt() {
        return noTt;
    }

    public void setNoTt(Integer noTt) {
        this.noTt = noTt;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public byte[] getArchivoResumen() {
        return archivoResumen;
    }

    public void setArchivoResumen(byte[] archivoResumen) {
        this.archivoResumen = archivoResumen;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public byte[] getArchivoProtocolo() {
        return archivoProtocolo;
    }

    public void setArchivoProtocolo(byte[] archivoProtocolo) {
        this.archivoProtocolo = archivoProtocolo;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public List<ProfesorDocente> getProfesorDocenteList() {
        return profesorDocenteList;
    }

    public void setProfesorDocenteList(List<ProfesorDocente> profesorDocenteList) {
        this.profesorDocenteList = profesorDocenteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noTt != null ? noTt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.noTt == null && other.noTt != null) || (this.noTt != null && !this.noTt.equals(other.noTt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.egresado.jpa.Documento[noTt=" + noTt + "]";
    }

}
