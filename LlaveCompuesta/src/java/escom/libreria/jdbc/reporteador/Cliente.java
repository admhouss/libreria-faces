package escom.libreria.jdbc.reporteador;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cliente implements Serializable{


    private String correo;
    private String articuloTitulo;
    private String rfc;
    private BigDecimal porcentaje;
    private String concepto;

    public Cliente(String correo, String articuloTitulo, String rfc, BigDecimal porcentaje, String concepto) {
        this.correo = correo;
        this.articuloTitulo = articuloTitulo;
        this.rfc = rfc;
        this.porcentaje = porcentaje;
        this.concepto = concepto;
    }

    public Cliente() {

    }

    public String getArticuloTitulo() {
        return articuloTitulo;
    }

    public void setArticuloTitulo(String articuloTitulo) {
        this.articuloTitulo = articuloTitulo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }






            //PORCENTAJE,CONCEPTO,RFC,ARTICULO
}
