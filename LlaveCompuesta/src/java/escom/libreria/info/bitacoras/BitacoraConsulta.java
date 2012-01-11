/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.bitacoras;

import escom.libreria.info.facturacion.Articulo;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "bitacora_consulta")
@NamedQueries({
    @NamedQuery(name = "BitacoraConsulta.findAll", query = "SELECT b FROM BitacoraConsulta b"),
    @NamedQuery(name = "BitacoraConsulta.findByIdMovimiento", query = "SELECT b FROM BitacoraConsulta b WHERE b.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "BitacoraConsulta.findByFecha", query = "SELECT b FROM BitacoraConsulta b WHERE b.fecha = :fecha")})
public class BitacoraConsulta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MOVIMIENTO")
    private Integer idMovimiento;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Articulo articulo;

    public BitacoraConsulta() {
    }

    public BitacoraConsulta(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public BitacoraConsulta(Integer idMovimiento, Date fecha) {
        this.idMovimiento = idMovimiento;
        this.fecha = fecha;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        hash += (idMovimiento != null ? idMovimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraConsulta)) {
            return false;
        }
        BitacoraConsulta other = (BitacoraConsulta) object;
        if ((this.idMovimiento == null && other.idMovimiento != null) || (this.idMovimiento != null && !this.idMovimiento.equals(other.idMovimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "articulo.BitacoraConsulta[idMovimiento=" + idMovimiento + "]";
    }

}
