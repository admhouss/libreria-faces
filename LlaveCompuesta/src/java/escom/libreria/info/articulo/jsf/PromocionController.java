package escom.libreria.info.articulo.jsf;

import escom.libreria.comun.GeneradorHTML;
import escom.libreria.comun.ValidarFechaFormat;
import escom.libreria.info.articulo.Promocion;
import escom.libreria.info.articulo.PromocionPK;

import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.PromocionFacade;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.generadorIDpromociones.GeneradorContador;
import escom.libreria.info.generadorIDpromociones.ejb.GeneradorContadorFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

@ManagedBean (name="promocionController")
@SessionScoped
public class PromocionController implements Serializable{

    private Promocion current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.PromocionFacade ejbFacade;
    @EJB private escom.libreria.info.cliente.ejb.ClienteFacade clieteFacade;
    @EJB private escom.libreria.correo.ProcesoJMail jMail;
    @EJB private ArticuloFacade articuloFacade;
    @EJB private GeneradorContadorFacade generadorContadorFacade;
    private Articulo articuloSeeccionado;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private PromocionPK p;
    private String value;
    private List<Articulo> listaArticulosPromociones;
    private List<Promocion> listaPromociones;


    private static  Logger logger = Logger.getLogger(PromocionController.class);




    public List<Promocion> getListaPromociones() {
        return listaPromociones;
    }

    public void setListaPromociones(List<Promocion> listaPromociones) {
        this.listaPromociones = listaPromociones;
    }


    public String buscar(){
         listaPromociones= getFacade().buscarPromocionByIndicie(codigoPromocion);
         return "/promocion/SendMail";
    }


    public List<Articulo> getListaArticulosPromociones() {

         listaArticulosPromociones=articuloFacade.findAll();

        return listaArticulosPromociones;
    }

    public void setListaArticulosPromociones(List<Articulo> listaArticulosPromociones) {
        this.listaArticulosPromociones = listaArticulosPromociones;
    }

   public List<Integer> getListaCogiosPromocion(){

           List<Integer> l=  getFacade().getIndicesPromocion();
           return l;
   }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

  public String prepareFinalizar(){

        codigoPromocion=null;
        current=null;
       return "/promocion/List";

  }

    public PromocionController() {
       // p=new PromocionPK();

    }

    public Integer getGeneradorPromociones(){
       Integer contador=generadorContadorFacade.count()+1;
       
       GeneradorContador g=new GeneradorContador();
       g.setContador(contador);
       generadorContadorFacade.create(g);
        return contador;
    }

