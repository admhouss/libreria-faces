/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.jpa;



import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.Pedido;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cliente")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByPaterno", query = "SELECT c FROM Cliente c WHERE c.paterno = :paterno"),
    @NamedQuery(name = "Cliente.findByMaterno", query = "SELECT c FROM Cliente c WHERE c.materno = :materno"),
    @NamedQuery(name = "Cliente.findByFax", query = "SELECT c FROM Cliente c WHERE c.fax = :fax"),
    @NamedQuery(name = "Cliente.findByEstatus", query = "SELECT c FROM Cliente c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "Cliente.findByPassword", query = "SELECT c FROM Cliente c WHERE c.password = :password"),
    @NamedQuery(name = "Cliente.findByFechaAlta", query = "SELECT c FROM Cliente c WHERE c.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "Cliente.findByRecibeInfor", query = "SELECT c FROM Cliente c WHERE c.recibeInfor = :recibeInfor"),
    @NamedQuery(name = "Cliente.findByEmail", query = "SELECT c FROM Cliente c WHERE c.email = :email"),
    @NamedQuery(name = "Cliente.findByModificacion", query = "SELECT c FROM Cliente c WHERE c.modificacion = :modificacion"),
    @NamedQuery(name = "Cliente.findByTelefono", query = "SELECT c FROM Cliente c WHERE c.telefono = :telefono")})
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "PATERNO")
    private String paterno;
    @Basic(optional = false)
    @Column(name = "MATERNO")
    private String materno;
    @Column(name = "FAX")
    private String fax;
    @Basic(optional = false)
    @Column(name = "ESTATUS")
    private boolean estatus;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "RECIBE_INFOR")
    private boolean recibeInfor;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @Column(name = "MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificacion;
    @Basic(optional = false)
    @Column(name = "TELEFONO")
    private String telefono;
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Estado estado;
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Categoria categoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<BitacoraCliente> bitacoraClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Difacturacion> difacturacionList;//nuevo
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Pedido> pedidoList;

    //  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    //private List<Compra> compraCollection;
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }
    

    //public List<Compra> getCompraCollection() {
      //  return compraCollection;
    //}

    /*public void setCompraCollection(List<Compra> compraCollection) {
        this.compraCollection = compraCollection;
    }*/



   /* public Collection<Compra> getCompraCollection() {
        return compraCollection;
    }

    public void setCompraCollection(Collection<Compra> compraCollection) {
        this.compraCollection = compraCollection;
    }

    */

    public List<Difacturacion> getDifacturacionList() {
        return difacturacionList;
    }

    public void setDifacturacionList(List<Difacturacion> difacturacionList) {
        this.difacturacionList = difacturacionList;
    }



    public Cliente() {
    }

    public Cliente(String id) {
        this.id = id;
    }

    public Cliente(String id, String nombre, String paterno, String materno, boolean estatus, String password, Date fechaAlta, boolean recibeInfor, String email, Date modificacion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.estatus = estatus;
        this.password = password;
        this.fechaAlta = fechaAlta;
        this.recibeInfor = recibeInfor;
        this.email = email;
        this.modificacion = modificacion;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean getRecibeInfor() {
        return recibeInfor;
    }

    public void setRecibeInfor(boolean recibeInfor) {
        this.recibeInfor = recibeInfor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getModificacion() {
        return modificacion;
    }

    public void setModificacion(Date modificacion) {
        this.modificacion = modificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<BitacoraCliente> getBitacoraClienteList() {
        return bitacoraClienteList;
    }

    public void setBitacoraClienteList(List<BitacoraCliente> bitacoraClienteList) {
        this.bitacoraClienteList = bitacoraClienteList;
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
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.cliente.jpa.Cliente[id=" + id + "]";
    }

}
