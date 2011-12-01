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




import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jsf.ArticuloController;
import escom.libreria.info.articulo.jsf.PublicacionController;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.mime.ejb.MimeFacade;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
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
import javax.servlet.http.HttpServletResponse;
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
    private String urlDownloads="C:/Users/xxx/Documents/expedienteXML/";///home/libreria/public_ftp/incoming/";
    private String urlXML="C:/Users/xxx/Documents/expedienteXML/";//"/home/libreria/public_ftp/";
    private static final int BUFFER_SIZE = 10240;
    private static final int DEFAULT_BUFFER_SIZE = 10240;
    private String imagemTemporaria;
    private String extension,menssageOut;
    @EJB private escom.libreria.info.procesarEditorialXML.Editorialfacade editorialfacade;
    @EJB private escom.libreria.mime.ejb.MimeFacade mimeFacade;

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

/*public void crearArchivo(byte[] bytes, String arquivo) {
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
   }*/

    public void handleFileDocumentUpload(FileUploadEvent event) {
        try{
             posExtension=-1;
             String documento="";
             setMenssageOut("");
             articuloController.getSelected().setArchivo("");
              if(descargarArchivo(event,urlDownloads)){
                  try{
                      documento=event.getFile().getFileName();
                      posExtension=documento.indexOf(".");
                  }catch(Exception e){setMenssageOut("Error "+e.getMessage());}

                  if( posExtension==-1 || documento==null){
                     setMenssageOut("El archivo, no cuenta con extension!");
                  }else
                  {
                     extension=documento.substring(posExtension);
                     articuloController.getSelected().setFormatoDigital(extension);
                     articuloController.getSelected().setArchivo(documento);
                     setMenssageOut("El archivo "+documento +" fue  Cargado Satisfactoriamente");
                   }

              }

        }catch(Exception e){setMenssageOut("No fue posible subir el archivo");}
    }

   private byte[] bufferTemporal = new byte[BUFFER_SIZE];
  
    public boolean descargarArchivo(FileUploadEvent event,String urlFile) {
      
       
      System.out.println("Ruta a guardar[ " + urlFile + event.getFile().getFileName());
        
        try {
            FileOutputStream fileOutputStream = null;
            File result = null;
            result = new File(urlFile+ event.getFile().getFileName());
           
            fileOutputStream = new FileOutputStream(result);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bulk;
            InputStream inputStream = event.getFile().getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                 }
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

   /* public boolean descargarArchivoXML(FileUploadEvent event) {


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
}*/
    private int posExtension;
    public void handleFileUpload(FileUploadEvent event) {
                setMenssageOut("");
                String nombrePortada="";
                articuloController.getSelected().setImagen("");
               if(descargarArchivo(event,carpetaPortadas)){
                    nombrePortada=event.getFile().getFileName();   
                    articuloController.getSelected().setImagen(urlPortada+nombrePortada);
               }else
               setMenssageOut("No fue posible cargar el archivo");
      }

    public void handleFileUploadXML(FileUploadEvent event){
        if(descargarArchivo(event,urlXML)){
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

    private void download(String path, String nameFile, String tipoArchivo) throws IOException {

        // Prepare.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        File file = new File(path,nameFile);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        //String url = "archivoPdf?path=" + path + "&fileName=" + nameFile + "&fileType=txt";
        try {
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

            // Init servlet response.
            response.reset();
            response.setContentType(tipoArchivo);

            response.setContentLength(Long.valueOf(file.length()).intValue());
            response.setHeader("Content-disposition", "attachment; filename=\"" + nameFile + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }
        facesContext.responseComplete();
    }
    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it. It may be useful to
                // know that this will generally only be thrown when the client aborted the download.
                e.printStackTrace();
            }
        }
    }

    public String descargarArchivoDocumento(Articulo articulo){
        try {
            String nombre = articulo.getArchivo();
            String formato=articulo.getFormatoDigital().replaceAll(".","");
            String mime=mimeFacade.buscarMimeType(formato);
            
            download(urlDownloads, nombre, mime);

        } catch (Exception ex) {

           JsfUtil.addErrorMessage("El documento que intenta descargar no se encuentra disponible");
           // ex.printStackTrace();
            Logger.getLogger(SubirFiles.class.getName()).log(Level.SEVERE, null, ex);
             return null;
        }
        return null;
    }




   }

