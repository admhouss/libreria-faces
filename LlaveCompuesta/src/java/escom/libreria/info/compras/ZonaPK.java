/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author xxx
 */
@Embeddable
public class ZonaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_ZONA")
    private String idZona;
    @Basic(optional = false)
    @Column(name = "PESO")
    private BigDecimal peso;

    public ZonaPK() {
    }

    public ZonaPK(String idZona, BigDecimal peso) {
        this.idZona = idZona;
        this.peso = peso;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idZona != null ? idZona.hashCode() : 0);
        hash += (peso != null ? peso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZonaPK)) {
            return false;
        }
        ZonaPK other = (ZonaPK) object;
        if ((this.idZona == null && other.idZona != null) || (this.idZona != null && !this.idZona.equals(other.idZona))) {
            return false;
        }
        if ((this.peso == null && other.peso != null) || (this.peso != null && !this.peso.equals(other.peso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "construirXMLPruebita.ZonaPK[idZona=" + idZona + ", peso=" + peso + "]";
    }

}
