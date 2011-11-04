/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "suscripcion")
@NamedQueries({
    @NamedQuery(name = "Suscripcion.findAll", query = "SELECT s FROM Suscripcion s"),
    @NamedQuery(name = "Suscripcion.findByIdSuscripcion", query = "SELECT s FROM Suscripcion s WHERE s.suscripcionPK.idSuscripcion = :idSuscripcion"),
    @NamedQuery(name = "Suscripcion.findByIdArticulo", query = "SELECT s FROM Suscripcion s WHERE s.suscripcionPK.idArticulo = :idArticulo"),
    @NamedQuery(name = "Suscripcion.findByNumero", query = "SELECT s FROM Suscripcion s WHERE s.numero = :numero"),
    @NamedQuery(name = "Suscripcion.findByIdNumero", query = "SELECT s FROM Suscripcion s WHERE s.idNumero = :idNumero")})
public class Suscripcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SuscripcionPK suscripcionPK;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private int numero;
    @Basic(optional = false)
    @Column(name = "ID_NUMERO")
    private int idNumero;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articulo articulo;

    public Suscripcion() {
    }

    public Suscripcion(SuscripcionPK suscripcionPK) {
        this.suscripcionPK = suscripcionPK;
    }

    public Suscripcion(SuscripcionPK suscripcionPK, int numero, int idNumero) {
        this.suscripcionPK = suscripcionPK;
        this.numero = numero;
        this.idNumero = idNumero;
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

    public int getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(int idNumero) {
        this.idNumero = idNumero;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
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
        return "escom.libreria.info.articulo.jpa.Suscripcion[suscripcionPK=" + suscripcionPK + "]";
    }

}
