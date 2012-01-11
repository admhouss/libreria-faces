/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import escom.libreria.info.facturacion.Articulo;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "envioelectronico")
@NamedQueries({
    @NamedQuery(name = "Envioelectronico.findAll", query = "SELECT e FROM Envioelectronico e"),
    @NamedQuery(name = "Envioelectronico.findByIdPedido", query = "SELECT e FROM Envioelectronico e WHERE e.envioelectronicoPK.idPedido = :idPedido"),
    @NamedQuery(name = "Envioelectronico.findByIdArticulo", query = "SELECT e FROM Envioelectronico e WHERE e.envioelectronicoPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "Envioelectronico.findByLigaDescarga", query = "SELECT e FROM Envioelectronico e WHERE e.ligaDescarga = :ligaDescarga"),
    @NamedQuery(name = "Envioelectronico.findByObservaciones", query = "SELECT e FROM Envioelectronico e WHERE e.observaciones = :observaciones")})
public class Envioelectronico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EnvioelectronicoPK envioelectronicoPK;
    @Basic(optional = false)
    @Column(name = "LIGA_DESCARGA")
    private String ligaDescarga;
    @Basic(optional = false)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @JoinColumns({
        @JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID_PEDIDO", insertable = false, updatable = false),
        @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;

    public Envioelectronico() {
    }

    public Envioelectronico(EnvioelectronicoPK envioelectronicoPK) {
        this.envioelectronicoPK = envioelectronicoPK;
    }

    public Envioelectronico(EnvioelectronicoPK envioelectronicoPK, String ligaDescarga, String observaciones) {
        this.envioelectronicoPK = envioelectronicoPK;
        this.ligaDescarga = ligaDescarga;
        this.observaciones = observaciones;
    }

    public Envioelectronico(int idPedido, int idArticulo) {
        this.envioelectronicoPK = new EnvioelectronicoPK(idPedido, idArticulo);
    }

    public EnvioelectronicoPK getEnvioelectronicoPK() {
        return envioelectronicoPK;
    }

    public void setEnvioelectronicoPK(EnvioelectronicoPK envioelectronicoPK) {
        this.envioelectronicoPK = envioelectronicoPK;
    }

    public String getLigaDescarga() {
        return ligaDescarga;
    }

    public void setLigaDescarga(String ligaDescarga) {
        this.ligaDescarga = ligaDescarga;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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
        hash += (envioelectronicoPK != null ? envioelectronicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Envioelectronico)) {
            return false;
        }
        Envioelectronico other = (Envioelectronico) object;
        if ((this.envioelectronicoPK == null && other.envioelectronicoPK != null) || (this.envioelectronicoPK != null && !this.envioelectronicoPK.equals(other.envioelectronicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.Envioelectronico[envioelectronicoPK=" + envioelectronicoPK + "]";
    }

}
