/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.jpa;

import com.escom.info.compra.Difacturacion;
import com.escom.info.compra.Factura;
import com.escom.info.compra.FacturaGeneral;
import escom.libreria.info.contacto.jpa.Direnvio;
import java.io.Serializable;
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

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "estado")
@NamedQueries({
    @NamedQuery(name = "Estado.findAll", query = "SELECT e FROM Estado e"),
    @NamedQuery(name = "Estado.findById", query = "SELECT e FROM Estado e WHERE e.id = :id"),
    @NamedQuery(name = "Estado.findByNombre", query = "SELECT e FROM Estado e WHERE e.nombre = :nombre")})
public class Estado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<Cliente> clienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<Factura> facturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<Difacturacion> difacturacionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<Direnvio> direnvioList;



    public List<Difacturacion> getDifacturacionList() {
        return difacturacionList;
    }

    public void setDifacturacionList(List<Difacturacion> difacturacionList) {
        this.difacturacionList = difacturacionList;
    }

    public List<Direnvio> getDirenvioList() {
        return direnvioList;
    }

    public void setDirenvioList(List<Direnvio> direnvioList) {
        this.direnvioList = direnvioList;
    }

    

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }



    public Estado() {
    }

    public Estado(Integer id) {
        this.id = id;
    }

    public Estado(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
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
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.cliente.jpa.Estado[id=" + id + "]";
    }

}
