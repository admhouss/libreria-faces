package escom.libreria.info.articulo.jsf;

import escom.libreria.comun.ValidarNumero;
import escom.libreria.info.administracion.Actividadusuario;
import escom.libreria.info.administracion.ActividadusuarioPK;
import escom.libreria.info.administracion.Usuarioadministrativo;
import escom.libreria.info.administracion.ejb.ActividadusuarioFacade;
import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.administracion.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.CriteriosBusqueda;
import escom.libreria.info.articulo.Publicacion;
import escom.libreria.info.articulo.ejb.PublicacionFacade;
import escom.libreria.info.bitacoras.BitacoraCliente;
import escom.libreria.info.bitacoras.BitacoraClientePK;
import escom.libreria.info.bitacoras.ejb.BitacoraClienteFacade;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.login.sistema.SistemaController;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


@ManagedBean (name="publicacionController")
@SessionScoped
public class PublicacionController extends CriteriosBusqueda implements Serializable{

    private Publicacion current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.PublicacionFacade ejbFacade;
    @EJB private BitacoraClienteFacade ejbFacadeBitacora;
    @EJB private ActividadusuarioFacade ejbActividadusuarioFacade;
    @EJB private ArticuloFacade articuloFacade;
    private int tipoArticuloMostrar=-1;

    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Publicacion> listaPublicacion;
    private List<Publicacion> listPublicacionByBusqueda;//libros
    private List<Publicacion> listPublicacionAccesorio;//accesorios
    private List<Publicacion> listPublicacionSuscripcion;//suscripciones
    private List<Articulo> listArticulo;
   // private boolean showList ;//mostrar libos ,accesorios,suscripciones

    private String banderaCategoria="";
    private int redireccionarTo;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    private static  Logger logger = Logger.getLogger(PublicacionController.class.getName());

    public List<Articulo> getListArticulo() {
        return listArticulo;
    }

    public void setListArticulo(List<Articulo> listArticulo) {
        this.listArticulo = listArticulo;
    }

    public int getTipoArticuloMostrar() {
        return tipoArticuloMostrar;
    }

    public void setTipoArticuloMostrar(int tipoArticuloMostrar) {
        this.tipoArticuloMostrar = tipoArticuloMostrar;
    }



    public void prepareSeleccionArticulo(Articulo articulo){
        getSelected().setArticulo(articulo);
        JsfUtil.addSuccessMessage("ARTICULO SELECCIONADO SATISFACTORIAMENTE");
    }


    public List<Publicacion> getListPublicacionAccesorio() {
        return listPublicacionAccesorio;
    }

    public void setListPublicacionAccesorio(List<Publicacion> listPublicacionAccesorio) {
        this.listPublicacionAccesorio = listPublicacionAccesorio;
    }

    public List<Publicacion> getListPublicacionSuscripcion() {
        return listPublicacionSuscripcion;
    }

    public void setListPublicacionSuscripcion(List<Publicacion> listPublicacionSuscripcion) {
        this.listPublicacionSuscripcion = listPublicacionSuscripcion;
    }





    public String getBanderaCategoria() {
        return banderaCategoria;
    }

    public void setBanderaCategoria(String banderaCategoria) {
        this.banderaCategoria = banderaCategoria;
    }

public List<String> getListaString(){
    if(getSelectCategoria()==null)
        setSelectCategoria("p.articulo.creador");
    List<String> l= getFacade().buscarDinamica(getSelectCategoria());
    return  l;
}



     public PublicacionController() {
        {
            
        }
     }//constructor
       


    public List<String> complete(String cadena){
       List<String>  autores=null;
       autores=articuloFacade.buscarAutor(cadena.trim());
       if(autores==null)
       autores=new ArrayList<String>();
       return autores;
    }
     public boolean isActivate(){//retorna true si es diferente de null
        return (listPublicacionByBusqueda==null || listPublicacionByBusqueda.isEmpty())?false:true;
    }


     public List<Publicacion> getListLibros(){
        List<Publicacion> l=getFacade().getListLibros();
        return l;
     }


     public List<Publicacion> getListLibroByCategoria(){
           
            return listPublicacionByBusqueda;
    }

