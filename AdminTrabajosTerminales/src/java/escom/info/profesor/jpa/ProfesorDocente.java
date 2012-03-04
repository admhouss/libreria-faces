/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.profesor.jpa;

import escom.info.documento.jpa.Documento;
import escom.info.egresado.jpa.Egresado;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "profesor_docente")
@NamedQueries({
    @NamedQuery(name = "ProfesorDocente.findAll", query = "SELECT p FROM ProfesorDocente p"),
    @NamedQuery(name = "ProfesorDocente.findByIdProfesor", query = "SELECT p FROM ProfesorDocente p WHERE p.idProfesor = :idProfesor")})
public class ProfesorDocente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PROFESOR")
    private Integer idProfesor;
    @JoinColumn(name = "ID_PROFESOR", referencedColumnName = "ID_BOLETA", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Profesor profesor;
    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "NO_TT")
    @ManyToOne(optional = false)
    private Documento documento;
    @JoinColumn(name = "ID_EGRESADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Egresado egresado;

    public ProfesorDocente() {
    }

    public ProfesorDocente(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Egresado getEgresado() {
        return egresado;
    }

    public void setEgresado(Egresado egresado) {
        this.egresado = egresado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProfesor != null ? idProfesor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfesorDocente)) {
            return false;
        }
        ProfesorDocente other = (ProfesorDocente) object;
        if ((this.idProfesor == null && other.idProfesor != null) || (this.idProfesor != null && !this.idProfesor.equals(other.idProfesor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.egresado.jpa.ProfesorDocente[idProfesor=" + idProfesor + "]";
    }

}
