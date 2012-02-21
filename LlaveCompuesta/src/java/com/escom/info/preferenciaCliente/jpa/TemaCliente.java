/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.preferenciaCliente.jpa;

import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.facturacion.Articulo;
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
@Table(name = "tema_cliente")
@NamedQueries({
    @NamedQuery(name = "TemaCliente.findAll", query = "SELECT t FROM TemaCliente t"),
    @NamedQuery(name = "TemaCliente.findById", query = "SELECT t FROM TemaCliente t WHERE t.id = :id")})
public class TemaCliente implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_ARTICULO")
    private String idArticulo;
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private String idCliente;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
   

    public TemaCliente() {
    }

    public TemaCliente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof TemaCliente)) {
            return false;
        }
        TemaCliente other = (TemaCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.escom.info.preferenciaCliente.jpa.TemaCliente[id=" + id + "]";
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

}
