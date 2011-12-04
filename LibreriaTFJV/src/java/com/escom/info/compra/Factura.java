/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra;

import escom.libreria.info.cliente.jpa.Estado;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "factura")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByFolio", query = "SELECT f FROM Factura f WHERE f.facturaPK.folio = :folio"),
    @NamedQuery(name = "Factura.findByNoInterno", query = "SELECT f FROM Factura f WHERE f.noInterno = :noInterno"),
    @NamedQuery(name = "Factura.findBySucursal", query = "SELECT f FROM Factura f WHERE f.sucursal = :sucursal"),
    @NamedQuery(name = "Factura.findByFechaCert", query = "SELECT f FROM Factura f WHERE f.fechaCert = :fechaCert"),
    @NamedQuery(name = "Factura.findByNoSerie", query = "SELECT f FROM Factura f WHERE f.noSerie = :noSerie"),
    @NamedQuery(name = "Factura.findByFechaEmi", query = "SELECT f FROM Factura f WHERE f.fechaEmi = :fechaEmi"),
    @NamedQuery(name = "Factura.findByCertEmi", query = "SELECT f FROM Factura f WHERE f.certEmi = :certEmi"),
    @NamedQuery(name = "Factura.findBySerie", query = "SELECT f FROM Factura f WHERE f.serie = :serie"),
    @NamedQuery(name = "Factura.findByRfc", query = "SELECT f FROM Factura f WHERE f.facturaPK.rfc = :rfc"),
    @NamedQuery(name = "Factura.findByRazonSocial", query = "SELECT f FROM Factura f WHERE f.razonSocial = :razonSocial"),
    @NamedQuery(name = "Factura.findByCalle", query = "SELECT f FROM Factura f WHERE f.calle = :calle"),
    @NamedQuery(name = "Factura.findByNoExterior", query = "SELECT f FROM Factura f WHERE f.noExterior = :noExterior"),
    @NamedQuery(name = "Factura.findByColonia", query = "SELECT f FROM Factura f WHERE f.colonia = :colonia"),
    @NamedQuery(name = "Factura.findByDel", query = "SELECT f FROM Factura f WHERE f.del = :del"),
    @NamedQuery(name = "Factura.findByPais", query = "SELECT f FROM Factura f WHERE f.pais = :pais"),
    @NamedQuery(name = "Factura.findByCp", query = "SELECT f FROM Factura f WHERE f.cp = :cp"),
    @NamedQuery(name = "Factura.findByComprobante", query = "SELECT f FROM Factura f WHERE f.comprobante = :comprobante"),
    @NamedQuery(name = "Factura.findByNoFolio", query = "SELECT f FROM Factura f WHERE f.noFolio = :noFolio")})
public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturaPK facturaPK;
    @Basic(optional = false)
    @Column(name = "NO_INTERNO")
    private String noInterno;
    @Basic(optional = false)
    @Column(name = "SUCURSAL")
    private String sucursal;
    @Basic(optional = false)
    @Column(name = "FECHA_CERT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCert;
    @Basic(optional = false)
    @Column(name = "NO_SERIE")
    private String noSerie;
    @Basic(optional = false)
    @Column(name = "FECHA_EMI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmi;
    @Basic(optional = false)
    @Column(name = "CERT_EMI")
    private String certEmi;
    @Basic(optional = false)
    @Column(name = "SERIE")
    private String serie;
    @Basic(optional = false)
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;
    @Basic(optional = false)
    @Column(name = "CALLE")
    private String calle;
    @Basic(optional = false)
    @Column(name = "NO_EXTERIOR")
    private String noExterior;
    @Basic(optional = false)
    @Column(name = "COLONIA")
    private String colonia;
    @Basic(optional = false)
    @Column(name = "DEL")
    private String del;
    @Basic(optional = false)
    @Column(name = "PAIS")
    private String pais;
    @Basic(optional = false)
    @Column(name = "CP")
    private int cp;
    @Basic(optional = false)
    @Lob
    @Column(name = "FACTURA")
    private byte[] factura;
    @Basic(optional = false)
    @Column(name = "COMPROBANTE")
    private String comprobante;
    @Basic(optional = false)
    @Column(name = "NO_FOLIO")
    private int noFolio;
    @JoinColumn(name = "RFC", referencedColumnName = "RFC", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Difacturacion difacturacion;
    /*@JoinColumn(name = "ID_COMPRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Compra compra;*/
    @JoinColumn(name = "ID_EDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Estado estado;

    public Factura() {
    }

    public Factura(FacturaPK facturaPK) {
        this.facturaPK = facturaPK;
    }

    public Factura(FacturaPK facturaPK, String noInterno, String sucursal, Date fechaCert, String noSerie, Date fechaEmi, String certEmi, String serie, String razonSocial, String calle, String noExterior, String colonia, String del, String pais, int cp, byte[] factura, String comprobante, int noFolio) {
        this.facturaPK = facturaPK;
        this.noInterno = noInterno;
        this.sucursal = sucursal;
        this.fechaCert = fechaCert;
        this.noSerie = noSerie;
        this.fechaEmi = fechaEmi;
        this.certEmi = certEmi;
        this.serie = serie;
        this.razonSocial = razonSocial;
        this.calle = calle;
        this.noExterior = noExterior;
        this.colonia = colonia;
        this.del = del;
        this.pais = pais;
        this.cp = cp;
        this.factura = factura;
        this.comprobante = comprobante;
        this.noFolio = noFolio;
    }

    public Factura(String folio, String rfc) {
        this.facturaPK = new FacturaPK(folio, rfc);
    }

    public FacturaPK getFacturaPK() {
        return facturaPK;
    }

    public void setFacturaPK(FacturaPK facturaPK) {
        this.facturaPK = facturaPK;
    }

    public String getNoInterno() {
        return noInterno;
    }

    public void setNoInterno(String noInterno) {
        this.noInterno = noInterno;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public Date getFechaCert() {
        return fechaCert;
    }

    public void setFechaCert(Date fechaCert) {
        this.fechaCert = fechaCert;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public Date getFechaEmi() {
        return fechaEmi;
    }

    public void setFechaEmi(Date fechaEmi) {
        this.fechaEmi = fechaEmi;
    }

    public String getCertEmi() {
        return certEmi;
    }

    public void setCertEmi(String certEmi) {
        this.certEmi = certEmi;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNoExterior() {
        return noExterior;
    }

    public void setNoExterior(String noExterior) {
        this.noExterior = noExterior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public byte[] getFactura() {
        return factura;
    }

    public void setFactura(byte[] factura) {
        this.factura = factura;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public int getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(int noFolio) {
        this.noFolio = noFolio;
    }

    public Difacturacion getDifacturacion() {
        return difacturacion;
    }

    public void setDifacturacion(Difacturacion difacturacion) {
        this.difacturacion = difacturacion;
    }

    

    public Estado getEstado() {  
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaPK != null ? facturaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.facturaPK == null && other.facturaPK != null) || (this.facturaPK != null && !this.facturaPK.equals(other.facturaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.escom.info.compra.Factura[facturaPK=" + facturaPK + "]";
    }

}
