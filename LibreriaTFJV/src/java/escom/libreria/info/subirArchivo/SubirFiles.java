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
import java.util.logging.Level;
import java.util.logging.Logger;
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


    private String urlFile="/home/libreria/www/articulos/";                ///"C:/Users/xxx/Documents/NetBeansProjects/respaldo/LibreriaTFJV/web/resources/images/";
    private String urlVirtual="http://www.libreria-tfjfa.com/articulos/"; //"/resources/images/";
    private static final int BUFFER_SIZE = 9124;
    private String imagemTemporaria;
    private CroppedImage croppedImage;
    private String extension;
 
    @ManagedProperty("#{articuloController}")
    ArticuloController articuloController;

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
        int posicion=-1;
              if(descargarArchivo(event)){

                      urlVirtual+=event.getFile().getFileName();
                      posicion=event.getFile().getFileName().indexOf(".");
                  if(posicion==-1){
                        JsfUtil.addErrorMessage("El archivo, no cuenta con extension!");
                  }else{
                     extension=event.getFile().getFileName().substring(posicion);
                     articuloController.getSelected().setFormato(extension);
                     articuloController.getSelected().setFormatoDigital(extension);
                     articuloController.getSelected().setArchivo(urlVirtual);
                   }

              }
      



    }

   private byte[] bufferTemporal = new byte[BUFFER_SIZE];
  
    public boolean descargarArchivo(FileUploadEvent event) {
       // ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
       
      System.out.println("Ruta a guardar[ " + urlFile + event.getFile().getFileName());
        
        try {
            FileOutputStream fileOutputStream = null;
            File result = null;
            result = new File(urlFile + event.getFile().getFileName());
           
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
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logo 1 no puede ser cargado", ""));
        return false;
    }
}
    public void handleFileUpload(FileUploadEvent event) {

            //System.out.println(event.getFile().getFileName());
 int posicion=-1;
                
               if(descargarArchivo(event)){

                      urlVirtual+=event.getFile().getFileName();
                      posicion=event.getFile().getFileName().indexOf(".");
                  if(posicion==-1){
                        JsfUtil.addErrorMessage("El archivo, no cuenta con extension!");
                  }else{
                     extension=event.getFile().getFileName().substring(posicion);
                     articuloController.getSelected().setImagen(urlVirtual);
                   }
                
                
               }else{
                  JsfUtil.addErrorMessage("No fue posible cargar el archivo");
               }
               
                
    }
     
    


}

