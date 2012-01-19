package escom.libreria.info.facturacion.jsf;

import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.jsf.AlmacenController;
import escom.libreria.info.articulo.jsf.ImpuestoController;
import escom.libreria.info.articulo.jsf.PromocionController;

import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.descuentos.ejb.DescuentoArticuloFacade;
import escom.libreria.info.descuentos.jsf.DescuentoArticuloController;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.proveedor.Proveedor;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ProveedorArticuloPK;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;
import escom.libreria.info.proveedor.ejb.ProveedorFacade;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.model.DualListModel;


@ManagedBean (name="articuloController")
@SessionScoped
public class ArticuloController implements Serializable {

    private Articulo current;
    private DataModel items = null;
    @EJB private ArticuloFacade ejbFacade;
    @EJB private escom.libreria.info.articulo.ejb.ImpuestoFacade impuestoFacade;
    @EJB private escom.libreria.info.articulo.ejb.PromocionFacade promocionFacade;
    @EJB private DescuentoArticuloFacade  descuentoFacade;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenFacade almacenFacade;
    @EJB private escom.libreria.info.facturacion.ejb.TipoArticuloFacade tipoArticuloejbFacade;
    @EJB private ProveedorFacade proveedorFacade;
    @EJB private ProveedorArticuloFacade proveedorArticuloFacade;

    //private DualListModel<Proveedor> proveedores=null;
    private PaginationHelper pagination;
    private int selectedItemIndex,formward;
    private String catego;
    private String categoria;
    private int opc=-1;
    private int cantidad;

    //Titulo Dinamico tipo de categoria

    @ManagedProperty("#{impuestoController}")
    private ImpuestoController impuestoController; //descargando beann impuesto de session
    @ManagedProperty("#{descuentoArticuloController}")
    private DescuentoArticuloController  descuentoController; //descargando bean  descuento de session;
    @ManagedProperty("#{promocionController}")
    private PromocionController promController; //descargando bean promocion de session;
    @ManagedProperty("#{almanceController}")
    private AlmacenController almacenController; //descargando bean almancen de session;

