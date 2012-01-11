/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;


import escom.libreria.info.articulo.AlmacenPedido;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.cliente.Cliente;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
    @NamedQuery(name = "Pedido.findByTipoEnvio", query = "SELECT p FROM Pedido p WHERE p.tipoEnvio = :tipoEnvio"),
    @NamedQuery(name = "Pedido.findByFechaPedido", query = "SELECT p FROM Pedido p WHERE p.fechaPedido = :fechaPedido"),
    @NamedQuery(name = "Pedido.findByEstado", query = "SELECT p FROM Pedido p WHERE p.estado = :estado")})
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
    @Basic(optional = false)
    @Column(name = "FECHA_PEDIDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPedido;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pedido")
    private AlmacenPedido almacenPedido;
    



    public Pedido() {
    }

    public Pedido(PedidoPK pedidoPK) {
        this.pedidoPK = pedidoPK;
    }

    public Pedido(PedidoPK pedidoPK, int noArticuloCategoria, String categoria, BigDecimal precioNeto, BigDecimal impuesto, BigDecimal descuento, BigDecimal precioTotal, String tipoEnvio, Date fechaPedido, String estado) {
        this.pedidoPK = pedidoPK;
        this.noArticuloCategoria = noArticuloCategoria;
        this.categoria = categoria;
        this.precioNeto = precioNeto;
        this.impuesto = impuesto;
        this.descuento = descuento;
        this.precioTotal = precioTotal;
        this.tipoEnvio = tipoEnvio;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
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
        if(precioTotal!=null){
            precioTotal=precioTotal.setScale(2,BigDecimal.ROUND_UP);
        }
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

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        return "compras.Pedido[pedidoPK=" + pedidoPK + "]";
    }

}
