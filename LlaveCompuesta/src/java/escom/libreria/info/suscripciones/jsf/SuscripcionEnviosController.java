package escom.libreria.info.suscripciones.jsf;

import escom.libreria.info.suscripciones.SuscripcionEnvios;
import escom.libreria.info.suscripciones.SuscripcionEnviosPK;
import escom.libreria.info.suscripciones.jsf.util.JsfUtil;
import escom.libreria.info.suscripciones.jsf.util.PaginationHelper;
import escom.libreria.info.suscripciones.ejb.SuscripcionEnviosFacade;
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

@ManagedBean (name="suscripcionEnviosController")
@SessionScoped
public class SuscripcionEnviosController implements Serializable{

    private SuscripcionEnvios current;
    private DataModel items = null;
    @EJB private escom.libreria.info.suscripciones.ejb.SuscripcionEnviosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public SuscripcionEnviosController() {
    }


    public List<SuscripcionEnvios> getListSuscripcionEnvios(){
        List<SuscripcionEnvios> l=getFacade().findAll();
        return l;
    }
    public SuscripcionEnvios getSelected() {
        if (current == null) {
            current = new SuscripcionEnvios();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SuscripcionEnviosFacade getFacade() {
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

    public String prepareView(SuscripcionEnvios se) {
        current=se; //
        se.setPedido(se.getPedido());
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SuscripcionEnvios();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            SuscripcionEnviosPK pk=new SuscripcionEnviosPK();
            pk.setIdArticulo(current.getArticulo().getId());
            pk.setIdPedido(current.getPedido().getPedidoPK().getIdPedido());
            pk.setIdSuscripcion(current.getSuscripcion().getSuscripcionPK().getIdSuscripcion());
            current.setSuscripcionEnviosPK(pk);
            current.setArticulo(current.getArticulo());
            current.setEstadoEnvio(current.getEstadoEnvio());
            current.setPedido(current.getPedido());
            current.setObservaciones(current.getObservaciones());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Suscripcion Envios creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Suscripcion Envios"));
            return null;
        }
    }

    public String prepareEdit(SuscripcionEnvios se) {
        current=se; //
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Suscripcion Envios Actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar Suscripcion Envios"));
            return null;
        }
    }

    public String destroy(SuscripcionEnvios se) {
        current=se; //
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Suscripcion Envio eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("SuscripcionEnviosDeleted"));
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

    @FacesConverter(forClass=SuscripcionEnvios.class)
    public static class SuscripcionEnviosControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SuscripcionEnviosController controller = (SuscripcionEnviosController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "suscripcionEnviosController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.suscripciones.SuscripcionEnviosPK getKey(String value) {
            escom.libreria.info.suscripciones.SuscripcionEnviosPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.suscripciones.SuscripcionEnviosPK();
            key.setIdPedido(Integer.parseInt(values[0]));
            key.setIdSuscripcion(Integer.parseInt(values[1]));
            key.setIdArticulo(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(escom.libreria.info.suscripciones.SuscripcionEnviosPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdPedido());
            sb.append(SEPARATOR);
            sb.append(value.getIdSuscripcion());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SuscripcionEnvios) {
                SuscripcionEnvios o = (SuscripcionEnvios) object;
                return getStringKey(o.getSuscripcionEnviosPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+SuscripcionEnviosController.class.getName());
            }
        }

    }

}