    public AlmacenController getAlmacenController() {
        return almacenController;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


/*TRAE ARTICULOS CON TIPO ARTICULO CD,DVD,REVISTA Y LIBRO*/
    public List<Articulo> getTipoArticuloPublicaciones(){
         List<Articulo> publicaciones=getFacade().getPublicaciones();
         return publicaciones;
    }
    
    /*TRAE ARTICULOS CON TIPO ARTICULO SUSCRIPCION*/
    public List<Articulo> getTipoArticuloSuscripcion(){
         List<Articulo> publicaciones=getFacade().getSuscripcion();
         return publicaciones;
    }

    public void setAlmacenController(AlmacenController almacenController) {
        this.almacenController = almacenController;
    }

    public DescuentoArticuloController getDescuentoController() {
        return descuentoController;
    }



    public void setDescuentoController(DescuentoArticuloController descuentoController) {
        this.descuentoController = descuentoController;
    }

    public String prepreaListByArticulo(Articulo p){
        current=p;
        return "/articulo/ViewProveedor";
    }





    public String prepareListProveedorArticulo(){
        return "/articulo/proveedor_articulo/Create";
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





 public String eliminarProveedorArticulo(ProveedorArticulo p){
      current.getProveedorArticuloList().remove(p);
      proveedorArticuloFacade.remove(p);
      //getFacade().borrarProveedorArticulo(p.getProveedor().getId(),p.getArticulo().getId());
      JsfUtil.addSuccessMessage("Proveedor Elimado satisfactoriamente");
      return "/articulo/ViewProveedor";

  }



   public String prepareViewVenta(Articulo articulo){
       current=articulo;
       return "/articulo/ViewVenta";
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




    /*private void limpiarVariables(){
       setAnio("");setAutor("");
       setEditorial("");setResumen("");setTitulo("");

    }*/
    /* public String buscar(){
        anio=anio==null?" ":getAnio().trim();
        titulo=titulo==null?" ":getTitulo();
        editorial=editorial==null?" ":getEditorial();
        autor=autor==null?" ":getAutor();
        resumen=resumen==null?" ":getResumen();
        listaLibros= getFacade().buscarLibro(editorial,editorial,editorial,editorial,editorial);
        limpiarVariables();
        return "List";
    }*/

    /* public String buscarGeneral(){
         general=general==null?" ":getGeneral().trim();
         return "#";

     }*/

     /*public String buscarLibroGeneral(){
         titulo=titulo==null?" ":getTitulo();
         editorial=editorial==null?"":getEditorial();
         listaLibros=getFacade().buscarLibro(editorial,editorial ,editorial, editorial, editorial);
         return "/busqueda/List";
     }*/



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
        return "/articulo/List";
    }


    public String prepareLibroBuscar(Articulo p){
         current=p;
        return "/busqueda/View";
    }
    public String prepareLibro(Articulo p){
        current=p;
        return "View";
    }

    public String prepareView(Articulo p) {
      current=p;
      //current.setProveedorList(current.getProveedorList());
      return "/articulo/View";
    }

    public String prepareCreate() {
        current = new Articulo();
        selectedItemIndex = -1;
        return "/articulo/Create";
    }

   private Proveedor proveedor;

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String createArticulo(){


        ProveedorArticulo pa=proveedorArticuloFacade.buscarArticuloProveedor(current.getId(),proveedor.getId());
        if(pa==null){ //el articulo no tiene asignado este proveedor
           Almacen almacen=almacenFacade.find(current.getId()); //buscamos el articulo que se encuentra en almacen
           if(almacen==null){//no se encuentra en almacen..

               JsfUtil.addErrorMessage(("El articulo no se encuentra en Almacen"));
            }
            else{
                ProveedorArticulo proveedor_articulo=new ProveedorArticulo();
                ProveedorArticuloPK primaryKey=new  ProveedorArticuloPK();
                proveedor_articulo.setArticulo(current);
                proveedor_articulo.setCantidad(getCantidad());
                proveedor_articulo.setProveedor(proveedor);
                proveedor_articulo.setUltMof(new Date());
                proveedor_articulo.setProveedorArticuloPK(primaryKey);
                primaryKey.setIdArticulo(current.getId());
                primaryKey.setIdProveedor(proveedor.getId());

                current.setAsunto(current.getAsunto());
                current.setArchivo(current.getArchivo());
                current.setAgregacionRecurso(current.getAgregacionRecurso());
                current.setCodigo(current.getCodigo());
                current.setCosto(current.getCosto());
                current.setCreador(current.getCreador());
                current.setDescripcion(current.getDescripcion());
                current.setModUpdate(new  Date());
                current.setFechaRegistro(new Date());
                current.setFechaCreacion(current.getFechaCreacion());
                current.setFormato(current.getFormato());
                current.setFormatoDigital(current.getFormatoDigital());
                current.getProveedorArticuloList().add(proveedor_articulo);
                current.setImagen(current.getImagen());
                almacen.setEnConsigna(almacen.getEnConsigna()+getCantidad());
                almacen.setExistencia(almacen.getEnConsigna()+almacen.getEnFirme());
                getFacade().edit(current);
                almacenFacade.edit(almacen);
                JsfUtil.addSuccessMessage(("Proveedor agregado Sastisfactoriamente"));
            }//else
        }else
        JsfUtil.addErrorMessage(("Ya se encuentra asignado este proveedor"));
         return "/articulo/ViewProveedor";
    }
    public String create() {
        try {
            current.setAsunto(current.getAsunto());
            current.setArchivo(current.getArchivo());
            current.setAgregacionRecurso(current.getAgregacionRecurso());
            current.setCodigo(current.getCodigo());
            current.setCosto(current.getCosto());
            current.setCreador(current.getCreador());
            current.setDescripcion(current.getDescripcion());
            current.setModUpdate(new  Date());
            current.setFechaRegistro(new Date());
            current.setFechaCreacion(new Date());
            current.setFormato(current.getFormato());
            current.setFormatoDigital(current.getFormatoDigital());
           // current.setProveedorList(current.getProveedorList());
            current.setImagen(current.getImagen());
             getFacade().create(current);
            JsfUtil.addSuccessMessage(("Articulo Created"));
            return prepareView(current);
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

            current.setFormato(current.getFormato());
            current.setAlmacen(current.getAlmacen());
            current.setCodigo(current.getCodigo());
            current.setCosto(current.getCosto());
            current.setFormatoDigital(current.getFormatoDigital());
            current.setTipoArticulo(current.getTipoArticulo());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Articulo Actualizado Satisfactoriamente"));
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
