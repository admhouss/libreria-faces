package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.PublicacionFacade;
import escom.libreria.info.articulo.jpa.CriteriosBusqueda;
import escom.libreria.info.cliente.jpa.BitacoraCliente;
import escom.libreria.info.cliente.jpa.BitacoraClientePK;
import escom.libreria.info.login.sistema.SistemaController;
import escom.libreria.info.usarioAdministrativo.jpa.Actividadusuario;
import escom.libreria.info.usarioAdministrativo.jpa.Usuarioadministrativo;
import java.io.IOException;
import java.io.Serializable;
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

@ManagedBean (name="publicacionController")
@SessionScoped
public class PublicacionController extends CriteriosBusqueda implements Serializable{

    private Publicacion current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.PublicacionFacade ejbFacade;
    @EJB private escom.libreria.info.cliente.ejb.BitacoraClienteFacade ejbFacadeBitacora;
    @EJB private escom.libreria.info.usarioAdministrativo.ejb.ActividadusuarioFacade ejbActividadusuarioFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Publicacion> listaPublicacion;
    private List<Publicacion> listPublicacionByBusqueda;
    private int selectCategoria;
    private int redireccionarTo;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;

     public PublicacionController() {}//constructor
       

     public boolean isActivate(){//retorna true si es diferente de null
        return (listPublicacionByBusqueda==null || listPublicacionByBusqueda.isEmpty())?false:true;
    }

     public List<Publicacion> getListLibroByCategoria(){
            setCategoria(getFacade().getCategoria(selectCategoria));
            listPublicacionByBusqueda=getFacade().buscarLibroByCategoria(getCategoria());
            return listPublicacionByBusqueda;
    }

     private void addbicatoraUsuarioAdministrador(Publicacion p,int selectOperacion){
          if(sistemaController!=null && sistemaController.getUsuarioAdministrador()!=null){
              Usuarioadministrativo user=sistemaController.getUsuarioAdministrador();
              Actividadusuario actividad=new Actividadusuario();
              actividad.setFecha(new Date());
              actividad.setUsuarioadministrativo(user);
              switch(selectOperacion){
                  case 1://YES
                      actividad.setActividad("CONSULTO PULIBACION: "+ p.getArticulo().getTitulo());
                      actividad.setQuery("SELECT p.* FROM publicacion AS p,articulo AS a WHERE p.ID_DC="+p.getIdDc()+" AND p.ID_ARTICULO=a.ID ORDER BY a.titulo DESC;");
                   break;
                    case 2://NO
                      actividad.setActividad("ACTUALIZACION PULIBACION:" + p.getArticulo().getTitulo());
                      actividad.setQuery("UPDATE WHERE p.ID_DC="+p.getIdDc()+" AND p.ID_ARTICULO=a.ID ORDER BY a.titulo DESC;");
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

          }
     }
    
    public String buscar(){
        listPublicacionByBusqueda=getFacade().buscarArticulo(getAutor(),getTitulo(),getTema(),getPeriodo(),getNumero(),getISSN(),getISBN(),getEditorial(),getAsunto());
        if(!isActivate())
        JsfUtil.addSuccessMessage("No se encontrarn ninguna coincidencias");
        return "/busqueda/List";
    }

     public String buscarLibroRelacionados(Publicacion p){
       listPublicacionByBusqueda=getFacade().buscarArticulo(p.getArticulo().getCreador(),p.getArticulo().getTitulo(),p.getArticulo().getDescripcion(),p.getPeriodoMes(),p.getNumero(), p.getIssn(),p.getIsbn(),p.getEditorial(),p.getArticulo().getAsunto());
       addBitacoraCliente(p);
       addbicatoraUsuarioAdministrador(p,1);
       if(!isActivate())
       JsfUtil.addSuccessMessage("No se encontraron coincidencias!");
       return "/busqueda/List";
    }

    public List<Publicacion> getListNovedadesPublicacion(){
            listPublicacionByBusqueda=getFacade().buscarArticuloNovedades();
            if(!isActivate())
            JsfUtil.addSuccessMessage("No se encontrarn ninguna coincidencias");
            return listPublicacionByBusqueda;
    }
     

    public String prepareListByCategoria_one(int i,int render){
        try {
            selectCategoria = i;
            redireccionarTo = render;
            setCategoria(getFacade().getCategoria(selectCategoria));
            JsfUtil.getDispacher("/faces/busqueda/ListCategoria.xhtml");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(PublicacionController.class.getName()).log(Level.SEVERE, null, ex);
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
       listPublicacionByBusqueda=getFacade().buscarArticulo(getGeneral(),getGeneral(),getGeneral(),new Date(), 1, 1, getGeneral(),getGeneral(), getGeneral());
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
            Logger.getLogger(PublicacionController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PublicacionController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PublicacionController.class.getName()).log(Level.SEVERE, null, ex);
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
        }catch(Exception e){e.printStackTrace();}
    }
    public String prepareView(Publicacion p,int render,int operacion) {

        
        addBitacoraCliente(p);
        addbicatoraUsuarioAdministrador(p,operacion);
        redireccionarTo=render;
        current=p;
        return "/publicacion/ViewVenta";
    }
    public String prepareView(Publicacion p,int render) {


        addBitacoraCliente(p);
        redireccionarTo=render;
        current=p;
        return "/publicacion/ViewVenta";
    }
     public String prepareView(Publicacion p) {
        //redireccionarTo=render;
        addbicatoraUsuarioAdministrador(p,1);
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
            if(current!=null){
              getFacade().create(current);
              JsfUtil.addSuccessMessage(("Publicacion Creada Satisfactoriamente"));
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return prepareView(current);
    }

    public String prepareEdit(Publicacion p) {
        current=p;
        return "/publicacion/Edit";

    }

    public String update() {
        try {
            getFacade().edit(current);
            addbicatoraUsuarioAdministrador(current, 2);
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
        addbicatoraUsuarioAdministrador(p, 3);
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
           return getFacade().findAll();
    }

    public void setListaPublicacion(List<Publicacion> listaPublicacion) {
        this.listaPublicacion = listaPublicacion;
    }
    

}
