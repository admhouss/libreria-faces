/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.documento.jpa;

import escom.info.profesor.jpa.Profesor;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "tipo_docente")
@NamedQueries({
    @NamedQuery(name = "TipoDocente.findAll", query = "SELECT t FROM TipoDocente t"),
    @NamedQuery(name = "TipoDocente.findById", query = "SELECT t FROM TipoDocente t WHERE t.id = :id"),
    @NamedQuery(name = "TipoDocente.findByNombre", query = "SELECT t FROM TipoDocente t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoDocente.findByDescripcion", query = "SELECT t FROM TipoDocente t WHERE t.descripcion = :descripcion")})
public class TipoDocente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocente")
    private List<Profesor> profesorList;

    public TipoDocente() {
    }

    public TipoDocente(Integer id) {
        this.id = id;
    }

    public TipoDocente(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Profesor> getProfesorList() {
        return profesorList;
    }

    public void setProfesorList(List<Profesor> profesorList) {
        this.profesorList = profesorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDocente)) {
            return false;
        }
        TipoDocente other = (TipoDocente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.egresado.jpa.TipoDocente[id=" + id + "]";
    }

}
