/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo.conf;

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
@Table(name = "servidor_correo_conf")
@NamedQueries({
    @NamedQuery(name = "ServidorCorreoConf.findAll", query = "SELECT s FROM ServidorCorreoConf s"),
    @NamedQuery(name = "ServidorCorreoConf.findById", query = "SELECT s FROM ServidorCorreoConf s WHERE s.id = :id"),
    @NamedQuery(name = "ServidorCorreoConf.findByUsuario", query = "SELECT s FROM ServidorCorreoConf s WHERE s.usuario = :usuario"),
    @NamedQuery(name = "ServidorCorreoConf.findByDescripcion", query = "SELECT s FROM ServidorCorreoConf s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "ServidorCorreoConf.findByContrasenia", query = "SELECT s FROM ServidorCorreoConf s WHERE s.contrasenia = :contrasenia")})
public class ServidorCorreoConf implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "CONTRASENIA")
    private String contrasenia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servidorCorreoConfId")
    private List<Propiedades> propiedadesList;

    public ServidorCorreoConf() {
    }

    public ServidorCorreoConf(Integer id) {
        this.id = id;
    }

    public ServidorCorreoConf(Integer id, String descripcion, String contrasenia) {
        this.id = id;
        this.descripcion = descripcion;
        this.contrasenia = contrasenia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public List<Propiedades> getPropiedadesList() {
        return propiedadesList;
    }

    public void setPropiedadesList(List<Propiedades> propiedadesList) {
        this.propiedadesList = propiedadesList;
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
        if (!(object instanceof ServidorCorreoConf)) {
            return false;
        }
        ServidorCorreoConf other = (ServidorCorreoConf) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.correo.conf.ServidorCorreoConf[id=" + id + "]";
    }

}
