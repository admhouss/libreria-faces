/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.proveedor;


import escom.libreria.info.articulo.AlmacenPedido;
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
@Table(name = "proveedor")
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
    @NamedQuery(name = "Proveedor.findById", query = "SELECT p FROM Proveedor p WHERE p.id = :id"),
    @NamedQuery(name = "Proveedor.findByNombre", query = "SELECT p FROM Proveedor p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Proveedor.findByCalle", query = "SELECT p FROM Proveedor p WHERE p.calle = :calle"),
    @NamedQuery(name = "Proveedor.findByColonia", query = "SELECT p FROM Proveedor p WHERE p.colonia = :colonia"),
    @NamedQuery(name = "Proveedor.findByCd", query = "SELECT p FROM Proveedor p WHERE p.cd = :cd"),
    @NamedQuery(name = "Proveedor.findByContacto", query = "SELECT p FROM Proveedor p WHERE p.contacto = :contacto"),
    @NamedQuery(name = "Proveedor.findByTipo", query = "SELECT p FROM Proveedor p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Proveedor.findByCredito", query = "SELECT p FROM Proveedor p WHERE p.credito = :credito"),
    @NamedQuery(name = "Proveedor.findByTelefono", query = "SELECT p FROM Proveedor p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Proveedor.findByFax", query = "SELECT p FROM Proveedor p WHERE p.fax = :fax"),
    @NamedQuery(name = "Proveedor.findByMail", query = "SELECT p FROM Proveedor p WHERE p.mail = :mail"),
    @NamedQuery(name = "Proveedor.findByPuesto", query = "SELECT p FROM Proveedor p WHERE p.puesto = :puesto"),
    @NamedQuery(name = "Proveedor.findByZona", query = "SELECT p FROM Proveedor p WHERE p.zona = :zona"),
    @NamedQuery(name = "Proveedor.findByDescto", query = "SELECT p FROM Proveedor p WHERE p.descto = :descto"),
    @NamedQuery(name = "Proveedor.findByMmod", query = "SELECT p FROM Proveedor p WHERE p.mmod = :mmod"),
    @NamedQuery(name = "Proveedor.findByRfc", query = "SELECT p FROM Proveedor p WHERE p.rfc = :rfc"),
    @NamedQuery(name = "Proveedor.findByCp", query = "SELECT p FROM Proveedor p WHERE p.cp = :cp"),
    @NamedQuery(name = "Proveedor.findByCtaDiv", query = "SELECT p FROM Proveedor p WHERE p.ctaDiv = :ctaDiv"),
    @NamedQuery(name = "Proveedor.findBySigla", query = "SELECT p FROM Proveedor p WHERE p.sigla = :sigla"),
    @NamedQuery(name = "Proveedor.findByFechaAlta", query = "SELECT p FROM Proveedor p WHERE p.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "Proveedor.findByEstatus", query = "SELECT p FROM Proveedor p WHERE p.estatus = :estatus")})
public class Proveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "CALLE")
    private String calle;
    @Basic(optional = false)
    @Column(name = "COLONIA")
    private String colonia;
    @Basic(optional = false)
    @Column(name = "CD")
    private String cd;
    @Column(name = "CONTACTO")
    private String contacto;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "CREDITO")
    private BigDecimal credito;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "FAX")
    private String fax;
    @Basic(optional = false)
    @Column(name = "MAIL")
    private String mail;
    @Column(name = "PUESTO")
    private String puesto;
    @Basic(optional = false)
    @Column(name = "ZONA")
    private String zona;
    @Basic(optional = false)
    @Column(name = "DESCTO")
    private String descto;
    @Basic(optional = false)
    @Column(name = "MMOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mmod;
    @Basic(optional = false)
    @Column(name = "RFC")
    private String rfc;
    @Basic(optional = false)
    @Column(name = "CP")
    private String cp;
    @Column(name = "CTA_DIV")
    private String ctaDiv;
    @Basic(optional = false)
    @Column(name = "SIGLA")
    private String sigla;
    @Basic(optional = false)
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "ESTATUS")
    private boolean estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private List<ProveedorArticulo> proveedorArticuloList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private List<AlmacenPedido> almacenPedidoList;
    
    

    public Proveedor() {
    }

    public Proveedor(Integer id) {
        this.id = id;
    }

    public Proveedor(Integer id, String nombre, String calle, String colonia, String cd, String mail, String zona, String descto, Date mmod, String rfc, String cp, String sigla, Date fechaAlta, boolean estatus) {
        this.id = id;
        this.nombre = nombre;
        this.calle = calle;
        this.colonia = colonia;
        this.cd = cd;
        this.mail = mail;
        this.zona = zona;
        this.descto = descto;
        this.mmod = mmod;
        this.rfc = rfc;
        this.cp = cp;
        this.sigla = sigla;
        this.fechaAlta = fechaAlta;
        this.estatus = estatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getDescto() {
        return descto;
    }

    public void setDescto(String descto) {
        this.descto = descto;
    }

    public Date getMmod() {
        return mmod;
    }

    public void setMmod(Date mmod) {
        this.mmod = mmod;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCtaDiv() {
        return ctaDiv;
    }

    public void setCtaDiv(String ctaDiv) {
        this.ctaDiv = ctaDiv;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public List<ProveedorArticulo> getProveedorArticuloList() {
        return proveedorArticuloList;
    }

    public void setProveedorArticuloList(List<ProveedorArticulo> proveedorArticuloList) {
        this.proveedorArticuloList = proveedorArticuloList;
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
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "articulo.Proveedor[id=" + id + "]";
    }

}
