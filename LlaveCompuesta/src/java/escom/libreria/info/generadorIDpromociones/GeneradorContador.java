/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.generadorIDpromociones;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "generador_contador")
@NamedQueries({
    @NamedQuery(name = "GeneradorContador.findAll", query = "SELECT g FROM GeneradorContador g"),
    @NamedQuery(name = "GeneradorContador.findByContador", query = "SELECT g FROM GeneradorContador g WHERE g.contador = :contador")})
public class GeneradorContador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "CONTADOR")
    private Integer contador;

    public GeneradorContador() {
    }

    public GeneradorContador(Integer contador) {
        this.contador = contador;
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contador != null ? contador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneradorContador)) {
            return false;
        }
        GeneradorContador other = (GeneradorContador) object;
        if ((this.contador == null && other.contador != null) || (this.contador != null && !this.contador.equals(other.contador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.generadorIDpromociones.GeneradorContador[contador=" + contador + "]";
    }

}
