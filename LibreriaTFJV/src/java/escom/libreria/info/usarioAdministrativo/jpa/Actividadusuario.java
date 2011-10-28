/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.usarioAdministrativo.jpa;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "actividadusuario")
@NamedQueries({
    @NamedQuery(name = "Actividadusuario.findAll", query = "SELECT a FROM Actividadusuario a"),
    @NamedQuery(name = "Actividadusuario.findByIdActividad", query = "SELECT a FROM Actividadusuario a WHERE a.idActividad = :idActividad"),
    @NamedQuery(name = "Actividadusuario.findByActividad", query = "SELECT a FROM Actividadusuario a WHERE a.actividad = :actividad"),
    @NamedQuery(name = "Actividadusuario.findByFecha", query = "SELECT a FROM Actividadusuario a WHERE a.fecha = :fecha")})
public class Actividadusuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ACTIVIDAD")
    private Integer idActividad;
    @Basic(optional = false)
    @Column(name = "ACTIVIDAD")
    private String actividad;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Usuarioadministrativo usuarioadministrativo;
    @Basic(optional = false)
    @Column(name = "QUERY")
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    

    public Actividadusuario() {
    }

    public Actividadusuario(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public Actividadusuario(Integer idActividad, String actividad, Date fecha) {
        this.idActividad = idActividad;
        this.actividad = actividad;
        this.fecha = fecha;
    }

    public Integer getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuarioadministrativo getUsuarioadministrativo() {
        return usuarioadministrativo;
    }

    public void setUsuarioadministrativo(Usuarioadministrativo usuarioadministrativo) {
        this.usuarioadministrativo = usuarioadministrativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActividad != null ? idActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividadusuario)) {
            return false;
        }
        Actividadusuario other = (Actividadusuario) object;
        if ((this.idActividad == null && other.idActividad != null) || (this.idActividad != null && !this.idActividad.equals(other.idActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.usarioAdministrativo.jpa.Actividadusuario[idActividad=" + idActividad + "]";
    }

}
