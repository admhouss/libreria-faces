package escom.libreria.info.compras.jpa;

import escom.libreria.info.compras.Zona;
import escom.libreria.info.compras.jpa.util.JsfUtil;
import escom.libreria.info.compras.jpa.util.PaginationHelper;
import escom.libreria.info.compras.ejb.ZonaFacade;
import java.io.Serializable;
import java.math.BigDecimal;
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

@ManagedBean (name="zonaController")
@SessionScoped
public class ZonaController implements Serializable{

    private Zona current;
    private DataModel items = null;
    @EJB private escom.libreria.info.compras.ejb.ZonaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ZonaController() {
    }

    public Zona getSelected() {
        if (current == null) {
            current = new Zona();
            selectedItemIndex = -1;
        }
        return current;
    }


    public List<Zona> getListZona(){
       List<Zona> zonas= getFacade().findAll();
       return zonas;
    }
    private ZonaFacade getFacade() {
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

    public String prepareView(Zona z) {
        current=z;//
        current.setIdZona(z.getIdZona());
        current.setPeso(z.getPeso());
        current.setTarifa(z.getTarifa());
        return "/zona/View";
    }

    public String prepareCreate() {
        current = new Zona();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Zona creada Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear la zona"));
            return null;
        }
    }

    public String prepareEdit(Zona z) {
        current=z;//
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/zona/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Zona actualizada Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar  la zona"));
            return null;
        }
    }

    public String destroy(Zona z) {
        current=z;//
        getFacade().remove(z);
        JsfUtil.addSuccessMessage("Zona eliminada Satisfactoriamente");
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();

        return "/zona/List";
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
            JsfUtil.addSuccessMessage(("ZonaDeleted"));
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

    @FacesConverter(forClass=Zona.class)
    public static class ZonaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ZonaController controller = (ZonaController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "zonaController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Zona) {
                Zona o = (Zona) object;
                return getStringKey(o.getIdZona());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ZonaController.class.getName());
            }
        }

    }

}
