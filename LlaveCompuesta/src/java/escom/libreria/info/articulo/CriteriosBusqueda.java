/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo;

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
	private String  Numero;
	private String  ISSN ;
	private String  ISBN ;
	private String  Editorial;
	private String  categoria;
	private String  Asunto; //(Dublin core)
        private String general;
        private String tipoArticulo;
        private String selectCategoria;
        private String selectCategoriaPeriodo;

    public String getSelectCategoria() {
        return selectCategoria;
    }

    public String getSelectCategoriaPeriodo() {
        return selectCategoriaPeriodo;
    }

    public void setSelectCategoriaPeriodo(String selectCategoriaPeriodo) {
        this.selectCategoriaPeriodo = selectCategoriaPeriodo;
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

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String Numero) {
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
