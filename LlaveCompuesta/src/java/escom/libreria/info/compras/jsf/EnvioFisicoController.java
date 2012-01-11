package escom.libreria.info.compras.jsf;

import escom.libreria.info.compras.EnvioFisico;
import escom.libreria.info.compras.EnvioFisicoPK;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.compras.jsf.util.PaginationHelper;
import escom.libreria.info.compras.ejb.EnvioFisicoFacade;
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

@ManagedBean (name="envioFisicoController")
@SessionScoped
public class EnvioFisicoController implements Serializable{

    private EnvioFisico current;
    private DataModel items = null;
    @EJB private escom.libreria.info.compras.ejb.EnvioFisicoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public EnvioFisicoController() {
    }

    public EnvioFisico getSelected() {
        if (current == null) {
            current = new EnvioFisico();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EnvioFisicoFacade getFacade() {
        return ejbFacade;
    }


    public List<EnvioFisico> getListEnvioFisicos(){
        List<EnvioFisico> l=getFacade().findAll();
        return l;
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

    public String prepareView(EnvioFisico p) {
        current=p;
      //  selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new EnvioFisico();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            EnvioFisicoPK pk=new EnvioFisicoPK();
            pk.setIdArticulo(current.getArticulo().getId());
            pk.setIdPedido(current.getPedido().getPedidoPK().getIdPedido());
            current.setEnvioFisicoPK(pk);
            current.setArticulo(current.getArticulo());
            current.setDirenvio(current.getDirenvio());
            current.setPaqueteria(current.getPaqueteria());
            current.setObservaciones(current.getObservaciones());
            current.setNoGuia(current.getNoGuia());


            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Envio Fisico Createdo Satisfactoriamente"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear Envio Fisico"));
            return null;
        }
    }

    public String prepareEdit(EnvioFisico p ) {
        current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Envio Fisico Actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar Envio Fisico"));
            return null;
        }
    }

    public String destroy(EnvioFisico p ) {
        current=p;
        getFacade().remove(p);
        JsfUtil.addSuccessMessage("Envio Fisico Eliminado Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("EnvioFisicoDeleted"));
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

    @FacesConverter(forClass=EnvioFisico.class)
    public static class EnvioFisicoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EnvioFisicoController controller = (EnvioFisicoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "envioFisicoController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.compras.EnvioFisicoPK getKey(String value) {
            escom.libreria.info.compras.EnvioFisicoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.compras.EnvioFisicoPK();
            key.setIdPedido(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.compras.EnvioFisicoPK value) {
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
            if (object instanceof EnvioFisico) {
                EnvioFisico o = (EnvioFisico) object;
                return getStringKey(o.getEnvioFisicoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+EnvioFisicoController.class.getName());
            }
        }

    }

}
