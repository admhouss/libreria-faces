package escom.libreria.info.suscripciones.jsf;

import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.carrito.jsf.CarritoController;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.login.ejb.SistemaFacade;
import escom.libreria.info.suscripciones.Suscripcion;
import escom.libreria.info.suscripciones.SuscripcionPK;
import escom.libreria.info.suscripciones.jsf.util.JsfUtil;
import escom.libreria.info.suscripciones.jsf.util.PaginationHelper;
import escom.libreria.info.suscripciones.ejb.SuscripcionFacade;
import java.io.Serializable;
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

@ManagedBean (name="suscripcionController")
@SessionScoped
public class SuscripcionController implements Serializable{
    @ManagedProperty("#{carritoController}")
    private CarritoController carritoController;
    private Suscripcion current;
    private DataModel items = null;
    @EJB private escom.libreria.info.suscripciones.ejb.SuscripcionFacade ejbFacade;
    @EJB private SistemaFacade sistemaFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int suscripcion;
    private List<Suscripcion> listasuscripciones;

    public CarritoController getCarritoController() {
        return carritoController;
    }

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }


    public List<Suscripcion> getListasuscripciones() {
        return listasuscripciones;
    }

    public void setListasuscripciones(List<Suscripcion> listasuscripciones) {
        this.listasuscripciones = listasuscripciones;
    }



public String buscar(){

    listasuscripciones=getFacade().getSuscripcionByID(suscripcion);
    return "/suscripcionVentas/List";
}
    public int getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(int suscripcion) {
        this.suscripcion = suscripcion;
    }



    public SuscripcionController() {
    }

    public Suscripcion getSelected() {
        if (current == null) {
            current = new Suscripcion();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Suscripcion> getListSuscripcion(){
       List<Suscripcion> l=getFacade().findAll();
       return l;
    }

    public List<Integer> getIdSuscripciones(){
       List<Integer> l= getFacade().getIdSuscripciones();
       return l;
    }

    private SuscripcionFacade getFacade() {
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


    public String agregarArticulo(){

      //List<Articulo> articulos= getFacade().getArticulosByID(suscripcion);
        //          carritoController.agregarArticulo(null)



        //idDelasusico

        return "";

    }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView(Suscripcion p) {
        current=p;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Suscripcion();
        
        return "Create";
    }

    private int idSuscripcion;//idSuscripcion

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    /*public Object crearCarritoCompra(){
        CarritoCompraTemporalLocal carrito = sistemaFacade.getObtenerBandejaTemporal();
    }*/
    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }


    public String create() {

        try {
            SuscripcionPK pk=new SuscripcionPK();
            pk.setIdArticulo(current.getArticulo().getId());
            pk.setIdSuscripcion(idSuscripcion);

            current.setSuscripcionPK(pk);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Suscripcion creada Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear suscripcion"));
            return null;
        }
    }

    public String prepareEdit(Suscripcion p) {
        current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Suscripcion Actualizada Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar suscripcion"));
            return null;
        }
    }

    public String destroy(Suscripcion p) {
        current=p;
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Suscripcion eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("SuscripcionDeleted"));
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

    @FacesConverter(forClass=Suscripcion.class)
    public static class SuscripcionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SuscripcionController controller = (SuscripcionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "suscripcionController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.suscripciones.SuscripcionPK getKey(String value) {
            escom.libreria.info.suscripciones.SuscripcionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.suscripciones.SuscripcionPK();
            key.setIdSuscripcion(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.suscripciones.SuscripcionPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdSuscripcion());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Suscripcion) {
                Suscripcion o = (Suscripcion) object;
                return getStringKey(o.getSuscripcionPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+SuscripcionController.class.getName());
            }
        }

    }

}
