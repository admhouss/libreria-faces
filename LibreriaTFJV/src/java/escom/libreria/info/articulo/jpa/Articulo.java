/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;


import escom.libreria.info.cliente.jpa.BitacoraCliente;

import escom.libreria.info.proveedor.jpa.Proveedor;
import escom.libreria.info.proveedor.jpa.ProveedorArticulo;
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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
    @NamedQuery(name = "Articulo.findByFormato", query = "SELECT a FROM Articulo a WHERE a.formato = :formato"),
    @NamedQuery(name = "Articulo.findByIdioma", query = "SELECT a FROM Articulo a WHERE a.idioma = :idioma"),
    @NamedQuery(name = "Articulo.findByModUpdate", query = "SELECT a FROM Articulo a WHERE a.modUpdate = :modUpdate"),
    @NamedQuery(name = "Articulo.findByPublicador", query = "SELECT a FROM Articulo a WHERE a.publicador = :publicador"),
    @NamedQuery(name = "Articulo.findByAsunto", query = "SELECT a FROM Articulo a WHERE a.asunto = :asunto"),
    @NamedQuery(name = "Articulo.findByFechaRegistro", query = "SELECT a FROM Articulo a WHERE a.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Articulo.findByImagen", query = "SELECT a FROM Articulo a WHERE a.imagen = :imagen"),
    @NamedQuery(name = "Articulo.findByArchivo", query = "SELECT a FROM Articulo a WHERE a.archivo = :archivo"),
    @NamedQuery(name = "Articulo.findByNombreAlternativo", query = "SELECT a FROM Articulo a WHERE a.nombreAlternativo = :nombreAlternativo"),
    @NamedQuery(name = "Articulo.findByFechaDisponibilidad", query = "SELECT a FROM Articulo a WHERE a.fechaDisponibilidad = :fechaDisponibilidad"),
    @NamedQuery(name = "Articulo.findByFechaCreacion", query = "SELECT a FROM Articulo a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Articulo.findByCreador", query = "SELECT a FROM Articulo a WHERE a.creador = :creador"),
    @NamedQuery(name = "Articulo.findByTitulo", query = "SELECT a FROM Articulo a WHERE a.titulo = :titulo"),
    @NamedQuery(name = "Articulo.findByFormatoDigital", query = "SELECT a FROM Articulo a WHERE a.formatoDigital = :formatoDigital"),
    @NamedQuery(name = "Articulo.findByAgregacionRecurso", query = "SELECT a FROM Articulo a WHERE a.agregacionRecurso = :agregacionRecurso"),
    @NamedQuery(name = "Articulo.findByCodigo", query = "SELECT a FROM Articulo a WHERE a.codigo = :codigo")})
public class Articulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "UNIDAD")
    private String unidad;
    @Basic(optional = false)
    @Column(name = "DIVISA")
    private String divisa;
    @Basic(optional = false)
    @Column(name = "COSTO")
    private BigDecimal costo;
    @Basic(optional = false)
    @Column(name = "PRECIO_UNITARIO")
    private BigDecimal precioUnitario;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "FORMATO")
    private String formato;
    @Basic(optional = false)
    @Column(name = "IDIOMA")
    private String idioma;
    @Basic(optional = false)
    @Column(name = "MOD_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modUpdate;
    @Basic(optional = false)
    @Column(name = "PUBLICADOR")
    private String publicador;
    @Lob
    @Column(name = "REQUERIDO")
    private String requerido;
    @Basic(optional = false)
    @Column(name = "ASUNTO")
    private String asunto;
    @Basic(optional = false)
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "IMAGEN")
    private String imagen;
    @Column(name = "ARCHIVO")
    private String archivo;
    @Basic(optional = false)
    @Lob
    @Column(name = "RESUMEN_RECURSO")
    private String resumenRecurso;
    @Column(name = "NOMBRE_ALTERNATIVO")
    private String nombreAlternativo;
    @Basic(optional = false)
    @Column(name = "FECHA_DISPONIBILIDAD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDisponibilidad;
    @Lob
    @Column(name = "REFERENCIAS_BIBLIOGRAFICAS")
    private String referenciasBibliograficas;
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "CREADOR")
    private String creador;
    @Lob
    @Column(name = "TABLA_CONTENIDOS")
    private String tablaContenidos;
    @Column(name = "TITULO")
    private String titulo;
    @Column(name = "FORMATO_DIGITAL")
    private String formatoDigital;
    @Column(name = "AGREGACION_RECURSO")
    private String agregacionRecurso;
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoArticulo tipoArticulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Suscripcion> suscripcionList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "articulo")
    private DescuentoArticulo descuentoArticulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Publicacion> publicacionList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "articulo")
    private Almacen almacen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Impuesto> impuestoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Comentario> comentarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<BitacoraCliente> bitacoraClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<ProveedorArticulo>  proveedorArticulos;

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Pedido> pedidoList;

    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

