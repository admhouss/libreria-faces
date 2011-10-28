package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.jpa.Comentario;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.ComentarioFacade;
import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.cliente.jpa.Cliente;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean (name="comentarioController")
@SessionScoped
public class ComentarioController implements Serializable{

    private Comentario current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.ComentarioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String comentario;
    private List<Comentario> listaComentarios;
    @ManagedProperty("#{publicacionController}")
    PublicacionController publicacionController;

    public PublicacionController getPublicacionController() {
        return publicacionController;
    }

    public void setPublicacionController(PublicacionController publicacionController) {
        this.publicacionController = publicacionController;
    }


    public List<Comentario> getListaComentarios() {
        if(listaComentarios==null){
            List<Comentario> l=getFacade().getComentariosByArticulo(publicacionController.getSelected().getArticulo());
            if(l!=null)
            listaComentarios=l;
            

        }
        return listaComentarios;
    }

    public void setListaComentarios(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }




    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    private void  addBitacora(Cliente cliente ,Articulo articulo){

        //String Query
    }
    public String addComentario(Cliente cliente,Articulo articulo){

        if(cliente==null){
            JsfUtil.addErrorMessage("Usuario no identificado");
            return "/login/Create";
        }

        try{

             current=new Comentario();
             current.setArticulo(articulo);
             current.setAutor(cliente.getNombre()+" "+cliente.getPaterno());
             current.setComentario(comentario);
             current.setFechaComentario(new Date());
             getFacade().create(current);
             listaComentarios=getFacade().getComentariosByArticulo(articulo);
             JsfUtil.addSuccessMessage("Comentario Agregado Satisfactoriamente");
             setComentario("");
        }catch(Exception e){e.printStackTrace();}
        
        return "/busqueda/List";
    }

    public ComentarioController() {
    }



    public Comentario getSelected() {
        if (current == null) {
            current = new Comentario();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ComentarioFacade getFacade() {
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

    public String prepareView() {
        current = (Comentario)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Comentario();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Comentario").getString("ComentarioCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Comentario").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Comentario)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Comentario").getString("ComentarioUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Comentario").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Comentario)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Comentario").getString("ComentarioDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Comentario").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Comentario.class)
    public static class ComentarioControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ComentarioController controller = (ComentarioController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "comentarioController");
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
            if (object instanceof Comentario) {
                Comentario o = (Comentario) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ComentarioController.class.getName());
            }
        }

    }

}