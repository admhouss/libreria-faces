/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo;

import escom.libreria.info.facturacion.Articulo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "promocion")
@NamedQueries({
    @NamedQuery(name = "Promocion.findAll", query = "SELECT p FROM Promocion p"),
    @NamedQuery(name = "Promocion.findById", query = "SELECT p FROM Promocion p WHERE p.promocionPK.id = :id"),
    @NamedQuery(name = "Promocion.findByIdArticulo", query = "SELECT p FROM Promocion p WHERE p.promocionPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "Promocion.findByPrecioPublico", query = "SELECT p FROM Promocion p WHERE p.precioPublico = :precioPublico"),
    @NamedQuery(name = "Promocion.findByDiaInicio", query = "SELECT p FROM Promocion p WHERE p.diaInicio = :diaInicio"),
    @NamedQuery(name = "Promocion.findByDiaFin", query = "SELECT p FROM Promocion p WHERE p.diaFin = :diaFin")})
public class Promocion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PromocionPK promocionPK;
    @Basic(optional = false)
    @Column(name = "PRECIO_PUBLICO")
    private BigDecimal precioPublico;
    @Basic(optional = false)
    @Column(name = "DIA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaInicio;
    @Basic(optional = false)
    @Column(name = "DIA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaFin;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;

    public Promocion() {
    }

    public Promocion(PromocionPK promocionPK) {
        this.promocionPK = promocionPK;
    }

    public Promocion(PromocionPK promocionPK, BigDecimal precioPublico, Date diaInicio, Date diaFin) {
        this.promocionPK = promocionPK;
        this.precioPublico = precioPublico;
        this.diaInicio = diaInicio;
        this.diaFin = diaFin;
    }

    public Promocion(int id, int idArticulo) {
        this.promocionPK = new PromocionPK(id, idArticulo);
    }

    public PromocionPK getPromocionPK() {
        return promocionPK;
    }

    public void setPromocionPK(PromocionPK promocionPK) {
        this.promocionPK = promocionPK;
    }

    public BigDecimal getPrecioPublico() {
        return precioPublico;
    }

    public void setPrecioPublico(BigDecimal precioPublico) {
        this.precioPublico = precioPublico;
    }

    public Date getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(Date diaInicio) {
        this.diaInicio = diaInicio;
    }

    public Date getDiaFin() {
        return diaFin;
    }

    public void setDiaFin(Date diaFin) {
        this.diaFin = diaFin;
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
        hash += (promocionPK != null ? promocionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Promocion)) {
            return false;
        }
        Promocion other = (Promocion) object;
        if ((this.promocionPK == null && other.promocionPK != null) || (this.promocionPK != null && !this.promocionPK.equals(other.promocionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "articulo.Promocion[promocionPK=" + promocionPK + "]";
    }

}
