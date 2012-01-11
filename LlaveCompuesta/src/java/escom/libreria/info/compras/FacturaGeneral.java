/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "factura_general")
@NamedQueries({
    @NamedQuery(name = "FacturaGeneral.findAll", query = "SELECT f FROM FacturaGeneral f"),
    @NamedQuery(name = "FacturaGeneral.findByFolio", query = "SELECT f FROM FacturaGeneral f WHERE f.folio = :folio"),
    @NamedQuery(name = "FacturaGeneral.findByComprobante", query = "SELECT f FROM FacturaGeneral f WHERE f.comprobante = :comprobante"),
    @NamedQuery(name = "FacturaGeneral.findByFechaCert", query = "SELECT f FROM FacturaGeneral f WHERE f.fechaCert = :fechaCert"),
    @NamedQuery(name = "FacturaGeneral.findByNoSerie", query = "SELECT f FROM FacturaGeneral f WHERE f.noSerie = :noSerie"),
    @NamedQuery(name = "FacturaGeneral.findByFechaEmi", query = "SELECT f FROM FacturaGeneral f WHERE f.fechaEmi = :fechaEmi"),
    @NamedQuery(name = "FacturaGeneral.findByCertEmi", query = "SELECT f FROM FacturaGeneral f WHERE f.certEmi = :certEmi"),
    @NamedQuery(name = "FacturaGeneral.findBySerie", query = "SELECT f FROM FacturaGeneral f WHERE f.serie = :serie"),
    @NamedQuery(name = "FacturaGeneral.findByNoFolio", query = "SELECT f FROM FacturaGeneral f WHERE f.noFolio = :noFolio"),
    @NamedQuery(name = "FacturaGeneral.findByNoInterno", query = "SELECT f FROM FacturaGeneral f WHERE f.noInterno = :noInterno"),
    @NamedQuery(name = "FacturaGeneral.findBySucursal", query = "SELECT f FROM FacturaGeneral f WHERE f.sucursal = :sucursal"),
    @NamedQuery(name = "FacturaGeneral.findByRfc", query = "SELECT f FROM FacturaGeneral f WHERE f.rfc = :rfc"),
    @NamedQuery(name = "FacturaGeneral.findByRazonSocial", query = "SELECT f FROM FacturaGeneral f WHERE f.razonSocial = :razonSocial"),
    @NamedQuery(name = "FacturaGeneral.findByCalle", query = "SELECT f FROM FacturaGeneral f WHERE f.calle = :calle"),
    @NamedQuery(name = "FacturaGeneral.findByNoExterior", query = "SELECT f FROM FacturaGeneral f WHERE f.noExterior = :noExterior"),
    @NamedQuery(name = "FacturaGeneral.findByColonia", query = "SELECT f FROM FacturaGeneral f WHERE f.colonia = :colonia"),
    @NamedQuery(name = "FacturaGeneral.findByDel", query = "SELECT f FROM FacturaGeneral f WHERE f.del = :del"),
    @NamedQuery(name = "FacturaGeneral.findByIdEdo", query = "SELECT f FROM FacturaGeneral f WHERE f.idEdo = :idEdo"),
    @NamedQuery(name = "FacturaGeneral.findByPais", query = "SELECT f FROM FacturaGeneral f WHERE f.pais = :pais"),
    @NamedQuery(name = "FacturaGeneral.findByCp", query = "SELECT f FROM FacturaGeneral f WHERE f.cp = :cp")})
public class FacturaGeneral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "FOLIO")
    private String folio;
    @Basic(optional = false)
    @Column(name = "COMPROBANTE")
    private String comprobante;
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
    @Column(name = "NO_FOLIO")
    private int noFolio;
    @Basic(optional = false)
    @Column(name = "NO_INTERNO")
    private String noInterno;
    @Basic(optional = false)
    @Column(name = "SUCURSAL")
    private String sucursal;
    @Basic(optional = false)
    @Column(name = "RFC")
    private String rfc;
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
    @Column(name = "ID_EDO")
    private int idEdo;
    @Basic(optional = false)
    @Column(name = "PAIS")
    private String pais;
    @Basic(optional = false)
    @Column(name = "CP")
    private int cp;
    @JoinColumn(name = "ID_COMPRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Compra compra;

    public FacturaGeneral() {
    }

    public FacturaGeneral(String folio) {
        this.folio = folio;
    }

    public FacturaGeneral(String folio, String comprobante, Date fechaCert, String noSerie, Date fechaEmi, String certEmi, String serie, int noFolio, String noInterno, String sucursal, String rfc, String razonSocial, String calle, String noExterior, String colonia, String del, int idEdo, String pais, int cp) {
        this.folio = folio;
        this.comprobante = comprobante;
        this.fechaCert = fechaCert;
        this.noSerie = noSerie;
        this.fechaEmi = fechaEmi;
        this.certEmi = certEmi;
        this.serie = serie;
        this.noFolio = noFolio;
        this.noInterno = noInterno;
        this.sucursal = sucursal;
        this.rfc = rfc;
        this.razonSocial = razonSocial;
        this.calle = calle;
        this.noExterior = noExterior;
        this.colonia = colonia;
        this.del = del;
        this.idEdo = idEdo;
        this.pais = pais;
        this.cp = cp;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
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

    public int getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(int noFolio) {
        this.noFolio = noFolio;
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

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
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

    public int getIdEdo() {
        return idEdo;
    }

    public void setIdEdo(int idEdo) {
        this.idEdo = idEdo;
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (folio != null ? folio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaGeneral)) {
            return false;
        }
        FacturaGeneral other = (FacturaGeneral) object;
        if ((this.folio == null && other.folio != null) || (this.folio != null && !this.folio.equals(other.folio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.FacturaGeneral[folio=" + folio + "]";
    }

}
