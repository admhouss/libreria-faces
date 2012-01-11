/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.servidorConfiguracion;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "mime")
@NamedQueries({
    @NamedQuery(name = "Mime.findAll", query = "SELECT m FROM Mime m"),
    @NamedQuery(name = "Mime.findById", query = "SELECT m FROM Mime m WHERE m.id = :id"),
    @NamedQuery(name = "Mime.findByDescripcion", query = "SELECT m FROM Mime m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Mime.findByExtension", query = "SELECT m FROM Mime m WHERE m.extension = :extension"),
    @NamedQuery(name = "Mime.findByMimeType", query = "SELECT m FROM Mime m WHERE m.mimeType = :mimeType")})
public class Mime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "EXTENSION")
    private String extension;
    @Basic(optional = false)
    @Column(name = "MIME_TYPE")
    private String mimeType;

    public Mime() {
    }

    public Mime(Integer id) {
        this.id = id;
    }

    public Mime(Integer id, String descripcion, String extension, String mimeType) {
        this.id = id;
        this.descripcion = descripcion;
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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
        if (!(object instanceof Mime)) {
            return false;
        }
        Mime other = (Mime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "administracion.Mime[id=" + id + "]";
    }

}
