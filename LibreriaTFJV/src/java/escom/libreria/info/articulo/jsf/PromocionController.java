package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.jpa.Promocion;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.PromocionFacade;
import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.PromocionPK;
import escom.libreria.info.cliente.jpa.Cliente;
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

@ManagedBean (name="promocionController")
@SessionScoped
public class PromocionController implements Serializable{

    private Promocion current;

    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.PromocionFacade ejbFacade;
    @EJB private escom.libreria.info.cliente.ejb.ClienteFacade clieteFacade;
     @EJB private escom.libreria.correo.ProcesoJMail jMail;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private PromocionPK p;


    public PromocionController() {
       // p=new PromocionPK();

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

    public void correo(Promocion promocion){
       List<Cliente> l= clieteFacade.getListClientesActive();
       List<String> destinatarios=new ArrayList<String>();
       for(Cliente cliente:l)
       destinatarios.add(cliente.getEmail().trim());
       if(destinatarios.size()>0)
       jMail.enviarCorreo("Nueva Promocion sobre el Articulo"+promocion.getArticulo().getIdIdc().getEditorial(), "Esto es una Prueba de corre responsable Yamil, proyecto actual libreria", destinatarios);
       
   }


    public String prepareView(Promocion p) {
        current=p;
       
        return "/promocion/View";
    }

    public String prepareCreate() {
        current = new Promocion();
        selectedItemIndex = -1;
        return "Create";
    }
    public void InitialArticulo(Articulo a){
      p=new PromocionPK();
      p.setIdArticulo(a.getId());
      current.setPromocionPK(p);
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
            if(current.getPromocionPK()==null ){
             JsfUtil.addErrorMessage(" Asegurese que ha seleccionado un Articulo");
             return prepareCreate();
            }else{
            current.setArticulo(current.getArticulo());
            current.setPromocionPK(current.getPromocionPK());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Promocion").getString("PromocionCreated"));
            return prepareView(current);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Promocion").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Promocion prom) {
        current=prom;
        System.out.println("PROMOCION PK:"+current.getPromocionPK().getId());
        return "/promocion/Edit";
    }

 private List<Promocion>  listPromocion;

    public void setListPromocion(List<Promocion> listPromocion) {
        this.listPromocion = listPromocion;
    }
    public List<Promocion> getListPromocion(){
        return getFacade().findAll();
    }
    public String update() {
        int id=0;
        try {
           current=getSelected();
           //Promocion prom=getFacade().find(current.getPromocionPK().getId());
                
           current.setDiaFin(current.getDiaFin());
           current.setDiaInicio(current.getDiaInicio());
           current.setPrecioPublico(current.getPrecioPublico());
           current.setArticulo(current.getArticulo());
           current.setPromocionPK(current.getPromocionPK());
           current.getPromocionPK().setIdArticulo(current.getPromocionPK().getIdArticulo());
           current.getPromocionPK().setId(current.getPromocionPK().getId());


          
             // selectedItemIndex=current.getPromocionPK().getId();
               getFacade().edit(current);
              // System.out.println("ACTUALIZAR PROMOCION PK:"+current.getPromocionPK());
        

           
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Promocion").getString("PromocionUpdated"));
            return "View";
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Promocion").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Promocion p) {
        current=p;
       getFacade().remove(p);
       JsfUtil.addSuccessMessage("Promocion Eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Promocion").getString("PromocionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Promocion").getString("PersistenceErrorOccured"));
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

        escom.libreria.info.articulo.jpa.PromocionPK getKey(String value) {
            escom.libreria.info.articulo.jpa.PromocionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.articulo.jpa.PromocionPK();
            key.setId(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.articulo.jpa.PromocionPK value) {
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
