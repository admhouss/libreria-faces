/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "compra")
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
    @NamedQuery(name = "Compra.findById", query = "SELECT c FROM Compra c WHERE c.id = :id"),
    @NamedQuery(name = "Compra.findByIdPedido", query = "SELECT c FROM Compra c WHERE c.idPedido = :idPedido"),
    @NamedQuery(name = "Compra.findByFechaEnvio", query = "SELECT c FROM Compra c WHERE c.fechaEnvio = :fechaEnvio"),
    
    @NamedQuery(name = "Compra.findByPagoTotal", query = "SELECT c FROM Compra c WHERE c.pagoTotal = :pagoTotal"),
    @NamedQuery(name = "Compra.findByEstado", query = "SELECT c FROM Compra c WHERE c.estado = :estado"),
    @NamedQuery(name = "Compra.findByObservaciones", query = "SELECT c FROM Compra c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "Compra.findByFecha", query = "SELECT c FROM Compra c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Compra.findByImpuesto", query = "SELECT c FROM Compra c WHERE c.impuesto = :impuesto"),
    @NamedQuery(name = "Compra.findByPagoNeto", query = "SELECT c FROM Compra c WHERE c.pagoNeto = :pagoNeto"),
    @NamedQuery(name = "Compra.findByDescuento", query = "SELECT c FROM Compra c WHERE c.descuento = :descuento"),
    @NamedQuery(name = "Compra.findByIdCliente", query = "SELECT c FROM Compra c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "Compra.findByCostoEnvio", query = "SELECT c FROM Compra c WHERE c.costoEnvio = :costoEnvio"),
    @NamedQuery(name = "Compra.findByNoReferencia", query = "SELECT c FROM Compra c WHERE c.noReferencia = :noReferencia"),
    @NamedQuery(name = "Compra.findByNoAutorizacion", query = "SELECT c FROM Compra c WHERE c.noAutorizacion = :noAutorizacion"),
    @NamedQuery(name = "Compra.findByCuenta", query = "SELECT c FROM Compra c WHERE c.cuenta = :cuenta"),
    @NamedQuery(name = "Compra.findByCcd", query = "SELECT c FROM Compra c WHERE c.ccd = :ccd"),
    @NamedQuery(name = "Compra.findByTipoPago", query = "SELECT c FROM Compra c WHERE c.tipoPago = :tipoPago")})
public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ID_PEDIDO")
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "FECHA_ENVIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
   
    @Column(name = "PAGO_TOTAL")
    private BigDecimal pagoTotal;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "IMPUESTO")
    private BigDecimal impuesto;
    @Basic(optional = false)
    @Column(name = "PAGO_NETO")
    private BigDecimal pagoNeto;
    @Basic(optional = false)
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;
    @Basic(optional = false)
    @Column(name = "COSTO_ENVIO")
    private BigDecimal costoEnvio;
    @Basic(optional = false)
    @Column(name = "NO_REFERENCIA")
    private String noReferencia;
    @Basic(optional = false)
    @Column(name = "NO_AUTORIZACION")
    private String noAutorizacion;
    @Basic(optional = false)
    @Column(name = "CUENTA")
    private String cuenta;
    @Basic(optional = false)
    @Column(name = "CCD")
    private int ccd;
    @Basic(optional = false)
    @Column(name = "TIPO_PAGO")
    private String tipoPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compra")
    private List<FacturaGeneral> facturaGeneralList;

    public Compra() {
    }

    public Compra(Integer id) {
        this.id = id;
    }

    public Compra(Integer id, int idPedido, Date fechaEnvio, String estado, String observaciones, Date fecha, BigDecimal impuesto, BigDecimal pagoNeto, BigDecimal descuento, String idCliente, BigDecimal costoEnvio, String noReferencia, String noAutorizacion, String cuenta, int ccd, String tipoPago) {
        this.id = id;
        this.idPedido = idPedido;
        this.fechaEnvio = fechaEnvio;
        
        this.estado = estado;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.impuesto = impuesto;
        this.pagoNeto = pagoNeto;
        this.descuento = descuento;
        this.idCliente = idCliente;
        this.costoEnvio = costoEnvio;
        this.noReferencia = noReferencia;
        this.noAutorizacion = noAutorizacion;
        this.cuenta = cuenta;
        this.ccd = ccd;
        this.tipoPago = tipoPago;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    

    public BigDecimal getPagoTotal() {
        return pagoTotal;
    }

    public void setPagoTotal(BigDecimal pagoTotal) {
        this.pagoTotal = pagoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getPagoNeto() {
        return pagoNeto;
    }

    public void setPagoNeto(BigDecimal pagoNeto) {
        this.pagoNeto = pagoNeto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getNoReferencia() {
        return noReferencia;
    }

    public void setNoReferencia(String noReferencia) {
        this.noReferencia = noReferencia;
    }

    public String getNoAutorizacion() {
        return noAutorizacion;
    }

    public void setNoAutorizacion(String noAutorizacion) {
        this.noAutorizacion = noAutorizacion;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public int getCcd() {
        return ccd;
    }

    public void setCcd(int ccd) {
        this.ccd = ccd;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public List<FacturaGeneral> getFacturaGeneralList() {
        return facturaGeneralList;
    }

    public void setFacturaGeneralList(List<FacturaGeneral> facturaGeneralList) {
        this.facturaGeneralList = facturaGeneralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.Compra[id=" + id + "]";
    }

}
