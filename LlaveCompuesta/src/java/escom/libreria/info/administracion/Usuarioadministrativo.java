/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.administracion;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "usuarioadministrativo")
@NamedQueries({
    @NamedQuery(name = "Usuarioadministrativo.findAll", query = "SELECT u FROM Usuarioadministrativo u"),
    @NamedQuery(name = "Usuarioadministrativo.findByIdUsuario", query = "SELECT u FROM Usuarioadministrativo u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuarioadministrativo.findByNombre", query = "SELECT u FROM Usuarioadministrativo u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuarioadministrativo.findByPaterno", query = "SELECT u FROM Usuarioadministrativo u WHERE u.paterno = :paterno"),
    @NamedQuery(name = "Usuarioadministrativo.findByMaterno", query = "SELECT u FROM Usuarioadministrativo u WHERE u.materno = :materno"),
    @NamedQuery(name = "Usuarioadministrativo.findByPassword", query = "SELECT u FROM Usuarioadministrativo u WHERE u.password = :password"),
    @NamedQuery(name = "Usuarioadministrativo.findByCargo", query = "SELECT u FROM Usuarioadministrativo u WHERE u.cargo = :cargo")})
public class Usuarioadministrativo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private String idUsuario;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "PATERNO")
    private String paterno;
    @Basic(optional = false)
    @Column(name = "MATERNO")
    private String materno;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "CARGO")
    private String cargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioadministrativo")
    private List<Actividadusuario> actividadusuarioList;

    public Usuarioadministrativo() {
    }

    public Usuarioadministrativo(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuarioadministrativo(String idUsuario, String nombre, String paterno, String materno, String password, String cargo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.password = password;
        this.cargo = cargo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public List<Actividadusuario> getActividadusuarioList() {
        return actividadusuarioList;
    }

    public void setActividadusuarioList(List<Actividadusuario> actividadusuarioList) {
        this.actividadusuarioList = actividadusuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method wot work in the case the id fields are not set
        if (!(object instanceof Usuarioadministrativo)) {
            return false;
        }
        Usuarioadministrativo other = (Usuarioadministrativo) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "administracion.Usuarioadministrativo[idUsuario=" + idUsuario + "]";
    }

}
