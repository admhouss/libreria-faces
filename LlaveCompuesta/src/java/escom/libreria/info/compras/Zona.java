/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "zona")
@NamedQueries({
    @NamedQuery(name = "Zona.findAll", query = "SELECT z FROM Zona z"),
    @NamedQuery(name = "Zona.findByIdZona", query = "SELECT z FROM Zona z WHERE z.idZona = :idZona"),
    @NamedQuery(name = "Zona.findByPeso", query = "SELECT z FROM Zona z WHERE z.peso = :peso"),
    @NamedQuery(name = "Zona.findByTarifa", query = "SELECT z FROM Zona z WHERE z.tarifa = :tarifa")})
public class Zona implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZonaPK zonaPK;
    @Column(name = "PESO")
    private BigDecimal peso;
    @Column(name = "TARIFA")
    private BigDecimal tarifa;
    @OneToMany(mappedBy = "zona")
    private List<Estado> estadoList;

    public Zona() {
    }

    public ZonaPK getZonaPK() {
        return zonaPK;
    }

    public void setZonaPK(ZonaPK zonaPK) {
        this.zonaPK = zonaPK;
    }

   
    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public List<Estado> getEstadoList() {
        return estadoList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Zona other = (Zona) obj;
        if (this.zonaPK != other.zonaPK && (this.zonaPK == null || !this.zonaPK.equals(other.zonaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }




    @Override
    public String toString() {
        return "escom.libreria.info.compras.Zona[zonaPK=" + zonaPK + "]";
    }

}
