/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author xxx
 */
@Entity
@Table(name = "publicacion")
@NamedQueries({
    @NamedQuery(name = "Publicacion.findAll", query = "SELECT p FROM Publicacion p"),
    @NamedQuery(name = "Publicacion.findByIdDc", query = "SELECT p FROM Publicacion p WHERE p.idDc = :idDc"),
    @NamedQuery(name = "Publicacion.findByPeriodoMes", query = "SELECT p FROM Publicacion p WHERE p.periodoMes = :periodoMes"),
    @NamedQuery(name = "Publicacion.findByPeriodoAnio", query = "SELECT p FROM Publicacion p WHERE p.periodoAnio = :periodoAnio"),
    @NamedQuery(name = "Publicacion.findByEpoca", query = "SELECT p FROM Publicacion p WHERE p.epoca = :epoca"),
    @NamedQuery(name = "Publicacion.findByAnio", query = "SELECT p FROM Publicacion p WHERE p.anio = :anio"),
    @NamedQuery(name = "Publicacion.findByNumero", query = "SELECT p FROM Publicacion p WHERE p.numero = :numero"),
    @NamedQuery(name = "Publicacion.findByIssn", query = "SELECT p FROM Publicacion p WHERE p.issn = :issn"),
    @NamedQuery(name = "Publicacion.findByIsbn", query = "SELECT p FROM Publicacion p WHERE p.isbn = :isbn"),
    @NamedQuery(name = "Publicacion.findByTomo", query = "SELECT p FROM Publicacion p WHERE p.tomo = :tomo"),
    @NamedQuery(name = "Publicacion.findByEditorial", query = "SELECT p FROM Publicacion p WHERE p.editorial = :editorial")})
public class Publicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_DC")
    private Integer idDc;
    @Basic(optional = false)
    @Column(name = "PERIODO_MES")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodoMes;
    @Basic(optional = false)
    @Column(name = "PERIODO_ANIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodoAnio;
    @Basic(optional = false)
    @Column(name = "EPOCA")
    private String epoca;
    @Basic(optional = false)
    @Column(name = "ANIO")
    private String anio;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private int numero;
    @Basic(optional = false)
    @Column(name = "ISSN")
    private int issn;
    @Basic(optional = false)
    @Column(name = "ISBN")
    private String isbn;
    @Basic(optional = false)
    @Column(name = "TOMO")
    private String tomo;
    @Basic(optional = false)
    @Column(name = "EDITORIAL")
    private String editorial;
    @Lob
    @Column(name = "ARCHIVO")
    private byte[] archivo;
    @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID")
    @ManyToOne
    private Articulo articulo;

    public Publicacion() {
    }

    public Publicacion(Integer idDc) {
        this.idDc = idDc;
    }

    public Publicacion(Integer idDc, Date periodoMes, Date periodoAnio, String epoca, String anio, int numero, int issn, String isbn, String tomo, String editorial) {
        this.idDc = idDc;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.epoca = epoca;
        this.anio = anio;
        this.numero = numero;
        this.issn = issn;
        this.isbn = isbn;
        this.tomo = tomo;
        this.editorial = editorial;
    }

    public Integer getIdDc() {
        return idDc;
    }

    public void setIdDc(Integer idDc) {
        this.idDc = idDc;
    }

    public Date getPeriodoMes() {
        return periodoMes;
    }

    public void setPeriodoMes(Date periodoMes) {
        this.periodoMes = periodoMes;
    }

    public Date getPeriodoAnio() {
        return periodoAnio;
    }

    public void setPeriodoAnio(Date periodoAnio) {
        this.periodoAnio = periodoAnio;
    }

    public String getEpoca() {
        return epoca;
    }

    public void setEpoca(String epoca) {
        this.epoca = epoca;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getIssn() {
        return issn;
    }

    public void setIssn(int issn) {
        this.issn = issn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
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
        hash += (idDc != null ? idDc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publicacion)) {
            return false;
        }
        Publicacion other = (Publicacion) object;
        if ((this.idDc == null && other.idDc != null) || (this.idDc != null && !this.idDc.equals(other.idDc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "escom.libreria.info.articulo.jpa.Publicacion[idDc=" + idDc + "]";
    }

}
