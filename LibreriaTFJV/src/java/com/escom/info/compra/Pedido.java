/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra;

import escom.libreria.info.articulo.jpa.Articulo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "pedido")
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByIdPedido", query = "SELECT p FROM Pedido p WHERE p.pedidoPK.idPedido = :idPedido"),
    @NamedQuery(name = "Pedido.findByIdArticulo", query = "SELECT p FROM Pedido p WHERE p.pedidoPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "Pedido.findByNoArticuloCategoria", query = "SELECT p FROM Pedido p WHERE p.noArticuloCategoria = :noArticuloCategoria"),
    @NamedQuery(name = "Pedido.findByCategoria", query = "SELECT p FROM Pedido p WHERE p.categoria = :categoria"),
    @NamedQuery(name = "Pedido.findByPrecioNeto", query = "SELECT p FROM Pedido p WHERE p.precioNeto = :precioNeto"),
    @NamedQuery(name = "Pedido.findByImpuesto", query = "SELECT p FROM Pedido p WHERE p.impuesto = :impuesto"),
    @NamedQuery(name = "Pedido.findByDescuento", query = "SELECT p FROM Pedido p WHERE p.descuento = :descuento"),
    @NamedQuery(name = "Pedido.findByPrecioTotal", query = "SELECT p FROM Pedido p WHERE p.precioTotal = :precioTotal"),
    @NamedQuery(name = "Pedido.findByTipoEnvio", query = "SELECT p FROM Pedido p WHERE p.tipoEnvio = :tipoEnvio")})
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidoPK pedidoPK;
    @Basic(optional = false)
    @Column(name = "NO_ARTICULO_CATEGORIA")
    private int noArticuloCategoria;
    @Basic(optional = false)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Basic(optional = false)
    @Column(name = "PRECIO_NETO")
    private BigDecimal precioNeto;
    @Basic(optional = false)
    @Column(name = "IMPUESTO")
    private BigDecimal impuesto;
    @Basic(optional = false)
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    @Basic(optional = false)
    @Column(name = "PRECIO_TOTAL")
    private BigDecimal precioTotal;
    @Basic(optional = false)
    @Column(name = "TIPO_ENVIO")
    private String tipoEnvio;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<Compra> compraList;*/
    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "pedido")
    //private Enviorealizado enviorealizado;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    //private List<EnvioFisico> envioFisicoList;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;

    public Pedido() {
    }

    public Pedido(PedidoPK pedidoPK) {
        this.pedidoPK = pedidoPK;
    }

    public Pedido(PedidoPK pedidoPK, int noArticuloCategoria, String categoria, BigDecimal precioNeto, BigDecimal impuesto, BigDecimal descuento, BigDecimal precioTotal, String tipoEnvio) {
        this.pedidoPK = pedidoPK;
        this.noArticuloCategoria = noArticuloCategoria;
        this.categoria = categoria;
        this.precioNeto = precioNeto;
        this.impuesto = impuesto;
        this.descuento = descuento;
        this.precioTotal = precioTotal;
        this.tipoEnvio = tipoEnvio;
    }

    public Pedido(int idPedido, int idArticulo) {
        this.pedidoPK = new PedidoPK(idPedido, idArticulo);
    }

    public PedidoPK getPedidoPK() {
        return pedidoPK;
    }

    public void setPedidoPK(PedidoPK pedidoPK) {
        this.pedidoPK = pedidoPK;
    }

    public int getNoArticuloCategoria() {
        return noArticuloCategoria;
    }

    public void setNoArticuloCategoria(int noArticuloCategoria) {
        this.noArticuloCategoria = noArticuloCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecioNeto() {
        return precioNeto;
    }

    public void setPrecioNeto(BigDecimal precioNeto) {
        this.precioNeto = precioNeto;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }
/*
    public List<Compra> getCompraList() {
        return compraList;
    }

    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
    }*/

  /*  public Enviorealizado getEnviorealizado() {
        return enviorealizado;
    }

    public void setEnviorealizado(Enviorealizado enviorealizado) {
        this.enviorealizado = enviorealizado;
    }*/

/*    public List<EnvioFisico> getEnvioFisicoList() {
        return envioFisicoList;
    }

    public void setEnvioFisicoList(List<EnvioFisico> envioFisicoList) {
        this.envioFisicoList = envioFisicoList;
    }
*/
    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidoPK != null ? pedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.pedidoPK == null && other.pedidoPK != null) || (this.pedidoPK != null && !this.pedidoPK.equals(other.pedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.escom.info.compra.Pedido[pedidoPK=" + pedidoPK + "]";
    }

}