 private int opt;
    @PostConstruct
    public void init() {
             if(getBanderaCategoria()!=null && !getBanderaCategoria().trim().equals("")){

                   //ValidarNumero validar=new ValidarNumero();
                   //showList=false;
                 // if(validar.validarNumero(getBanderaCategoria())){
                   // opt =Integer.parseInt(banderaCategoria);
                    //setCategoria(getFacade().getCategoria(opt));
                   // listPublicacionByBusqueda=getFacade().buscarLibroByCategoria(getCategoria());
                  //}else{
                      if(getBanderaCategoria().trim().equalsIgnoreCase("libros")){
                          setCategoria("Publicaciones");
                          listPublicacionByBusqueda=getFacade().getPublicaciones();
                          tipoArticuloMostrar=1;
                      }else if(getBanderaCategoria().trim().equalsIgnoreCase("accesorios")){
                          setCategoria("Accesorios");
                          listPublicacionByBusqueda=getFacade().buscarAccesorio();
                          tipoArticuloMostrar=1;
                      }else if(getBanderaCategoria().trim().equalsIgnoreCase("productos")){

                         tipoArticuloMostrar=3;
                         setCategoria("Productos");
                         listPublicacionAccesorio=getFacade().buscarAccesorio();
                         listPublicacionByBusqueda=getFacade().getPublicaciones();
                         listPublicacionSuscripcion=getFacade().getListSuscripciones();
                      } else{
                           tipoArticuloMostrar=5;
                            setCategoria(getBanderaCategoria());


                      listPublicacionByBusqueda=getFacade().getAsuntoArticulos(getBanderaCategoria());
                       logger.info("ASUNTOS"+getBanderaCategoria().trim());
                      }
                  //}

             }
     }

     private void addbicatoraUsuarioAdministrador(Publicacion p,int selectOperacion){
         try{
          if(sistemaController!=null && sistemaController.getUsuarioAdministrador()!=null){
              Usuarioadministrativo user=sistemaController.getUsuarioAdministrador();
              Actividadusuario actividad=new Actividadusuario();
              ActividadusuarioPK pK=new ActividadusuarioPK();
              pK.setIdUsuario(user.getIdUsuario());
              //pK.setIdActividad(u);
              actividad.setActividadusuarioPK(pK);
              actividad.setFecha(new Date());
              actividad.setUsuarioadministrativo(user);

              switch(selectOperacion){
                  case 1://YES
                      actividad.setActividad("CONSULTO PULIBACION: "+ p.getArticulo().getTitulo());
                      actividad.setQuery("SELECT t1.ID_DC, t1.ANIO, t1.ISSN, t1.EDITORIAL, t1.PERIODO_ANIO, t1.ISBN, t1.PERIODO_MES, t1.EPOCA, t1.NUMERO, t1.TOMO, t1.ID_ARTICULO FROM articulo t0, publicacion t1 WHERE (((t0.FECHA_REGISTRO >= ?) AND (t0.FECHA_REGISTRO <= ?)) AND (t0.ID = t1.ID_ARTICULO))"+ " AND t0.ID=" +p.getIdDc()+"ORDER BY t1.titulo DESC;");
                   break;
                    case 2://NO
                      actividad.setActividad("ACTUALIZACION PULIBACION:" + p.getArticulo().getTitulo());
                      actividad.setQuery("UPDATE from Publicacion AS p WHERE p.ID_DC="+p.getIdDc()+" AND p.ID_ARTICULO=a.ID ORDER BY a.titulo DESC;");
                   break;
                    case 3://YES
                      actividad.setActividad("ELIMINO PULIBACION: " + p.getArticulo().getTitulo());
                      actividad.setQuery("DELETE FROM publicacion AS p,articulo AS a WHERE p.ID_DC="+p.getIdDc()+" AND p.ID_ARTICULO=a.ID ORDER BY a.titulo DESC;");
                   break;
                   default://NO
                      actividad.setActividad("AGREGO PULIBACION" + p.getArticulo().getTitulo());
                      actividad.setQuery("INSERT INTO  publicacion AS p,articulo AS a WHERE p.ID_DC="+p.getIdDc()+" AND p.ID_ARTICULO=a.ID ORDER BY a.titulo DESC;");
              }

              ejbActividadusuarioFacade.create(actividad);
              logger.info("BITACORA DE USUARIO ADMINISTRATIVO  CREADA SATISFACTORIAMENTE");

          }
         }catch(Exception e){

             logger.error("ERROR AL CREAR ACTIVIDAD USUARIO ", e);
         }
     }



