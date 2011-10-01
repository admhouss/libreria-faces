package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.ArticuloFacade;
import escom.libreria.info.articulo.jpa.Promocion;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.faces.view.facelets.FaceletContext;

@ManagedBean (name="articuloController")
@SessionScoped
public class ArticuloController implements Serializable {

    private Articulo current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.ArticuloFacade ejbFacade;
    @EJB private escom.libreria.info.articulo.ejb.ImpuestoFacade impuestoFacade;
    @EJB private escom.libreria.info.articulo.ejb.PromocionFacade promocionFacade;
    @EJB private escom.libreria.info.articulo.ejb.DescuentoArticuloFacade  descuentoFacade;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenFacade almacenFacade;
    

    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int formward;//local
    private String anio,titulo,editorial,resumen,autor,general;
    private String catego;
    private String categoria;
    private int opc=-1;
    //Titulo Dinamico tipo de categoria

    @ManagedProperty("#{impuestoController}")
    ImpuestoController impuestoController; //descargando beann impuesto de session
    @ManagedProperty("#{descuentoController}")
    ArticuloController descuentoController; //descargando bean  descuento de session;
    @ManagedProperty("#{promocionController}")
    PromocionController promController; //descargando bean promocion de session;

    public AlmacenController getAlmacenController() {
        return almacenController;
    }

    public void setAlmacenController(AlmacenController almacenController) {
        this.almacenController = almacenController;
    }

    public ArticuloController getDescuentoController() {
        return descuentoController;
    }

    public void setDescuentoController(ArticuloController descuentoController) {
        this.descuentoController = descuentoController;
    }

    public ImpuestoController getImpuestoController() {
        return impuestoController;
    }

    public void setImpuestoController(ImpuestoController impuestoController) {
        this.impuestoController = impuestoController;
    }

    public PromocionController getPromController() {
        return promController;
    }

    public void setPromController(PromocionController promController) {
        this.promController = promController;
    }
    @ManagedProperty("#{almanceController}")
    AlmacenController almacenController; //descargando bean almancen de session;



    public String getGeneral() {
        return getFacade().getCategoria(opc);
        
    }

    public void setGeneral(String general) {
        this.general = general;
    }


   public String prepareViewVenta(Articulo articulo){
       current=articulo;
       return "/articulo/ViewVenta";
   }


    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    




    public ArticuloController() {
    }

    public Articulo getSelected() {
        if (current == null) {
            current = new Articulo();
            selectedItemIndex = -1;
        }
        return current;
    }
   
    private List<Articulo> listaLibros; //para vender
    private List<Articulo> listaNovedades;//principal cuando se carga la pagina

    public List<Articulo> getListaNovedades() {
          if(listaNovedades==null){
             listaNovedades=getFacade().buscarNovedades();
           }
        return listaNovedades;
    }

    public void setListaNovedades(List<Articulo> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }


    public List<Articulo> getListLibroByCategoria(){

           listaLibros=getFacade().buscarLibroByCategoria(opc);
           categoria=getFacade().getCategoria(opc);
          
           return listaLibros;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isActivate(){

        return (listaLibros==null || listaLibros.isEmpty())?false:true;

         /*if(listaLibros==null || listaLibros.isEmpty()){
            return false;//no muestres el panel
         }

         return true;
          * *
          */
    }
    public List<Articulo> getListaLibros() {
      //      if(listaLibros==null )
        //   listaLibros=getFacade().findAll();


        
        return listaLibros;
    }

    public void setListaLibros(List<Articulo> listaLibros) {
        this.listaLibros = listaLibros;
    }

    private void limpiarVariables(){
       setAnio("");setAutor("");
       setEditorial("");setResumen("");setTitulo("");
     
    }
     public String buscar(){
        anio=anio==null?" ":getAnio().trim();
        titulo=titulo==null?" ":getTitulo();
        editorial=editorial==null?" ":getEditorial();
        autor=autor==null?" ":getAutor();
        resumen=resumen==null?" ":getResumen(); 
        listaLibros= getFacade().buscarLibro(titulo,autor,editorial,resumen,anio);
        limpiarVariables();
        return "List";
    }

     public String buscarGeneral(){
         general=general==null?" ":getGeneral().trim();
         return "#";
         
     }

     public String buscarLibroGeneral(){
         titulo=titulo==null?" ":getTitulo();
         listaLibros=getFacade().buscarLibro( titulo,  titulo, titulo,  titulo, titulo);
         return "/busqueda/List";
     }

   

    private ArticuloFacade getFacade() {
        return ejbFacade;
    }

    public List<Articulo>  getListArticulos(){
        List<Articulo>l=getFacade().findAll();
        return l;
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
    public String regresarBusqueda(){
        return "/busqueda/List";
    }

    public String prepareList() {
        String ir=null;
        if(formward==1){
        recreateModel();
        return "List";
        }else if(formward==2){
           
            ir="/promocion/Create";
             formward=1;
        }else{
             ir="/promocion/Edit";
        }
        
         return ir;
    }


    public String prepareLibroBuscar(Articulo p){
         current=p;
        return "/busqueda/View";
    }
    public String prepareLibro(Articulo p){
        current=p;
        return "View";
    }

    public String prepareView(Articulo p,int go) {
      current=p;
      formward=go;
      
      if(go==1){
          promController.setListPromocion(promocionFacade.buscarPromocionByarticulo(p));
          impuestoController.setListaImpuesto(impuestoFacade.buscarImpuestoByarticulo(p));

          return "/articulo/View";
       }
      else if(go==2)
        return "./../articulo/View.xhtml";
      else
         return "/face/articulo/View.xhtml";
    }

    public String prepareCreate() {
        current = new Articulo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Articulo Created"));
            return prepareView(current,1);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Articulo p) {
      current=p;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Articulo Updated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Articulo p) {
      current=p;
      getFacade().remove(p);
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
      JsfUtil.addSuccessMessage("Articulo eliminado satisfactoriamente");

        return "List";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ArticuloDeleted"));
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

    @FacesConverter(forClass=Articulo.class)
    public static class ArticuloControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArticuloController controller = (ArticuloController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "articuloController");
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
            if (object instanceof Articulo) {
                Articulo o = (Articulo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ArticuloController.class.getName());
            }
        }

    }


     public String prepareListByCategoria_one(int i){
        try {
             opc=i;
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            String go = externalContext.getRequestContextPath()+"/faces/busqueda/ListCategoria.xhtml";
            externalContext.redirect(go);
           
        } catch (IOException ex) {
            Logger.getLogger(ArticuloController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
      


     public String prepareListByCategoriaNovedades(int op){
         opc=op;
         return "./../busqueda/ListCategoria.xhtml" ;
      }
     public String prepareListByCategoria(int i){
         opc=i;

        return "/busqueda/ListCategoria.xhtml";
    }
}
