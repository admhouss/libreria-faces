/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.descuentos;

import escom.libreria.info.facturacion.Articulo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "descuento_articulo")
@NamedQueries({
    @NamedQuery(name = "DescuentoArticulo.findAll", query = "SELECT d FROM DescuentoArticulo d"),
    @NamedQuery(name = "DescuentoArticulo.findByIdArticulo", query = "SELECT d FROM DescuentoArticulo d WHERE d.idArticulo = :idArticulo"),
    @NamedQuery(name = "DescuentoArticulo.findByDescuento", query = "SELECT d FROM DescuentoArticulo d WHERE d.descuento = :descuento"),
    @NamedQuery(name = "DescuentoArticulo.findByFechaInicio", query = "SELECT d FROM DescuentoArticulo d WHERE d.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "DescuentoArticulo.findByFechaFinal", query = "SELECT d FROM DescuentoArticulo d WHERE d.fechaFinal = :fechaFinal")})
public class DescuentoArticulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private Integer idArticulo;
    @Basic(optional = false)
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Basic(optional = false)
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "FECHA_FINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Articulo articulo;

    public DescuentoArticulo() {
    }

    public DescuentoArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public DescuentoArticulo(Integer idArticulo, BigDecimal descuento, Date fechaInicio, Date fechaFinal) {
        this.idArticulo = idArticulo;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
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
        return "articulo.DescuentoArticulo[idArticulo=" + idArticulo + "]";
    }

}
