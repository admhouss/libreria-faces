package escom.libreria.info.proveedor.jsf;


import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.administracion.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.Almacen;
import escom.libreria.info.articulo.ejb.AlmacenFacade;

import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.jsf.ArticuloController;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ProveedorArticuloPK;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;
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

@ManagedBean (name="proveedorArticuloController")
@SessionScoped
public class ProveedorArticuloController implements Serializable{

    private ProveedorArticulo current;
    private DataModel items = null;
    @EJB private ProveedorArticuloFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{articuloController}")
    private ArticuloController articuloController;
    @EJB private AlmacenFacade almacenFacade;

    public ArticuloController getArticuloController() {
        return articuloController;
    }

    public List<ProveedorArticulo> getProveedorArticuloList(){

        List<ProveedorArticulo> pa=getFacade().findAll();
        return pa;
    }

    public void setArticuloController(ArticuloController articuloController) {
        this.articuloController = articuloController;
    }


    public ProveedorArticuloController() {
    }



    public ProveedorArticulo getSelected() {
        if (current == null) {
            current = new ProveedorArticulo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProveedorArticuloFacade getFacade() {
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




    public String prepareView( ProveedorArticulo p) {
        current =p;// (ProveedorArticulo)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/proveedorArticulo/View";
    }

    public List<ProveedorArticulo> getListProveedorArticulo(){
           Articulo articulo=articuloController.getSelected();
           List<ProveedorArticulo> l=getFacade().buscarProveedor(articulo.getId().intValue());
           return l;
    }

    private Articulo articuloSelected;

    public String prepareCreate() {
        current = new ProveedorArticulo();
        //articuloSelected=articulo;
        return "Create";
    }

    public String create() {
        try {


            ProveedorArticulo p=getFacade().buscarArticuloProveedor(current.getArticulo().getId(),current.getProveedor().getId());

            if(p==null){
            Almacen almacen=almacenFacade.find(current.getArticulo().getId()); //buscamos el articulo que se encuentra en almacen
           if(almacen==null){//no se encuentra en almacen..

               JsfUtil.addErrorMessage(("El articulo no se encuentra en Almacen"));
               return null;
            }
            
            ProveedorArticuloPK pk=new ProveedorArticuloPK();
            pk.setIdArticulo(current.getArticulo().getId());
            pk.setIdProveedor(current.getProveedor().getId());
            current.setArticulo(current.getArticulo());
            current.setCantidad(current.getCantidad());
            current.setUltMof(new Date());
            current.setProveedor(current.getProveedor());
            current.setProveedorArticuloPK(pk);

            //actualizar almacen
            getFacade().create(current);
            almacen.setEnConsigna(almacen.getEnConsigna()+current.getCantidad());
            almacen.setExistencia(almacen.getEnConsigna()+almacen.getEnFirme());
            getFacade().edit(current);
            almacenFacade.edit(almacen);
            JsfUtil.addSuccessMessage(("Proveedor Articulo Creado Satisfactoriamente"));
            return prepareView(current);
            }else{
                JsfUtil.addErrorMessage("Proveedor Articulo ya se encuentra registrado");
                return null;
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear proveedor articulo"));
            return null;
        }
    }

    public String prepareEdit(ProveedorArticulo p) {
        current = p;//(ProveedorArticulo)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/proveedorArticulo/Edit";
    }

    public String update() {
        try {

            Almacen almacen=almacenFacade.find(current.getArticulo().getId()); //buscamos el articulo que se encuentra en almacen
           if(almacen==null){//no se encuentra en almacen.
               JsfUtil.addErrorMessage(("El articulo no se encuentra en Almacen"));
               return null;
            }
            almacen.setEnConsigna(almacen.getEnConsigna()+current.getCantidad());
            almacen.setExistencia(almacen.getEnConsigna()+almacen.getEnFirme());
            getFacade().edit(current);
            almacenFacade.edit(almacen);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Proveedor Articulo Actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(ProveedorArticulo p) {
        current =p;
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Proveedor Articulo eliminado Satisfactoriamente");
        return "/proveedorArticulo/List";
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
            JsfUtil.addSuccessMessage(("ProveedorArticuloDeleted"));
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

    @FacesConverter(forClass=ProveedorArticulo.class)
    public static class ProveedorArticuloControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedorArticuloController controller = (ProveedorArticuloController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedorArticuloController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.proveedor.ProveedorArticuloPK getKey(String value) {
            escom.libreria.info.proveedor.ProveedorArticuloPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.proveedor.ProveedorArticuloPK();
            key.setIdProveedor(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.proveedor.ProveedorArticuloPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdProveedor());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ProveedorArticulo) {
                ProveedorArticulo o = (ProveedorArticulo) object;
                return getStringKey(o.getProveedorArticuloPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ProveedorArticuloController.class.getName());
            }
        }

    }

}
