package escom.libreria.info.proveedor.jsf;



import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.articulo.jsf.util.PaginationHelper;

import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.proveedor.Proveedor;
import escom.libreria.info.proveedor.ejb.ProveedorFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

@ManagedBean (name="proveedorController")
@SessionScoped
public class ProveedorController implements Serializable{

    private Proveedor current;
    private DataModel items = null;
    @EJB private escom.libreria.info.proveedor.ejb.ProveedorFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Proveedor> listaProveedores;
    private List<Proveedor> listaProveedoresByArticulo;

    public List<Proveedor> getListaProveedoresByArticulo() {
        if(listaProveedoresByArticulo==null){
            listaProveedoresByArticulo=getFacade().findAll();
        }
        return listaProveedoresByArticulo;
    }

    public void setListaProveedoresByArticulo(List<Proveedor> listaProveedoresByArticulo) {
        this.listaProveedoresByArticulo = listaProveedoresByArticulo;
    }


    public ProveedorController() {
    }

    public Proveedor getSelected() {
        if (current == null) {
            current = new Proveedor();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProveedorFacade getFacade() {
        return ejbFacade;
    }


    public List<Proveedor> getListProveedor(){
       listaProveedores=getFacade().findAll();
       return listaProveedores;
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

  private Articulo articulo_proveedor;

    public Articulo getArticulo_proveedor() {
        return articulo_proveedor;
    }

    public void setArticulo_proveedor(Articulo articulo_proveedor) {
        this.articulo_proveedor = articulo_proveedor;
    }

    public String prepreaListByArticulo(Articulo item){
        item.setDivisa(item.getDivisa());
        articulo_proveedor=item;
        articulo_proveedor.setCosto(item.getCosto());

        return "/articulo/ViewProveedor";
    }
    public void destroyByArticulo(Proveedor proveedor){
//       articulo_proveedor.getProveedorList().remove(proveedor);
  //     listaProveedoresByArticulo.add(proveedor);
       JsfUtil.addErrorMessage("Proveedor eliminado");
    }
    public String prepareList() {
        //recreateModel();
        return "/proveedor/List";
    }

    public String prepareView(Proveedor p) {
       current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/proveedor/View";
    }

    public String prepareCreate() {
        current = new Proveedor();
        //selectedItemIndex = -1;
        return "Create";
    }


    public void agregar(Proveedor proveedor){
    /*    listaProveedoresByArticulo.remove(proveedor);
        if(articulo_proveedor.getProveedorList()==null)
            articulo_proveedor.setProveedorList(new ArrayList<Proveedor>());
        articulo_proveedor.getProveedorList().add(proveedor);
        JsfUtil.addSuccessMessage("Proveedor seleccionado satisfactoriamente");*/
      // return "/articulo/ViewProveedor";
    }
    public String create() {
        try {
            if(current!=null){

                current.setMmod(new Date());
                current.setFechaAlta(new Date());
                current.setMmod(new Date());
                current.setEstatus(true);
                getFacade().create(current);
                JsfUtil.addSuccessMessage("Proveedor Guardado Satisfactoriamente");
             }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return prepareView(current);
    }

    public String prepareEdit(Proveedor p) {
       current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
                current.setMmod(new Date());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Proveedor actualizado"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Proveedor p) {
       current=p;
       ejbFacade.remove(p);
       listaProveedores.remove(p);
       JsfUtil.addSuccessMessage("Proveedor eliminado satisfactoriamente");



        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProveedorDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Proveedor.class)
    public static class ProveedorControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedorController controller = (ProveedorController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedorController");
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
            if (object instanceof Proveedor) {
                Proveedor o = (Proveedor) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ProveedorController.class.getName());
            }
        }

    }

}
