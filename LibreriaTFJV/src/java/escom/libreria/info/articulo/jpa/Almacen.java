/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "almacen")
@NamedQueries({
    @NamedQuery(name = "Almacen.findAll", query = "SELECT a FROM Almacen a"),
    @NamedQuery(name = "Almacen.findByIdArticulo", query = "SELECT a FROM Almacen a WHERE a.idArticulo = :idArticulo"),
    @NamedQuery(name = "Almacen.findByExistencia", query = "SELECT a FROM Almacen a WHERE a.existencia = :existencia"),
    @NamedQuery(name = "Almacen.findByEnConsigna", query = "SELECT a FROM Almacen a WHERE a.enConsigna = :enConsigna"),
    @NamedQuery(name = "Almacen.findByEnFirme", query = "SELECT a FROM Almacen a WHERE a.enFirme = :enFirme")})
public class Almacen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private Integer idArticulo;
    @Basic(optional = false)
    @Column(name = "EXISTENCIA")
    private boolean existencia;
    @Basic(optional = false)
    @Column(name = "EN_CONSIGNA")
    private boolean enConsigna;
    @Basic(optional = false)
    @Column(name = "EN_FIRME")
    private boolean enFirme;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Articulo articulo;

    public Almacen() {
    }

    public Almacen(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Almacen(Integer idArticulo, boolean existencia, boolean enConsigna, boolean enFirme) {
        this.idArticulo = idArticulo;
        this.existencia = existencia;
        this.enConsigna = enConsigna;
        this.enFirme = enFirme;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public boolean getExistencia() {
        return existencia;
    }

    public void setExistencia(boolean existencia) {
        this.existencia = existencia;
    }

    public boolean getEnConsigna() {
        return enConsigna;
    }

    public void setEnConsigna(boolean enConsigna) {
        this.enConsigna = enConsigna;
    }

    public boolean getEnFirme() {
        return enFirme;
    }

    public void setEnFirme(boolean enFirme) {
        this.enFirme = enFirme;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArticulo != null ? idArticulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.jpa.Almacen[idArticulo=" + idArticulo + "]";
    }

}
