package escom.libreria.info.articulo.jsf;

import escom.libreria.info.articulo.AlmacenPedido;
import escom.libreria.info.articulo.AlmacenPedidoPK;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;
import escom.libreria.info.articulo.ejb.AlmacenPedidoFacade;
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

@ManagedBean (name="almacenPedidoController")
@SessionScoped
public class AlmacenPedidoController implements Serializable{

    private AlmacenPedido current;
    private DataModel items = null;
    @EJB private escom.libreria.info.articulo.ejb.AlmacenPedidoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AlmacenPedidoController() {
    }

    public AlmacenPedido getSelected() {
        if (current == null) {
            current = new AlmacenPedido();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<AlmacenPedido> getListAlmacenPedido(){
       List<AlmacenPedido> almacenPedido= getFacade().findAll();
       return almacenPedido;
    }
    private AlmacenPedidoFacade getFacade() {
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

    public String prepareView(AlmacenPedido a) {
        current = a;//(AlmacenPedido)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/almacenPedido/View";
    }

    public String prepareCreate() {
        current = new AlmacenPedido();
        //selectedItemIndex = -1;
        return "/almacenPedido/Create";
    }

    public String create() {
        try {
            AlmacenPedidoPK pk=new AlmacenPedidoPK();
             pk.setIdPedido(current.getPedido().getPedidoPK().getIdPedido());
             pk.setIdArticulo(current.getPedido().getPedidoPK().getIdArticulo());
             current.setAlmacenPedidoPK(pk);
             current.setPedido(current.getPedido());
             current.setProcAlmacen(current.getProcAlmacen());
             current.setProveedor(current.getProveedor());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Almacen Pedido creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Almacen Pedido"));
            return null;
        }
    }

    public String prepareEdit(AlmacenPedido almacen) {
        current = almacen;//(AlmacenPedido)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/almacenPedido/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("AlmacenPedidoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(AlmacenPedido almacen) {
        current =almacen;// (AlmacenPedido)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
        getFacade().remove(almacen);
        JsfUtil.addSuccessMessage("Almacen pedio eliminado satisfactoriamente");
        return "/almacenPedido/List";
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
            JsfUtil.addSuccessMessage(("AlmacenPedidoDeleted"));
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

    @FacesConverter(forClass=AlmacenPedido.class)
    public static class AlmacenPedidoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AlmacenPedidoController controller = (AlmacenPedidoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "almacenPedidoController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.articulo.AlmacenPedidoPK getKey(String value) {
            escom.libreria.info.articulo.AlmacenPedidoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.articulo.AlmacenPedidoPK();
            key.setIdPedido(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.articulo.AlmacenPedidoPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdPedido());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AlmacenPedido) {
                AlmacenPedido o = (AlmacenPedido) object;
                return getStringKey(o.getAlmacenPedidoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+AlmacenPedidoController.class.getName());
            }
        }

    }

}
