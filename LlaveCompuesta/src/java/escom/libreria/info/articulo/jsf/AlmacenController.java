package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.Almacen;

import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.AlmacenFacade;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.proveedor.ProveedorArticulo;
import escom.libreria.info.proveedor.ejb.ProveedorArticuloFacade;
import java.io.Serializable;
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

@ManagedBean (name="almacenController")
@SessionScoped
public class AlmacenController implements Serializable{

    private Almacen current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenFacade ejbFacade;
    @EJB private ProveedorArticuloFacade proveedorArticuloFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AlmacenController() {
    }

    public Almacen getSelected() {
        if (current == null) {
            current = new Almacen();
            selectedItemIndex = -1;
        }
        return current;
    }
    public List<Almacen> getListaAlmacen(){
        return getFacade().findAll();
    }

    public List<Almacen> getListAlmacensExistenciaCero(){
       List<Almacen> l= getFacade().getArticulosExistenciaero();
       return l;
    }

    private AlmacenFacade getFacade() {
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

    private List<ProveedorArticulo>  proveedorArticulos;//proveedor que tiene asignados este articulo;

    public List<ProveedorArticulo> getProveedorArticulos() {
        return proveedorArticulos;
    }

    public void setProveedorArticulos(List<ProveedorArticulo> proveedorArticulos) {
        this.proveedorArticulos = proveedorArticulos;
    }

    public String prepareView(Almacen p) {
        current=p;
       proveedorArticulos=proveedorArticuloFacade.buscarProveedor(p.getIdArticulo());
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Almacen();
        selectedItemIndex = -1;
        return "Create";
    }


    public void prepareSeleccionArticulo(Articulo articulo){
        getSelected().setArticulo(articulo);
        JsfUtil.addSuccessMessage("ARTICULO SELECCIONADO SATISFACTORIAMENTE");
    }
    public String create() {
        Almacen  buscarAlmacen=null;
        try {

            buscarAlmacen=getFacade().find(current.getArticulo().getId());
            if(buscarAlmacen==null){
                current.setIdArticulo(current.getArticulo().getId());
                current.setArticulo(current.getArticulo());
                current.setExistencia(current.getEnConsigna()+current.getEnFirme());
                getFacade().create(current);
                JsfUtil.addSuccessMessage(("Almacen Creado Satisfactoriamente"));
                return prepareView(current);
            }

            JsfUtil.addErrorMessage("El articulo ya fue registrado anteriormente");
            return "/almacen/Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error al crear almacen ");
            return null;
        }
    }

    public String prepareEdit(Almacen p) {
        current=p;
        current.setArticulo(current.getArticulo());
        return "/almacen/Edit";
    }



    public String update() {
        try {

          
            current.setArticulo(current.getArticulo());
            current.setIdArticulo(current.getArticulo().getId());
            current.setExistencia(current.getEnConsigna()+current.getEnFirme());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Almacen actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar Almacen"));
            return null;
        }
    }

    public String destroy(Almacen p) {
        current=p;
        /*selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();*/
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Almacen eliminado satisfacotiramente");
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
            JsfUtil.addSuccessMessage(("AlmacenDeleted"));
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

    @FacesConverter(forClass=Almacen.class)
    public static class AlmacenControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AlmacenController controller = (AlmacenController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "almacenController");
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
            if (object instanceof Almacen) {
                Almacen o = (Almacen) object;
                return getStringKey(o.getIdArticulo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+AlmacenController.class.getName());
            }
        }

    }

}
