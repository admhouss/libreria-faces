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
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "envio_fisico")
@NamedQueries({
    @NamedQuery(name = "EnvioFisico.findAll", query = "SELECT e FROM EnvioFisico e"),
    @NamedQuery(name = "EnvioFisico.findByIdPedido", query = "SELECT e FROM EnvioFisico e WHERE e.envioFisicoPK.idPedido = :idPedido"),
    @NamedQuery(name = "EnvioFisico.findByIdArticulo", query = "SELECT e FROM EnvioFisico e WHERE e.envioFisicoPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "EnvioFisico.findByNoGuia", query = "SELECT e FROM EnvioFisico e WHERE e.noGuia = :noGuia"),
    @NamedQuery(name = "EnvioFisico.findByPaqueteria", query = "SELECT e FROM EnvioFisico e WHERE e.paqueteria = :paqueteria"),
    @NamedQuery(name = "EnvioFisico.findByObservaciones", query = "SELECT e FROM EnvioFisico e WHERE e.observaciones = :observaciones")})
public class EnvioFisico implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EnvioFisicoPK envioFisicoPK;
    @Basic(optional = false)
    @Column(name = "NO_GUIA")
    private String noGuia;
    @Basic(optional = false)
    @Column(name = "PAQUETERIA")
    private String paqueteria;
    @Basic(optional = false)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
     @JoinColumns(
    {
    @JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID_PEDIDO", insertable = false, updatable = false),
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "ID_DIR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Direnvio direnvio;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;

    public EnvioFisico() {
    }

    public EnvioFisico(EnvioFisicoPK envioFisicoPK) {
        this.envioFisicoPK = envioFisicoPK;
    }

    public EnvioFisico(EnvioFisicoPK envioFisicoPK, String noGuia, String paqueteria, String observaciones) {
        this.envioFisicoPK = envioFisicoPK;
        this.noGuia = noGuia;
        this.paqueteria = paqueteria;
        this.observaciones = observaciones;
    }

    public EnvioFisico(int idPedido, int idArticulo) {
        this.envioFisicoPK = new EnvioFisicoPK(idPedido, idArticulo);
    }

    public EnvioFisicoPK getEnvioFisicoPK() {
        return envioFisicoPK;
    }

    public void setEnvioFisicoPK(EnvioFisicoPK envioFisicoPK) {
        this.envioFisicoPK = envioFisicoPK;
    }

    public String getNoGuia() {
        return noGuia;
    }

    public void setNoGuia(String noGuia) {
        this.noGuia = noGuia;
    }

    public String getPaqueteria() {
        return paqueteria;
    }

    public void setPaqueteria(String paqueteria) {
        this.paqueteria = paqueteria;
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

    public Direnvio getDirenvio() {
        return direnvio;
    }

    public void setDirenvio(Direnvio direnvio) {
        this.direnvio = direnvio;
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
        hash += (envioFisicoPK != null ? envioFisicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnvioFisico)) {
            return false;
        }
        EnvioFisico other = (EnvioFisico) object;
        if ((this.envioFisicoPK == null && other.envioFisicoPK != null) || (this.envioFisicoPK != null && !this.envioFisicoPK.equals(other.envioFisicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.EnvioFisico[envioFisicoPK=" + envioFisicoPK + "]";
    }

}
