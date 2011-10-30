/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.jpa;

import java.util.Date;

/**
 *
 * @author xxx
 */
public abstract class CriteriosBusqueda {
   	private String  Autor;
	private String  Titulo;
	private String  Tema;
	private Date  Periodo;// (Mes Año)
	private int  Numero;
	private int  ISSN ;
	private String  ISBN ;
	private String  Editorial;
	private String  categoria;
	private String  Asunto; //(Dublin core)
        private String general;
        private String tipoArticulo;
        private String selectCategoria;

    public String getSelectCategoria() {
        return selectCategoria;
    }

    public void setSelectCategoria(String selectCategoria) {
        this.selectCategoria = selectCategoria;
    }
        

    public String getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(String tipoArticulo) {
        this.tipoArticulo = tipoArticulo;
    }


    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

        

    public String getAsunto() {
        return Asunto;
    }

    public void setAsunto(String Asunto) {
        this.Asunto = Asunto;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    public String getEditorial() {
        return Editorial;
    }

    public void setEditorial(String Editorial) {
        this.Editorial = Editorial;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getISSN() {
        return ISSN;
    }

    public void setISSN(int ISSN) {
        this.ISSN = ISSN;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int Numero) {
        this.Numero = Numero;
    }

    public Date getPeriodo() {
        return Periodo;
    }

    public void setPeriodo(Date Periodo) {
        this.Periodo = Periodo;
    }

    public String getTema() {
        return Tema;
    }

    public void setTema(String Tema) {
        this.Tema = Tema;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Título) {
        this.Titulo = Título;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

        
}