     public Publicacion getOfertaDia(){

        Publicacion oferta=getFacade().getOfertaDelDia();
        return oferta;
     }
    public String buscar(){
        
        String queryTemporal="SELECT p FROM Publicacion p WHERE ";
        int acciones=0;//0,1,2;

        if(getAsunto().trim()!=null  && !getAsunto().trim().equals("")){
         queryTemporal+="p.articulo.asunto LIKE :asunto ";
         acciones=1;
        }

        if(getAutor().trim()!=null  && !getAutor().trim().equals("")){
            switch(acciones){
                case 0:
                queryTemporal+="p.articulo.creador LIKE :autor ";
                acciones=1;
                break;
                case 1:
                queryTemporal+="AND p.articulo.creador LIKE :autor ";
                acciones=1;
                break;

            }
        }
           
       
        if(getTitulo().trim()!=null  && !getTitulo().trim().equals("")){
                switch(acciones){
                 case 0:
                    queryTemporal+="p.articulo.titulo LIKE :titulo ";
                    acciones=1;
                 break;
                case 1:
                    queryTemporal+="AND p.articulo.titulo LIKE :titulo ";
                   acciones=1;
                 break;
                }

        }
        
        if(getTipoArticulo().trim()!=null  && !getTipoArticulo().trim().equals("")){
             switch(acciones){
                 case 0:
                   queryTemporal+="p.articulo.tipoArticulo.descripcion LIKE :tipo ";
                    acciones=1;
                 break;
                  case 1:
                   queryTemporal+="AND p.articulo.tipoArticulo.descripcion LIKE :tipo ";
                  acciones=1;
                 break;
            }
        }
        
        if(getPeriodo()!=null ){
            switch(acciones){
                 case 0:
                    queryTemporal+="p.periodoMes =:periodo ";
                    acciones=1;
                 break;
                 case 1:
                    queryTemporal+="OR p.periodoMes =:periodo ";
                    acciones=1;
                 break;
            }
        }

        if(getISBN().trim()!=null  && !getISBN().trim().equals("")){
             switch(acciones){
                 case 0:
                    queryTemporal+="p.isbn LIKE :ISBN ";
                    acciones=1;
                 break;

             case 1:
                queryTemporal+="AND p.isbn LIKE :ISBN ";
                acciones=1;
              break;

        }
        }
      

        if(getEditorial().trim()!=null  && !getEditorial().trim().equals("")){
           switch(acciones){
                 case 0:
                queryTemporal+="p.editorial LIKE :editorial ";
                acciones=1;
                break;
           case 1:
                queryTemporal+="AND p.editorial LIKE :editorial ";
                acciones=1;
                break;
            }
         }

        if(getISSN().trim()!=null  && !getISSN().trim().equals("")){

            switch(acciones){
                 case 0:
                 queryTemporal+="p.issn=:ISSN ";
                 acciones=1;
                break;
            case 1:
                 queryTemporal+="AND p.issn=:ISSN ";
                 acciones=1;
                break;

        }
        }

         if(getNumero().trim()!=null && !getNumero().trim().equals("") ){

             switch(acciones){
                 case 0:
                  queryTemporal+="p.numero =:numero ";
                   acciones=1;
                break;
            case 1:
                
                 queryTemporal+="AND p.numero =:numero ";
                   acciones=1;
                   break;
             }
            }
       
        queryTemporal+="ORDER BY p.articulo.titulo ASC";


        //System.out.println(queryTemporal);





        
       



        
         

        listPublicacionByBusqueda=getFacade().buscarArticuloDinamico(getAutor(),getTitulo(),getTipoArticulo(),getPeriodo(),getNumero(),getISSN(),getISBN(),getEditorial(),getAsunto(),queryTemporal);
        if(!isActivate())
        JsfUtil.addSuccessMessage("No se encontrarn ninguna coincidencias");
        return "/busqueda/List";
    
    }



