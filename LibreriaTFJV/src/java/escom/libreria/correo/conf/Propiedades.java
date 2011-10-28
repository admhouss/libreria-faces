/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo.conf;

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
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "propiedades")
@NamedQueries({
    @NamedQuery(name = "Propiedades.findAll", query = "SELECT p FROM Propiedades p"),
    @NamedQuery(name = "Propiedades.findById", query = "SELECT p FROM Propiedades p WHERE p.id = :id"),
    @NamedQuery(name = "Propiedades.findByValor", query = "SELECT p FROM Propiedades p WHERE p.valor = :valor"),
    @NamedQuery(name = "Propiedades.findByLlave", query = "SELECT p FROM Propiedades p WHERE p.llave = :llave")})
public class Propiedades implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private String valor;
    @Basic(optional = false)
    @Column(name = "LLAVE")
    private String llave;
    @JoinColumn(name = "SERVIDOR_CORREO_CONF_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ServidorCorreoConf servidorCorreoConfId;

    public Propiedades() {
    }

    public Propiedades(Integer id) {
        this.id = id;
    }

    public Propiedades(Integer id, String valor, String llave) {
        this.id = id;
        this.valor = valor;
        this.llave = llave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public ServidorCorreoConf getServidorCorreoConfId() {
        return servidorCorreoConfId;
    }

    public void setServidorCorreoConfId(ServidorCorreoConf servidorCorreoConfId) {
        this.servidorCorreoConfId = servidorCorreoConfId;
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
        if (!(object instanceof Propiedades)) {
            return false;
        }
        Propiedades other = (Propiedades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.correo.conf.Propiedades[id=" + id + "]";
    }

}
