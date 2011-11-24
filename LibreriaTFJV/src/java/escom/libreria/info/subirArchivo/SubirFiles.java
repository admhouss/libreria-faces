/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.subirArchivo;



/**
 *
 * @author yamil
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import escom.libreria.info.articulo.jsf.ArticuloController;
import escom.libreria.info.articulo.jsf.PublicacionController;
import escom.libreria.info.articulo.jsf.util.JsfUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.Buffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


/**
 *
 * @author yamil
 */

@ManagedBean(name="subirArchivo")
@SessionScoped


public class SubirFiles  implements Serializable{

   
    private String carpetaPortadas="/home/libreria/www/articulos/";
    private String urlPortada="http://www.libreria-tfjfa.com/articulos/";
    private String urlDownloads="/home/libreria/public_ftp/incoming/";
    private String urlXML="C:/Users/xxx/Documents/expedienteXML/";//"/home/libreria/public_ftp/";
    private static final int BUFFER_SIZE = 9124;
    private String imagemTemporaria;
    private String extension,menssageOut;
    @EJB private escom.libreria.info.procesarEditorialXML.Editorialfacade editorialfacade;

    @ManagedProperty("#{articuloController}")
    private ArticuloController articuloController;
    @ManagedProperty("#{publicacionController}")
    private PublicacionController publicacionController;

    public String getMenssageOut() {
        return menssageOut;
    }

    public void setMenssageOut(String menssageOut) {
        this.menssageOut = menssageOut;
    }


    public ArticuloController getArticuloController() {
        return articuloController;
    }

    public void setArticuloController(ArticuloController articuloController) {
        this.articuloController = articuloController;
    }

    
   

    

    /** Creates a new instance of SubirPDF */
    public SubirFiles() {
    }

public void crearArchivo(byte[] bytes, String arquivo) {
      FileOutputStream fos;
      try {
         fos = new FileOutputStream(arquivo);
         fos.write(bytes);
         fos.close();
      } catch (FileNotFoundException ex) {
         ex.printStackTrace();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
   }

    public void handleFileUpload0(FileUploadEvent event) {
        try{
             posExtension=-1;
             String documento="";
             articuloController.getSelected().setArchivo("");
              if(descargarArchivo(event)){
                      documento=event.getFile().getFileName();
                      posExtension=documento.indexOf(".");
                  if( posExtension==-1 || documento==null){
                     setMenssageOut("El archivo, no cuenta con extension!");
                  }else
                  {
                     extension=documento.substring(posExtension);
                     articuloController.getSelected().setFormatoDigital(extension);
                     articuloController.getSelected().setArchivo(urlDownloads+documento);
                   }

              }

        }catch(Exception e){setMenssageOut("No fue posible subir el archivo");}
    }

   private byte[] bufferTemporal = new byte[BUFFER_SIZE];
  
    public boolean descargarArchivo(FileUploadEvent event) {
      
       
      System.out.println("Ruta a guardar[ " + carpetaPortadas + event.getFile().getFileName());
        
        try {
            FileOutputStream fileOutputStream = null;
            File result = null;
            result = new File(carpetaPortadas + event.getFile().getFileName());
           
            fileOutputStream = new FileOutputStream(result);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bulk;
            InputStream inputStream = event.getFile().getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                 }
       //       bufferTemporal[i++]=(byte)bulk;
             fileOutputStream.write(buffer, 0, bulk);
             fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
           return true;
        } catch (IOException e) {
        System.out.println("Error handleFileUpload" + e);
        return false;
    }
}

    public boolean descargarArchivoXML(FileUploadEvent event) {


      System.out.println("Ruta a guardar[ " +urlXML + event.getFile().getFileName());

        try {
            FileOutputStream fileOutputStream = null;
            File result = null;
            result = new File(urlXML + event.getFile().getFileName());

            fileOutputStream = new FileOutputStream(result);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bulk;
            InputStream inputStream = event.getFile().getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                 }
       //       bufferTemporal[i++]=(byte)bulk;
             fileOutputStream.write(buffer, 0, bulk);
             fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
           return true;
        } catch (IOException e) {
        System.out.println("Error handleFileUpload" + e);
        return false;
    }
}
    private int posExtension;
    public void handleFileUpload(FileUploadEvent event) {
                setMenssageOut("");
                posExtension=-1;
                String nombrePortada="";
                articuloController.getSelected().setImagen("");
               if(descargarArchivo(event)){
                    nombrePortada=event.getFile().getFileName();   
                    articuloController.getSelected().setImagen(urlPortada+nombrePortada);
               }else
                    setMenssageOut("No fue posible cargar el archivo");
      }

    public void handleFileUploadXML(FileUploadEvent event){
        if(descargarArchivoXML(event)){
          List<String>editoriales=editorialfacade.getEditorialByXML(urlXML+event.getFile().getFileName());
           publicacionController.setEditorialesList(editoriales);
        }
     }

    public PublicacionController getPublicacionController() {
        return publicacionController;
    }

    public void setPublicacionController(PublicacionController publicacionController) {
        this.publicacionController = publicacionController;
    }


   }

