/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.subirArchivo;

import escom.libreria.info.encriptamientoMD5.EncriptamientoImp;
import escom.libreria.info.facturacion.jsf.ArticuloController;
import escom.libreria.servidorConfiguracion.ejb.MimeFacade;
import javax.annotation.PostConstruct;



/**
 *
 * @author yamil
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */






import escom.libreria.info.articulo.jsf.PublicacionController;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;

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
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
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

   
    private static String carpetaPortadas="/home/libreria/www/articulos/";
    private static String urlPortada="http://www.libreria-tfjfa.com/articulos/";//"C:/Users/xxx/Documents/NetBeansProjects/respaldo/LibreriaTFJV/web/resources/images/";
    private static String urlDownloads="/home/libreria/public_ftp/incoming/"; // "C:/Users/xxx/Documents/expedienteXML/"; //
    private static String urlXML="/home/libreria/public_ftp/"; //"C:/Users/xxx/Documents/expedienteXML/";
    private static final int BUFFER_SIZE =9924;
    private static final int DEFAULT_BUFFER_SIZE =9024;
    private String imagemTemporaria;
    private String extension,menssageOut;
    @EJB private escom.libreria.info.procesarEditorialXML.Editorialfacade editorialfacade;
    @EJB private MimeFacade mimeFacade;
    @EJB private ArticuloFacade articuloFacade;
    private static  Logger logger = Logger.getLogger(SubirFiles.class);

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

                     articuloController.getSelected().setFormatoDigital("");
                     articuloController.getSelected().setArchivo("");
                     articuloController.getSelected().setFormatoDigital(extension);
                     articuloController.getSelected().setArchivo(documento);
                     setMenssageOut("El archivo "+documento +" fue  Cargado Satisfactoriamente");
                   }

              }

        }catch(Exception e){setMenssageOut("No fue posible subir el archivo");}
    }

   private byte[] bufferTemporal = new byte[BUFFER_SIZE];
  
    public boolean descargarArchivo(FileUploadEvent event,String urlFile) {
      
       
     
        try {
            FileOutputStream fileOutputStream = null;
            File result = null;
            result = new File(urlFile,event.getFile().getFileName());
           
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
private String key;/*VARIABLE QUE LEE BROWSER */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
byte[] enBytes;
   @PostConstruct
    public void init() {
System.out.println("Mensaje Leido:"+getKey());
         try{
             if(getKey()!=null && !getKey().equals(""))
             {
                     EncriptamientoImp  encriptamientoImp=new EncriptamientoImp();
                     enBytes=encriptamientoImp.hexToBytes(getKey());
                     String mensaje= encriptamientoImp.decrypt(enBytes);
                     StringTokenizer token=new StringTokenizer(mensaje, "|");
                     Integer  idArticulo=Integer.parseInt(token.nextToken());
                     Articulo articulo =articuloFacade.find(idArticulo);
                     descargarArchivoDocumento(articulo);
                    /* if(articulo.getArchivo()!=null)
                     {
                      int pos=articulo.getArchivo().indexOf(".");
                      String extension=articulo.getArchivo().substring(pos, articulo.getArchivo().length());
                      String mime=mimeFacade.buscarMimeType(extension.replaceAll(".", ""));
                      download(articulo.getArchivo(), mime);
                     //String prueba="C:/Users/xxx/Documents/logLibreriaTFJVF/TFJV.txt";
                     }*/


                     ///Integer pedido=Integer.parseInt(mensaje);

                    System.out.println("Articulo a descargar es:"+mensaje);
             }
         }catch(Exception e){

               e.printStackTrace();
         }

     }
 
    private int posExtension;
    public void handleFileUpload(FileUploadEvent event) {
            System.out.println("hola archivo");
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
          editorialfacade.getEditorialByXML("");
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

    public void download(String path, String nameFile, String tipoArchivo) throws IOException {

      System.out.println("descargando el archivo"+path+nameFile);
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
            if(formato!=null){
             String mime=mimeFacade.buscarMimeType(formato);
             download(urlDownloads, nombre, mime);
            }
            JsfUtil.addErrorMessage("El documento que intenta descargar no se encuentra disponible");

        } catch (Exception ex) {

           JsfUtil.addErrorMessage("El documento que intenta descargar no se encuentra disponible");
           // ex.printStackTrace();
           logger.error("El documento que intenta descargar no se encuentra disponible", ex);
             return null;
        }
        return null;
    }




   }

