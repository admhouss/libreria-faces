/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import escom.libreria.info.proveedor.jpa.Proveedor;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "articulo")
@NamedQueries({
    @NamedQuery(name = "Articulo.findAll", query = "SELECT a FROM Articulo a"),
    @NamedQuery(name = "Articulo.findById", query = "SELECT a FROM Articulo a WHERE a.id = :id"),
    @NamedQuery(name = "Articulo.findByUnidad", query = "SELECT a FROM Articulo a WHERE a.unidad = :unidad"),
    @NamedQuery(name = "Articulo.findByDivisa", query = "SELECT a FROM Articulo a WHERE a.divisa = :divisa"),
    @NamedQuery(name = "Articulo.findByCosto", query = "SELECT a FROM Articulo a WHERE a.costo = :costo"),
    @NamedQuery(name = "Articulo.findByPrecioUnitario", query = "SELECT a FROM Articulo a WHERE a.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "Articulo.findByDescripcion", query = "SELECT a FROM Articulo a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Articulo.findByFechaRegistro", query = "SELECT a FROM Articulo a WHERE a.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Articulo.findByImagen", query = "SELECT a FROM Articulo a WHERE a.imagen = :imagen")})
public class Articulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "UNIDAD")
    private int unidad;
    @Basic(optional = false)
    @Column(name = "DIVISA")
    private String divisa;
    @Basic(optional = false)
    @Column(name = "COSTO")
    private BigDecimal costo;
    @Basic(optional = false)
    @Column(name = "PRECIO_UNITARIO")
    private BigDecimal precioUnitario;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "IMAGEN")
    private String imagen;
    @JoinColumn(name = "ID_IDC", referencedColumnName = "ID_DC")
    @ManyToOne(optional = false)
    private Publicacion idIdc;
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Proveedor idProveedor;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoArticulo idTipo;
  
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "articulo")
    private DescuentoArticulo descuentoArticulo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "articulo")
    private Impuesto impuesto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Promocion> promocionList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "articulo")
    private Almacen almacen;

    public Articulo() {
    }

    public Articulo(Integer id) {
        this.id = id;
    }

    public Articulo(Integer id, int unidad, String divisa, BigDecimal costo, BigDecimal precioUnitario, String descripcion, Date fechaRegistro) {
        this.id = id;
        this.unidad = unidad;
        this.divisa = divisa;
        this.costo = costo;
        this.precioUnitario = precioUnitario;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Publicacion getIdIdc() {
        return idIdc;
    }

    public void setIdIdc(Publicacion idIdc) {
        this.idIdc = idIdc;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    public TipoArticulo getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(TipoArticulo idTipo) {
        this.idTipo = idTipo;
    }

   

    

    public DescuentoArticulo getDescuentoArticulo() {
        return descuentoArticulo;
    }

    public void setDescuentoArticulo(DescuentoArticulo descuentoArticulo) {
        this.descuentoArticulo = descuentoArticulo;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public List<Promocion> getPromocionList() {
        return promocionList;
    }

    public void setPromocionList(List<Promocion> promocionList) {
        this.promocionList = promocionList;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
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
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.jpa.Articulo[id=" + id + "]";
    }

}
