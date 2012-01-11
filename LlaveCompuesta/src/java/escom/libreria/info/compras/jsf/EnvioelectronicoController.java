package escom.libreria.info.compras.jsf;

import escom.libreria.info.compras.Envioelectronico;
import escom.libreria.info.compras.EnvioelectronicoPK;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.compras.jsf.util.PaginationHelper;
import escom.libreria.info.compras.ejb.EnvioelectronicoFacade;
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

@ManagedBean (name="envioelectronicoController")
@SessionScoped
public class EnvioelectronicoController implements Serializable{

    private Envioelectronico current;
    private DataModel items = null;
    @EJB private escom.libreria.info.compras.ejb.EnvioelectronicoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public EnvioelectronicoController() {
    }

    public List<Envioelectronico> getListEnvioElectronicos(){
        List<Envioelectronico> l=getFacade().findAll();
        return l;
    }
    public Envioelectronico getSelected() {
        if (current == null) {
            current = new Envioelectronico();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EnvioelectronicoFacade getFacade() {
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

    public String prepareView(Envioelectronico e) {
        current=e;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Envioelectronico();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

            EnvioelectronicoPK pk=new EnvioelectronicoPK();
            pk.setIdArticulo(current.getArticulo().getId());
            pk.setIdPedido(current.getPedido().getPedidoPK().getIdPedido());
            current.setEnvioelectronicoPK(pk);
            current.setArticulo(current.getArticulo());
            current.setLigaDescarga(current.getLigaDescarga());
            current.setObservaciones(current.getObservaciones());
            current.setPedido(current.getPedido());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Envio electronico creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Envio electronico"));
            return null;
        }
    }

    public String prepareEdit(Envioelectronico e) {
        current=e;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("EnvioelectronicoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Envio electronico"));
            return null;
        }
    }

    public String destroy(Envioelectronico e ) {
        current=e;
        getFacade().remove(current);
       JsfUtil.addSuccessMessage("Envio Electronico eliminado Satisfactoriamente ");
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
            JsfUtil.addSuccessMessage(("EnvioelectronicoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Envio electronico"));
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

    @FacesConverter(forClass=Envioelectronico.class)
    public static class EnvioelectronicoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EnvioelectronicoController controller = (EnvioelectronicoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "envioelectronicoController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.compras.EnvioelectronicoPK getKey(String value) {
            escom.libreria.info.compras.EnvioelectronicoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.compras.EnvioelectronicoPK();
            key.setIdPedido(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.compras.EnvioelectronicoPK value) {
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
            if (object instanceof Envioelectronico) {
                Envioelectronico o = (Envioelectronico) object;
                return getStringKey(o.getEnvioelectronicoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+EnvioelectronicoController.class.getName());
            }
        }

    }

}
