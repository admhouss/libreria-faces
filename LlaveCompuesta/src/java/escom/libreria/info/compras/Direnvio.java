/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import escom.libreria.info.cliente.Cliente;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "direnvio")
@NamedQueries({
    @NamedQuery(name = "Direnvio.findAll", query = "SELECT d FROM Direnvio d"),
    @NamedQuery(name = "Direnvio.findById", query = "SELECT d FROM Direnvio d WHERE d.id = :id"),
    @NamedQuery(name = "Direnvio.findByAtencion", query = "SELECT d FROM Direnvio d WHERE d.atencion = :atencion"),
    @NamedQuery(name = "Direnvio.findByCalle", query = "SELECT d FROM Direnvio d WHERE d.calle = :calle"),
    @NamedQuery(name = "Direnvio.findByNoExterior", query = "SELECT d FROM Direnvio d WHERE d.noExterior = :noExterior"),
    @NamedQuery(name = "Direnvio.findByNoInterior", query = "SELECT d FROM Direnvio d WHERE d.noInterior = :noInterior"),
    @NamedQuery(name = "Direnvio.findByColonia", query = "SELECT d FROM Direnvio d WHERE d.colonia = :colonia"),
    @NamedQuery(name = "Direnvio.findByDelMunicipio", query = "SELECT d FROM Direnvio d WHERE d.delMunicipio = :delMunicipio"),
    @NamedQuery(name = "Direnvio.findByCp", query = "SELECT d FROM Direnvio d WHERE d.cp = :cp"),
    @NamedQuery(name = "Direnvio.findByReferencia", query = "SELECT d FROM Direnvio d WHERE d.referencia = :referencia"),
    @NamedQuery(name = "Direnvio.findByTelefono", query = "SELECT d FROM Direnvio d WHERE d.telefono = :telefono")})
public class Direnvio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ATENCION")
    private String atencion;
    @Basic(optional = false)
    @Column(name = "CALLE")
    private String calle;
    @Basic(optional = false)
    @Column(name = "NO_EXTERIOR")
    private String noExterior;
    @Basic(optional = false)
    @Column(name = "NO_INTERIOR")
    private String noInterior;
    @Basic(optional = false)
    @Column(name = "COLONIA")
    private String colonia;
    @Basic(optional = false)
    @Column(name = "DEL_MUNICIPIO")
    private String delMunicipio;
    @Basic(optional = false)
    @Column(name = "CP")
    private int cp;
    @Basic(optional = false)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Basic(optional = false)
    @Column(name = "TELEFONO")
    private String telefono;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "ID_EDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Estado estado;

    public Direnvio() {
    }

    public Direnvio(Integer id) {
        this.id = id;
    }

    public Direnvio(Integer id, String atencion, String calle, String noExterior, String noInterior, String colonia, String delMunicipio, int cp, String referencia, String telefono) {
        this.id = id;
        this.atencion = atencion;
        this.calle = calle;
        this.noExterior = noExterior;
        this.noInterior = noInterior;
        this.colonia = colonia;
        this.delMunicipio = delMunicipio;
        this.cp = cp;
        this.referencia = referencia;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
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

    public String getNoInterior() {
        return noInterior;
    }

    public void setNoInterior(String noInterior) {
        this.noInterior = noInterior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getDelMunicipio() {
        return delMunicipio;
    }

    public void setDelMunicipio(String delMunicipio) {
        this.delMunicipio = delMunicipio;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Direnvio)) {
            return false;
        }
        Direnvio other = (Direnvio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.Direnvio[id=" + id + "]";
    }

}
