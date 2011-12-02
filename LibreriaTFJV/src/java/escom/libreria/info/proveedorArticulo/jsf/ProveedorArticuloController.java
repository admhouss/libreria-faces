package escom.libreria.info.proveedorArticulo.jsf;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jsf.ArticuloController;
import escom.libreria.info.proveedor.jpa.ProveedorArticulo;
import escom.libreria.info.proveedorArticulo.jsf.util.JsfUtil;
import escom.libreria.info.proveedorArticulo.jsf.util.PaginationHelper;
import escom.libreria.info.proveedorArticulo.ejb.ProveedorArticuloFacade;
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
public class ProveedorArticuloController {

    private ProveedorArticulo current;
    private DataModel items = null;
    @EJB private escom.libreria.info.proveedorArticulo.ejb.ProveedorArticuloFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{articuloController}")
    private ArticuloController articuloController;

    public ArticuloController getArticuloController() {
        return articuloController;
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




    public String prepareView() {
        current = (ProveedorArticulo)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public List<ProveedorArticulo> getListProveedorArticulo(){
           Articulo articulo=articuloController.getSelected();
           List<ProveedorArticulo> l=getFacade().buscarProveedor(articulo.getId().intValue());
           return l;
    }


    public String prepareCreate() {
        current = new ProveedorArticulo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/ProveedorArticulo").getString("ProveedorArticuloCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/ProveedorArticulo").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ProveedorArticulo)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/ProveedorArticulo").getString("ProveedorArticuloUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/ProveedorArticulo").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ProveedorArticulo)getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/ProveedorArticulo").getString("ProveedorArticuloDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/ProveedorArticulo").getString("PersistenceErrorOccured"));
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

        escom.libreria.info.proveedor.jpa.ProveedorArticuloPK getKey(String value) {
            escom.libreria.info.proveedor.jpa.ProveedorArticuloPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.proveedor.jpa.ProveedorArticuloPK();
            key.setIdProveedor(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.proveedor.jpa.ProveedorArticuloPK value) {
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
