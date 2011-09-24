/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "descuento_articulo")
@NamedQueries({
    @NamedQuery(name = "DescuentoArticulo.findAll", query = "SELECT d FROM DescuentoArticulo d"),
    @NamedQuery(name = "DescuentoArticulo.findByIdArticulo", query = "SELECT d FROM DescuentoArticulo d WHERE d.idArticulo = :idArticulo"),
    @NamedQuery(name = "DescuentoArticulo.findByDescuento", query = "SELECT d FROM DescuentoArticulo d WHERE d.descuento = :descuento")})
public class DescuentoArticulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private Integer idArticulo;
    @Basic(optional = false)
    @Column(name = "descuento")
    private BigDecimal descuento;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Articulo articulo;

    public DescuentoArticulo() {
    }

    public DescuentoArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public DescuentoArticulo(Integer idArticulo, BigDecimal descuento) {
        this.idArticulo = idArticulo;
        this.descuento = descuento;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
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
        if (!(object instanceof DescuentoArticulo)) {
            return false;
        }
        DescuentoArticulo other = (DescuentoArticulo) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.jpa.DescuentoArticulo[idArticulo=" + idArticulo + "]";
    }

}
