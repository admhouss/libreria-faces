/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones;

import escom.libreria.info.facturacion.Articulo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "suscripcion")
@NamedQueries({
    @NamedQuery(name = "Suscripcion.findAll", query = "SELECT s FROM Suscripcion s"),
    @NamedQuery(name = "Suscripcion.findByIdSuscripcion", query = "SELECT s FROM Suscripcion s WHERE s.suscripcionPK.idSuscripcion = :idSuscripcion"),
    @NamedQuery(name = "Suscripcion.findByIdArticulo", query = "SELECT s FROM Suscripcion s WHERE s.suscripcionPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "Suscripcion.findByNumero", query = "SELECT s FROM Suscripcion s WHERE s.numero = :numero")})
    
public class Suscripcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SuscripcionPK suscripcionPK;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private int numero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "suscripcion")
    private List<SuscripcionEnvios> suscripcionEnviosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "suscripcion")
    private List<SuscripcionCliente> suscripcionClienteList;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "suscripcion")
    private List<SuscripcionElectronica> suscripcionElectronicaList;

    public Suscripcion() {
    }

    public Suscripcion(SuscripcionPK suscripcionPK) {
        this.suscripcionPK = suscripcionPK;
    }

    public Suscripcion(SuscripcionPK suscripcionPK, int numero) {
        this.suscripcionPK = suscripcionPK;
        this.numero = numero;
        
    }

    public Suscripcion(int idSuscripcion, int idArticulo) {
        this.suscripcionPK = new SuscripcionPK(idSuscripcion, idArticulo);
    }

    public SuscripcionPK getSuscripcionPK() {
        return suscripcionPK;
    }

    public void setSuscripcionPK(SuscripcionPK suscripcionPK) {
        this.suscripcionPK = suscripcionPK;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    
    public List<SuscripcionEnvios> getSuscripcionEnviosList() {
        return suscripcionEnviosList;
    }

    public void setSuscripcionEnviosList(List<SuscripcionEnvios> suscripcionEnviosList) {
        this.suscripcionEnviosList = suscripcionEnviosList;
    }

    public List<SuscripcionCliente> getSuscripcionClienteList() {
        return suscripcionClienteList;
    }

    public void setSuscripcionClienteList(List<SuscripcionCliente> suscripcionClienteList) {
        this.suscripcionClienteList = suscripcionClienteList;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public List<SuscripcionElectronica> getSuscripcionElectronicaList() {
        return suscripcionElectronicaList;
    }

    public void setSuscripcionElectronicaList(List<SuscripcionElectronica> suscripcionElectronicaList) {
        this.suscripcionElectronicaList = suscripcionElectronicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (suscripcionPK != null ? suscripcionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Suscripcion)) {
            return false;
        }
        Suscripcion other = (Suscripcion) object;
        if ((this.suscripcionPK == null && other.suscripcionPK != null) || (this.suscripcionPK != null && !this.suscripcionPK.equals(other.suscripcionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "compras.Suscripcion[suscripcionPK=" + suscripcionPK + "]";
    }

}
