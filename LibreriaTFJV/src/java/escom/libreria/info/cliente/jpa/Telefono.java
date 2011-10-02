/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "telefono")
@NamedQueries({
    @NamedQuery(name = "Telefono.findAll", query = "SELECT t FROM Telefono t"),
    @NamedQuery(name = "Telefono.findById", query = "SELECT t FROM Telefono t WHERE t.telefonoPK.id = :id"),
    @NamedQuery(name = "Telefono.findByTelefono", query = "SELECT t FROM Telefono t WHERE t.telefono = :telefono"),
    @NamedQuery(name = "Telefono.findByDescripcion", query = "SELECT t FROM Telefono t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Telefono.findByIdCliente", query = "SELECT t FROM Telefono t WHERE t.telefonoPK.idCliente = :idCliente")})
public class Telefono implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TelefonoPK telefonoPK;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public Telefono() {
    }

    public Telefono(TelefonoPK telefonoPK) {
        this.telefonoPK = telefonoPK;
    }

    public Telefono(int id, String idCliente) {
        this.telefonoPK = new TelefonoPK(id, idCliente);
    }

    public TelefonoPK getTelefonoPK() {
        return telefonoPK;
    }

    public void setTelefonoPK(TelefonoPK telefonoPK) {
        this.telefonoPK = telefonoPK;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telefonoPK != null ? telefonoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Telefono)) {
            return false;
        }
        Telefono other = (Telefono) object;
        if ((this.telefonoPK == null && other.telefonoPK != null) || (this.telefonoPK != null && !this.telefonoPK.equals(other.telefonoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.cliente.jpa.Telefono[telefonoPK=" + telefonoPK + "]";
    }

}
