/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.administrador.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "usurioadministrativo")
@NamedQueries({
    @NamedQuery(name = "Usurioadministrativo.findAll", query = "SELECT u FROM Usurioadministrativo u"),
    @NamedQuery(name = "Usurioadministrativo.findByIdUsuario", query = "SELECT u FROM Usurioadministrativo u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usurioadministrativo.findByNombre", query = "SELECT u FROM Usurioadministrativo u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usurioadministrativo.findByPaterno", query = "SELECT u FROM Usurioadministrativo u WHERE u.paterno = :paterno"),
    @NamedQuery(name = "Usurioadministrativo.findByMaterno", query = "SELECT u FROM Usurioadministrativo u WHERE u.materno = :materno"),
    @NamedQuery(name = "Usurioadministrativo.findByCargo", query = "SELECT u FROM Usurioadministrativo u WHERE u.cargo = :cargo")})
public class Usurioadministrativo implements Serializable {
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
    @Column(name = "CARGO")
    private String cargo;

    public Usurioadministrativo() {
    }

    public Usurioadministrativo(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usurioadministrativo(String idUsuario, String nombre, String paterno, String materno, String cargo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usurioadministrativo)) {
            return false;
        }
        Usurioadministrativo other = (Usurioadministrativo) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.info.administrador.jpa.Usurioadministrativo[idUsuario=" + idUsuario + "]";
    }

}
