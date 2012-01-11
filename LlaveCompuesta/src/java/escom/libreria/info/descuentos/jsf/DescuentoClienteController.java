package escom.libreria.info.descuentos.jsf;

import escom.libreria.info.descuentos.DescuentoCliente;
import escom.libreria.info.cliente.jsf.util.JsfUtil;
import escom.libreria.info.cliente.jsf.util.PaginationHelper;

import escom.libreria.info.descuentos.DescuentoClientePK;
import escom.libreria.info.descuentos.DescuentoCliente;
import escom.libreria.info.descuentos.DescuentoClientePK;
import escom.libreria.info.descuentos.ejb.DescuentoClienteFacade;
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

@ManagedBean (name="descuentoClienteController")
@SessionScoped
public class DescuentoClienteController implements Serializable{

    private DescuentoCliente current;
    private DataModel items = null;
    @EJB private DescuentoClienteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public DescuentoClienteController() {
    }


    public List<DescuentoCliente> getListDescuento(){
        return getFacade().findAll();
    }
    public DescuentoCliente getSelected() {
        if (current == null) {
            current = new DescuentoCliente();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DescuentoClienteFacade getFacade() {
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

    public String prepareView(DescuentoCliente p) {
       current=p;
        return "/descuentoCliente/View";
    }

    public String prepareCreate() {
        current = new DescuentoCliente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            DescuentoClientePK descuentoID=new DescuentoClientePK();
            current.setCliente(current.getCliente());
            current.setDescuento(current.getDescuento());
            descuentoID.setIdCliente(current.getCliente().getId());
            descuentoID.setIdDescuento(current.getDescuento().getId());
            current.setDescuentoClientePK(descuentoID);
            current.setFechaFin(current.getFechaFin());
            current.setFechaInicio(current.getFechaFin());

            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Descuento cliente creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear descuento cliente"));
            return null;
        }
    }

    public String prepareEdit(DescuentoCliente p) {
        current=p;
        current.setCliente(current.getCliente());
        current.setDescuentoClientePK(current.getDescuentoClientePK());
        current.setDescuento(current.getDescuento());
        return "/descuentoCliente/Edit";
    }

    public String update() {
        try {



            DescuentoClientePK descuentoID=current.getDescuentoClientePK();
            current.setCliente(current.getCliente());
            current.setDescuento(current.getDescuento());
            current.setFechaFin(current.getFechaFin());
            current.setFechaInicio(current.getFechaInicio());
            descuentoID.setIdCliente(current.getCliente().getId());
            descuentoID.setIdDescuento(current.getDescuento().getId());
            current.setDescuentoClientePK(descuentoID);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Descuento Cliente Actualizado"));

              return "/descuentoCliente/View";

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar descuento cliente"));
            return null;
        }
    }

    public String destroy(DescuentoCliente p) {
      current=p;
      getFacade().remove(current);
      JsfUtil.addSuccessMessage("Descuento eliminado satisfactoriamente");
      return "/descuentoCliente/List";
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
            JsfUtil.addSuccessMessage(("DescuentoClienteDeleted"));
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

    @FacesConverter(forClass=DescuentoCliente.class)
    public static class DescuentoClienteControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DescuentoClienteController controller = (DescuentoClienteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "descuentoClienteController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.descuentos.DescuentoClientePK getKey(String value) {
            escom.libreria.info.descuentos.DescuentoClientePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.descuentos.DescuentoClientePK();
            key.setIdCliente(values[0]);
            key.setIdDescuento(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.descuentos.DescuentoClientePK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdCliente());
            sb.append(SEPARATOR);
            sb.append(value.getIdDescuento());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DescuentoCliente) {
                DescuentoCliente o = (DescuentoCliente) object;
                return getStringKey(o.getDescuentoClientePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+DescuentoClienteController.class.getName());
            }
        }

    }

}