*/
    
    public Articulo() {
    }

    public List<ProveedorArticulo> getProveedorArticulos() {
        return proveedorArticulos;
    }

    public void setProveedorArticulos(List<ProveedorArticulo> proveedorArticulos) {
        this.proveedorArticulos = proveedorArticulos;
    }

     /*@JoinTable(name = "proveedor_articulo", joinColumns = {
        //@JoinColumn(name="CANTIDAD"),
        //@JoinColumn(name="ULT_MOD"),
        @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName = "ID")})
     @ManyToMany
     private List<Proveedor> proveedorList;
      *
      */

   /*  @Basic(optional = false)
     @Column(name = "CANTIDAD")
     private int cantidad;
     @Basic(optional = false)
     @Column(name = "ULT_MOD")
     @Temporal(javax.persistence.TemporalType.TIMESTAMP)
     private Date ultMod;
    *
    */

    /*public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }
    */

    
    public Articulo(Integer id) {
        this.id = id;
    }

    public Articulo(Integer id, String unidad, String divisa, BigDecimal costo, BigDecimal precioUnitario, String formato, String idioma, Date modUpdate, String publicador, String asunto, Date fechaRegistro, String resumenRecurso, Date fechaDisponibilidad, String creador, String codigo) {
        this.id = id;
        this.unidad = unidad;
        this.divisa = divisa;
        this.costo = costo;
        this.precioUnitario = precioUnitario;
        this.formato = formato;
        this.idioma = idioma;
        this.modUpdate = modUpdate;
        this.publicador = publicador;
        this.asunto = asunto;
        this.fechaRegistro = fechaRegistro;
        this.resumenRecurso = resumenRecurso;
        this.fechaDisponibilidad = fechaDisponibilidad;
        this.creador = creador;
        this.codigo = codigo;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public List<BitacoraCliente> getBitacoraClienteList() {
        return bitacoraClienteList;
    }

    public void setBitacoraClienteList(List<BitacoraCliente> bitacoraClienteList) {
        this.bitacoraClienteList = bitacoraClienteList;
    }

    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    public DescuentoArticulo getDescuentoArticulo() {
        return descuentoArticulo;
    }

    public void setDescuentoArticulo(DescuentoArticulo descuentoArticulo) {
        this.descuentoArticulo = descuentoArticulo;
    }

    public List<Impuesto> getImpuestoList() {
        return impuestoList;
    }

    public void setImpuestoList(List<Impuesto> impuestoList) {
        this.impuestoList = impuestoList;
    }

  /*  public List<Proveedor> getProveedorList() {
        return proveedorList;
    }

    public void setProveedorList(List<Proveedor> proveedorList) {
        this.proveedorList = proveedorList;
    }*/

    public List<Publicacion> getPublicacionList() {
        return publicacionList;
    }

    public void setPublicacionList(List<Publicacion> publicacionList) {
        this.publicacionList = publicacionList;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Date getModUpdate() {
        return modUpdate;
    }

    public void setModUpdate(Date modUpdate) {
        this.modUpdate = modUpdate;
    }

    public String getPublicador() {
        return publicador;
    }

    public void setPublicador(String publicador) {
        this.publicador = publicador;
    }

    public String getRequerido() {
        return requerido;
    }

    public void setRequerido(String requerido) {
        this.requerido = requerido;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getResumenRecurso() {
        return resumenRecurso;
    }

    public void setResumenRecurso(String resumenRecurso) {
        this.resumenRecurso = resumenRecurso;
    }

    public String getNombreAlternativo() {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo) {
        this.nombreAlternativo = nombreAlternativo;
    }

    public Date getFechaDisponibilidad() {
        return fechaDisponibilidad;
    }

    public void setFechaDisponibilidad(Date fechaDisponibilidad) {
        this.fechaDisponibilidad = fechaDisponibilidad;
    }

    public String getReferenciasBibliograficas() {
        return referenciasBibliograficas;
    }

    public void setReferenciasBibliograficas(String referenciasBibliograficas) {
        this.referenciasBibliograficas = referenciasBibliograficas;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getTablaContenidos() {
        return tablaContenidos;
    }

    public void setTablaContenidos(String tablaContenidos) {
        this.tablaContenidos = tablaContenidos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFormatoDigital() {
        return formatoDigital;
    }

    public void setFormatoDigital(String formatoDigital) {
        this.formatoDigital = formatoDigital;
    }

    public String getAgregacionRecurso() {
        return agregacionRecurso;
    }

    public void setAgregacionRecurso(String agregacionRecurso) {
        this.agregacionRecurso = agregacionRecurso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoArticulo getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(TipoArticulo tipoArticulo) {
        this.tipoArticulo = tipoArticulo;
    }

    public List<Suscripcion> getSuscripcionList() {
        return suscripcionList;
    }

    public void setSuscripcionList(List<Suscripcion> suscripcionList) {
        this.suscripcionList = suscripcionList;
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