     public String buscarDinamica(){
        if(getSelectCategoria()!=null && getGeneral().trim()!=null && !getGeneral().trim().equals("")){
          listPublicacionByBusqueda=getFacade().buscarDinamica(getSelectCategoria(), getGeneral());
         }
        if(!isActivate())
        JsfUtil.addSuccessMessage("No se encontrarn ninguna coincidencias");
        setGeneral("");
        return "/busqueda/List";
    }
       public String buscarDinamicaPeriodo(){
        if(getSelectCategoria()!=null && getPeriodo()!=null)
        listPublicacionByBusqueda=getFacade().buscarDinamicaPeriodo(getSelectCategoriaPeriodo(), getPeriodo());
        if(!isActivate())
        JsfUtil.addSuccessMessage("No se encontrarn ninguna coincidencias");
        //setPeriodo(null);
        return "/busqueda/List";
    }



     public String buscarLibroRelacionados(Publicacion p){

              
       Articulo articulo=p.getArticulo();

             listPublicacionByBusqueda=getFacade().buscarArticulo
            (articulo.getCreador(),articulo.getTitulo(), articulo.getTipoArticulo().getDescripcion(),p.getNumero(), p.getIssn(), p.getIsbn(),p.getEditorial(),articulo.getAsunto(),
            p.getTomo(),articulo.getUnidad(),articulo.getDivisa(),articulo.getFormato(),articulo.getPublicador(),articulo.getCodigo());
     
       addBitacoraCliente(p);
       addbicatoraUsuarioAdministrador(p,1);
       if(!isActivate())
        JsfUtil.addSuccessMessage("No se encontraron coincidencias!");
       return "/busqueda/List";

    }

    public List<Publicacion> getListNovedadesPublicacion(){
            listPublicacionByBusqueda=getFacade().getPublicaciones();
            return listPublicacionByBusqueda;
    }
     

   public String prepareListByCategoria_one(int i,int render){
        try {
            banderaCategoria = i+"";
            redireccionarTo = render;
            //setCategoria(getFacade().getCategoria(banderaCategoria));
             ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            String go = externalContext.getRequestContextPath()+"/faces/busqueda/ListCategoria.xhtml";
            externalContext.redirect(go);
            return null;
        } catch (Exception ex) {
           // Logger.getLogger(PublicacionController.class.getName()).log(Level.SEVERE, null, ex);
            logger.info("Error de direccionamiento");
        }
        return null;
    }
    public Publicacion getSelected() {
        if (current == null) {
            current = new Publicacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PublicacionFacade getFacade() {
        return ejbFacade;
    }

    public List<Publicacion> getListPublicacionByBusqueda() {
        return listPublicacionByBusqueda;
    }



   
    public String buscarLibroGeneral(){
        ValidarNumero v=new ValidarNumero();
        int numero=0;

        numero=v.validarNumero(getGeneral())?Integer.parseInt(getGeneral()):-1;
        setGeneral(getGeneral().trim()==null?"":getGeneral());


       
       listPublicacionByBusqueda=getFacade().buscarPublicaciones_Articulo(getGeneral(), numero);
              
       if(!isActivate()){
        JsfUtil.addSuccessMessage("No se encontraron coincidencias!");
       }
       setGeneral("");
       return "/busqueda/List";
    }

    public void setListPublicacionByBusqueda(List<Publicacion> listPublicacionByBusqueda) {
        this.listPublicacionByBusqueda = listPublicacionByBusqueda;
    }

    public String volverIndex(){
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            String go = externalContext.getRequestContextPath()+"/";
            externalContext.redirect(go);
            return "";
        } catch (IOException ex) {
           logger.error(ex, ex);
        }
        return "";
    }
     public String volverBusquedaList(){
         String go="";
         String tem;
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            go = externalContext.getRequestContextPath();
            externalContext.redirect(go+"/faces/busqueda/List.xhtml");
            return "";
        } catch (IOException ex) {
           logger.error(ex, ex);
        }
        return "";
    }
     public String volverBusquedaListCategoria(){
         String go="";
         String tem;
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
             go = externalContext.getRequestContextPath();
            externalContext.redirect(go+"/faces/busqueda/ListCategoria.xhtml");
            return "";
        } catch (IOException ex) {
           logger.error(ex, ex);
        }
        return "";
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        
        return "/publicacion/List";
    }