    public Promocion getSelected() {
        if (current == null) {
            current = new Promocion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PromocionFacade getFacade() {
        return ejbFacade;
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
        recreateModel();
        return "List";
    }

    public String sendCorreo(){
      //  System.out.println("tamaño"+selectPromocion.length);
       // current=promocion;
        return "/promocion/SendMail";
    }

    private  List<Cliente> clientes;
    private List<String> destinatarios;
    
    public String correo(){
       clientes= clieteFacade.getListClientesActive();
       destinatarios=new ArrayList<String>();
       GeneradorHTML generarHTM=new GeneradorHTML();
       String   query="",fechaInicial,fechaFinal;

       for(Cliente cliente:clientes)
       destinatarios.add(cliente.getId().trim());

       if(destinatarios.size()>0)
       {
         for(Promocion p:listaPromociones)
         {
             fechaInicial=ValidarFechaFormat.getFechaFormateada(p.getDiaInicio());
             fechaFinal=ValidarFechaFormat.getFechaFormateada(p.getDiaFin());
             query+=generarHTM.generaPromocione("<br/>"+getValue(),p.getArticulo().getTitulo() ,p.getArticulo().getCreador(),p.getPrecioPublico()+"", p.getArticulo().getImagen(),fechaInicial,fechaFinal);
         }
         jMail.enviarCorreo("Promocion Libreria Còdigo:"+codigoPromocion, query, destinatarios);
         setValue("");
         JsfUtil.addSuccessMessage("Promocion Enviada Satisfactoriamente");

       }
       setValue("");
       query="";

       return "/promocion/List";

   }


    public String prepareView(Promocion p) {
        current=p;

        return "/promocion/View";
    }

    private Integer codigoPromocion;

    public Integer getCodigoPromocion() {
        return codigoPromocion;
    }

    public void setCodigoPromocion(Integer codigoPromocion) {
        this.codigoPromocion = codigoPromocion;
    }

    public String prepareCreate() {
        current = new Promocion();
        codigoPromocion=getGeneradorPromociones();
        return "Create";
    }
    public void InitialArticulo(Articulo a){

     // p=new PromocionPK();
     // p.setIdArticulo(a.getId());
     // current.setPromocionPK(p);
      //current.setArticulo(a);
      //listaArticulosPromociones.remove(a);
        articuloSeeccionado=a;
     JsfUtil.addSuccessMessage("Articulo seleccionado satisfactoriamente");
    }

    public void InitialArticulo_Actualizar(Articulo a){
        current=getSelected();
    //      System.out.println("current"+current);




           current.setPromocionPK(current.getPromocionPK());
           current.setDiaFin(current.getDiaFin());
           current.setDiaInicio(current.getDiaInicio());
           current.setPrecioPublico(current.getPrecioPublico());
           current.setArticulo(a);
           current.getPromocionPK().setId(current.getPromocionPK().getId());
           current.getPromocionPK().setIdArticulo(a.getId());

           current.setPromocionPK(current.getPromocionPK());



     JsfUtil.addSuccessMessage("Articulo actualizado satisfactoriamente");
     //return "Edit";
    }


    public String create() {
        try {

            boolean validarFechas=ValidarFechaFormat.getValidarFecha(current.getDiaInicio(),current.getDiaFin());
            if(!validarFechas){
                JsfUtil.addErrorMessage("La Fecha Final debe ser Mayor o Igual que Fecha Inicial");
                return null;
            }
            if(codigoPromocion!=null && articuloSeeccionado!=null)
            {
                    PromocionPK pk=new PromocionPK();
                    pk.setId(codigoPromocion);
                    pk.setIdArticulo(articuloSeeccionado.getId());
                    current.setArticulo(articuloSeeccionado);
                    current.setPromocionPK(pk);
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(("Promocion creada Satisfactoriamente"));
                    return "/promocion/Create";
            }
            JsfUtil.addErrorMessage("Error no se ha selecionado ningun articulo");
            return null;
        } catch (Exception e) {
            logger.error("Error al crear la promocion,CODIGO PROMOCION"+codigoPromocion,e);
            JsfUtil.addErrorMessage("Error al crear la promocion");
            return null;
        }
    }

    public String prepareEdit(Promocion prom) {
        current=prom;
        current.setArticulo(current.getArticulo());
        return "/promocion/Edit";
    }

 private List<Promocion>  listPromocion;

    public void setListPromocion(List<Promocion> listPromocion) {
        this.listPromocion = listPromocion;
    }
    public List<Promocion> getListPromocion(){
       List<Promocion> l =getFacade().findAll();
       return l;
    }
    public String update() {


        boolean validarFechas=ValidarFechaFormat.getValidarFecha(current.getDiaInicio(), current.getDiaFin());
        if(!validarFechas){
            JsfUtil.addErrorMessage("La Fecha Final debe ser Mayor o Igual que Fecha Incial");
            return null;

        }
        try {
           current=getSelected();
           current.setDiaFin(current.getDiaFin());
           current.setDiaInicio(current.getDiaInicio());
           current.setPrecioPublico(current.getPrecioPublico());
           current.setArticulo(current.getArticulo());
           current.setPromocionPK(current.getPromocionPK());
           current.getPromocionPK().setIdArticulo(current.getPromocionPK().getIdArticulo());
           current.getPromocionPK().setId(current.getPromocionPK().getId());
           getFacade().edit(current);
           JsfUtil.addSuccessMessage(("Promocion actualizada Satisfactoriamente"));
           return "View";
        } catch (Exception e) {
            logger.error("Error al actualizar promocion",e);
            JsfUtil.addErrorMessage(("Error al actualizar la promocion"));
            return null;
        }
    }

    public String destroy(Promocion p) {
        current=p;
        getFacade().remove(p);
        JsfUtil.addSuccessMessage("Promocion Eliminada Satisfactoriamente");
        logger.info("Promocion Eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("PromocionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Promocion.class)
    public static class PromocionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PromocionController controller = (PromocionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "promocionController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.articulo.PromocionPK getKey(String value) {
            escom.libreria.info.articulo.PromocionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.articulo.PromocionPK();
            key.setId(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.articulo.PromocionPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getId());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Promocion) {
                Promocion o = (Promocion) object;
                return getStringKey(o.getPromocionPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+PromocionController.class.getName());
            }
        }

    }

}
