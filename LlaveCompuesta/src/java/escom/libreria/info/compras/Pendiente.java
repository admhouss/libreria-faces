/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.cliente.Cliente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "pendiente")
@NamedQueries({
    @NamedQuery(name = "Pendiente.findAll", query = "SELECT p FROM Pendiente p"),
    @NamedQuery(name = "Pendiente.findByIdArticulo", query = "SELECT p FROM Pendiente p WHERE p.idArticulo = :idArticulo"),
    @NamedQuery(name = "Pendiente.findByNoArtSolicitados", query = "SELECT p FROM Pendiente p WHERE p.noArtSolicitados = :noArtSolicitados"),
    @NamedQuery(name = "Pendiente.findByFecha", query = "SELECT p FROM Pendiente p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Pendiente.findByObservacones", query = "SELECT p FROM Pendiente p WHERE p.observacones = :observacones")})
public class Pendiente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private Integer idArticulo;
    @Basic(optional = false)
    @Column(name = "NO_ART_SOLICITADOS")
    private int noArtSolicitados;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "OBSERVACONES")
    private String observacones;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Articulo articulo;

    public Pendiente() {
    }

    public Pendiente(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Pendiente(Integer idArticulo, int noArtSolicitados, Date fecha, String observacones) {
        this.idArticulo = idArticulo;
        this.noArtSolicitados = noArtSolicitados;
        this.fecha = fecha;
        this.observacones = observacones;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getNoArtSolicitados() {
        return noArtSolicitados;
    }

    public void setNoArtSolicitados(int noArtSolicitados) {
        this.noArtSolicitados = noArtSolicitados;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacones() {
        return observacones;
    }

    public void setObservacones(String observacones) {
        this.observacones = observacones;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        if (!(object instanceof Pendiente)) {
            return false;
        }
        Pendiente other = (Pendiente) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.Pendiente[idArticulo=" + idArticulo + "]";
    }

}