/*
 *
 * 1: Novedades
 * 2: Categoria
 * 3: Busqueda

 */

    private void addBitacoraCliente(Publicacion p){
           try{
        if(sistemaController!=null && sistemaController.getCliente()!=null){
            BitacoraClientePK bitacoraPK=new BitacoraClientePK();
            BitacoraCliente bitacoraCliente=new BitacoraCliente();
            bitacoraPK.setIdArticulo(p.getArticulo().getId());
            bitacoraPK.setIdCliente(sistemaController.getCliente().getId());
            bitacoraCliente.setBitacoraClientePK(bitacoraPK);
            bitacoraCliente.setArticulo(p.getArticulo());
            bitacoraCliente.setFecha(new Date());
            bitacoraCliente.setEstado(true);
            bitacoraCliente.setCliente(sistemaController.getCliente());
            ejbFacadeBitacora.create(bitacoraCliente);
        }
        }catch(Exception e){
            
          logger.error("ERROR AL AGREGAR ARTICULO Y CLIENTE A LA BITACOTRA CLIENTE", e);

        }
    }
    public String prepareView(Publicacion p,int render) {


        addBitacoraCliente(p);
        addbicatoraUsuarioAdministrador(p,1);
        redireccionarTo=render;
        current=p;
        return "/publicacion/ViewVenta";
    }
    public String prepareView1(Publicacion p,int render) {


        addBitacoraCliente(p);
        redireccionarTo=render;
        current=p;
        return "/publicacion/ViewVenta";
    }
     public String prepareView(Publicacion p) {
        //redireccionarTo=render;
        //addbicatoraUsuarioAdministrador(p,1);
        current=p;
        return "/publicacion/View";
    }

    public String prepareCreate() {
        current = new Publicacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

    /*          Publicacion publica=getFacade().find(current.getIdDc());
              if(publica!=null){
                  JsfUtil.addErrorMessage("El ID de la publicacion ya se existe");
                  return  null;
              }
*/
              getFacade().create(current);
              JsfUtil.addSuccessMessage(("Publicacion Creada Satisfactoriamente"));
          
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return prepareView(current);
    }

    public String prepareEdit(Publicacion p) {
        current=p;
        current.setArticulo(current.getArticulo());
        return "/publicacion/Edit";

    }

    public String update() {
        try {

            current.setArticulo(current.getArticulo());
            getFacade().edit(current);
          //  addbicatoraUsuarioAdministrador(current, 2);
            JsfUtil.addSuccessMessage(("Publicacion Actualizada Satisfactoriamente"));
            return "/publicacion/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Publicacion p) {
        current=p;
        getListaPublicacion().remove(current);
        getFacade().remove(current);
        //addbicatoraUsuarioAdministrador(p, 3);
        JsfUtil.addSuccessMessage("Publicacion Eliminada Satisfactoriamente");
        return "/publicacion/List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PublicacionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass=Publicacion.class)
    public static class PublicacionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PublicacionController controller = (PublicacionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "publicacionController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Publicacion) {
                Publicacion o = (Publicacion) object;
                return getStringKey(o.getIdDc());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+PublicacionController.class.getName());
            }
        }

    }

     
    public int getRedireccionarTo() {
        return redireccionarTo;
    }

    public void setRedireccionarTo(int redireccionarTo) {
        this.redireccionarTo = redireccionarTo;
    }


    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }

    public List<Publicacion> getListaPublicacion() {

        /*UNA PUBLICACION ES TIPO_ARTICULO={LIBRO,CD,DVD,REVISTA}*/

            List<Publicacion> p=getFacade().getPublicaciones();
            return p;
           //return getFacade().findAll();
    }

    public void setListaPublicacion(List<Publicacion> listaPublicacion) {
        this.listaPublicacion = listaPublicacion;
    }
    

      public List<String> editorialesList;

    public List<String> getEditorialesList() {
        return editorialesList;
    }

    public void setEditorialesList(List<String> editorialesList) {
        this.editorialesList = editorialesList;
    }
}
