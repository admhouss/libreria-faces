/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra;

import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.cliente.jpa.Estado;
import java.io.Serializable;
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

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "difacturacion")
@NamedQueries({
    @NamedQuery(name = "Difacturacion.findAll", query = "SELECT d FROM Difacturacion d"),
    @NamedQuery(name = "Difacturacion.findByRfc", query = "SELECT d FROM Difacturacion d WHERE d.rfc = :rfc"),
    @NamedQuery(name = "Difacturacion.findByRazonSocial", query = "SELECT d FROM Difacturacion d WHERE d.razonSocial = :razonSocial"),
    @NamedQuery(name = "Difacturacion.findByCalle", query = "SELECT d FROM Difacturacion d WHERE d.calle = :calle"),
    @NamedQuery(name = "Difacturacion.findByNoExterior", query = "SELECT d FROM Difacturacion d WHERE d.noExterior = :noExterior"),
    @NamedQuery(name = "Difacturacion.findByNoInterior", query = "SELECT d FROM Difacturacion d WHERE d.noInterior = :noInterior"),
    @NamedQuery(name = "Difacturacion.findByColonia", query = "SELECT d FROM Difacturacion d WHERE d.colonia = :colonia"),
    @NamedQuery(name = "Difacturacion.findByDelMunicipio", query = "SELECT d FROM Difacturacion d WHERE d.delMunicipio = :delMunicipio"),
    @NamedQuery(name = "Difacturacion.findByCp", query = "SELECT d FROM Difacturacion d WHERE d.cp = :cp"),
    @NamedQuery(name = "Difacturacion.findByTelefono", query = "SELECT d FROM Difacturacion d WHERE d.telefono = :telefono")})
public class Difacturacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
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
    @Column(name = "TELEFONO")
    private String telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "difacturacion")
    private List<Factura> facturaList;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "ID_EDO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Estado estado;

    public Difacturacion() {
    }

    public Difacturacion(String rfc) {
        this.rfc = rfc;
    }

    public Difacturacion(String rfc, String razonSocial, String calle, String noExterior, String noInterior, String colonia, String delMunicipio, int cp, String telefono) {
        this.rfc = rfc;
        this.razonSocial = razonSocial;
        this.calle = calle;
        this.noExterior = noExterior;
        this.noInterior = noInterior;
        this.colonia = colonia;
        this.delMunicipio = delMunicipio;
        this.cp = cp;
        this.telefono = telefono;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
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
        hash += (rfc != null ? rfc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Difacturacion)) {
            return false;
        }
        Difacturacion other = (Difacturacion) object;
        if ((this.rfc == null && other.rfc != null) || (this.rfc != null && !this.rfc.equals(other.rfc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.escom.info.compra.Difacturacion[rfc=" + rfc + "]";
    }

}
