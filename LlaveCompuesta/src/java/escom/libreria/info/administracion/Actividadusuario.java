/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.administracion;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
    @NamedQuery(name = "Actividadusuario.findByIdActividad", query = "SELECT a FROM Actividadusuario a WHERE a.actividadusuarioPK.idActividad = :idActividad"),
    @NamedQuery(name = "Actividadusuario.findByIdUsuario", query = "SELECT a FROM Actividadusuario a WHERE a.actividadusuarioPK.idUsuario = :idUsuario"),
    @NamedQuery(name = "Actividadusuario.findByActividad", query = "SELECT a FROM Actividadusuario a WHERE a.actividad = :actividad"),
    @NamedQuery(name = "Actividadusuario.findByFecha", query = "SELECT a FROM Actividadusuario a WHERE a.fecha = :fecha")})
public class Actividadusuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActividadusuarioPK actividadusuarioPK;
    @Basic(optional = false)
    @Column(name = "ACTIVIDAD")
    private String actividad;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Lob
    @Column(name = "QUERY")
    private String query;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarioadministrativo usuarioadministrativo;

    public Actividadusuario() {
    }

    public Actividadusuario(ActividadusuarioPK actividadusuarioPK) {
        this.actividadusuarioPK = actividadusuarioPK;
    }

    public Actividadusuario(ActividadusuarioPK actividadusuarioPK, String actividad, Date fecha, String query) {
        this.actividadusuarioPK = actividadusuarioPK;
        this.actividad = actividad;
        this.fecha = fecha;
        this.query = query;
    }

    public Actividadusuario(int idActividad, String idUsuario) {
        this.actividadusuarioPK = new ActividadusuarioPK(idActividad, idUsuario);
    }

    public ActividadusuarioPK getActividadusuarioPK() {
        return actividadusuarioPK;
    }

    public void setActividadusuarioPK(ActividadusuarioPK actividadusuarioPK) {
        this.actividadusuarioPK = actividadusuarioPK;
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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
        hash += (actividadusuarioPK != null ? actividadusuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividadusuario)) {
            return false;
        }
        Actividadusuario other = (Actividadusuario) object;
        if ((this.actividadusuarioPK == null && other.actividadusuarioPK != null) || (this.actividadusuarioPK != null && !this.actividadusuarioPK.equals(other.actividadusuarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "administracion.Actividadusuario[actividadusuarioPK=" + actividadusuarioPK + "]";
    }

}
