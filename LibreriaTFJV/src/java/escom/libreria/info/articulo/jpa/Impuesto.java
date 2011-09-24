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
@Table(name = "impuesto")
@NamedQueries({
    @NamedQuery(name = "Impuesto.findAll", query = "SELECT i FROM Impuesto i"),
    @NamedQuery(name = "Impuesto.findByIdArticulo", query = "SELECT i FROM Impuesto i WHERE i.idArticulo = :idArticulo"),
    @NamedQuery(name = "Impuesto.findByMontoImpuesto", query = "SELECT i FROM Impuesto i WHERE i.montoImpuesto = :montoImpuesto"),
    @NamedQuery(name = "Impuesto.findByDescripcion", query = "SELECT i FROM Impuesto i WHERE i.descripcion = :descripcion")})
public class Impuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private Integer idArticulo;
    @Basic(optional = false)
    @Column(name = "MONTO_IMPUESTO")
    private BigDecimal montoImpuesto;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Articulo articulo;

    public Impuesto() {
    }

    public Impuesto(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Impuesto(Integer idArticulo, BigDecimal montoImpuesto, String descripcion) {
        this.idArticulo = idArticulo;
        this.montoImpuesto = montoImpuesto;
        this.descripcion = descripcion;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public BigDecimal getMontoImpuesto() {
        return montoImpuesto;
    }

    public void setMontoImpuesto(BigDecimal montoImpuesto) {
        this.montoImpuesto = montoImpuesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Impuesto)) {
            return false;
        }
        Impuesto other = (Impuesto) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.jpa.Impuesto[idArticulo=" + idArticulo + "]";
    }

}
