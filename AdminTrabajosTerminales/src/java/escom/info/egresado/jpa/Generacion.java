/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.egresado.jpa;

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
@Table(name = "generacion")
@NamedQueries({
    @NamedQuery(name = "Generacion.findAll", query = "SELECT g FROM Generacion g"),
    @NamedQuery(name = "Generacion.findById", query = "SELECT g FROM Generacion g WHERE g.id = :id"),
    @NamedQuery(name = "Generacion.findByAnio", query = "SELECT g FROM Generacion g WHERE g.anio = :anio"),
    @NamedQuery(name = "Generacion.findByDescripcion", query = "SELECT g FROM Generacion g WHERE g.descripcion = :descripcion")})
public class Generacion implements Serializable {
   
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ANIO")
    private String anio;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion")
    private List<Egresado> egresadoList;

    public Generacion() {
    }

    public Generacion(Integer id) {
        this.id = id;
    }

    public Generacion(Integer id, String anio, String descripcion) {
        this.id = id;
        this.anio = anio;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Egresado> getEgresadoList() {
        return egresadoList;
    }

    public void setEgresadoList(List<Egresado> egresadoList) {
        this.egresadoList = egresadoList;
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
        if (!(object instanceof Generacion)) {
            return false;
        }
        Generacion other = (Generacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.egresado.jpa.Generacion[id=" + id + "]";
    }

   

}
