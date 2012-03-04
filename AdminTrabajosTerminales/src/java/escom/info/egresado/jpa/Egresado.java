/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.egresado.jpa;

import escom.info.profesor.jpa.ProfesorDocente;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "egresado")
@NamedQueries({
    @NamedQuery(name = "Egresado.findAll", query = "SELECT e FROM Egresado e"),
    @NamedQuery(name = "Egresado.findById", query = "SELECT e FROM Egresado e WHERE e.id = :id"),
    @NamedQuery(name = "Egresado.findByNombre", query = "SELECT e FROM Egresado e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Egresado.findByCorreo", query = "SELECT e FROM Egresado e WHERE e.correo = :correo")})
public class Egresado implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "egresado")
    private List<ProfesorDocente> profesorDocenteList;
    @JoinColumn(name = "ID_GENERACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Generacion generacion;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "CORREO")
    private String correo;
    

    public Egresado() {
    }

    public Egresado(Integer id) {
        this.id = id;
    }

    public Egresado(Integer id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Generacion getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generacion generacion) {
        this.generacion = generacion;
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
        if (!(object instanceof Egresado)) {
            return false;
        }
        Egresado other = (Egresado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.egresado.jpa.Egresado[id=" + id + "]";
    }

    public List<ProfesorDocente> getProfesorDocenteList() {
        return profesorDocenteList;
    }

    public void setProfesorDocenteList(List<ProfesorDocente> profesorDocenteList) {
        this.profesorDocenteList = profesorDocenteList;
    }

    

}
