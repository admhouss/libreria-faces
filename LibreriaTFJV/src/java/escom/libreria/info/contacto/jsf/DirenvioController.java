package escom.libreria.info.contacto.jsf;

import escom.libreria.info.contacto.jpa.Direnvio;
import escom.libreria.info.contacto.jsf.util.JsfUtil;
import escom.libreria.info.contacto.jsf.util.PaginationHelper;
import escom.libreria.info.contacto.ejb.DirenvioFacade;
import escom.libreria.info.login.sistema.SistemaController;
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

@ManagedBean (name="direnvioController")
@SessionScoped
public class DirenvioController implements Serializable{

    private Direnvio current;
    private DataModel items = null;
    @EJB private escom.libreria.info.contacto.ejb.DirenvioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    @ManagedProperty("#{sistemaController}")
    SistemaController sistemaController;

    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }
    

    public String irMenu(){
        return "/cliente/modulo";
    }

    private List<Direnvio> listaDirEnvioCliente;

    public List<Direnvio> getListaDirEnvioCliente() {
        try{
         List<Direnvio> dirEnvio=getFacade().getListDirEnvioByCliente(sistemaController.getCliente().getId());
         return dirEnvio;
        }catch(Exception e){}
        return null;

        //return listaDirEnvioCliente;
    }

    public void setListaDirEnvioCliente(List<Direnvio> listaDirEnvioCliente) {
        this.listaDirEnvioCliente = listaDirEnvioCliente;
    }


    public DirenvioController() {
    }

    public Direnvio getSelected() {
        if (current == null) {
            current = new Direnvio();
            selectedItemIndex = -1;
        }
        return current;
    }



    public List<Direnvio> getListDireccionEnvio(){
       List<Direnvio> l= getFacade().findAll();
       return l;

    }

    private DirenvioFacade getFacade() {
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
        return "/direnvio/List";
    }

    public String prepareView(Direnvio p) {
       current=p;//
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/direnvio/View";
    }

    public String prepareCreate() {
        current = new Direnvio();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setIdCliente(sistemaController.getCliente());
            if(current.getIdCliente()!=null){
             getFacade().create(current);
              JsfUtil.addSuccessMessage(("DirenvioCreated"));
              return prepareView(current);
            }
            JsfUtil.addErrorMessage("No existe cliente asociado");
            return "/login/Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Direccion").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    

    public String prepareEdit(Direnvio p) {
       current=p;//
       current.setIdCliente(p.getIdCliente());
        return "/direnvio/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("DirenvioUpdated"));
            return "/direnvio/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Direccion").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Direnvio p) {
       current=p;//
       ejbFacade.remove(current);
       JsfUtil.addSuccessMessage("Direccion Eliminada satisfactoriamente");
        return "/direnvio/List";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Direccion").getString("DirenvioDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Direccion").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Direnvio.class)
    public static class DirenvioControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DirenvioController controller = (DirenvioController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "direnvioController");
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
            if (object instanceof Direnvio) {
                Direnvio o = (Direnvio) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+DirenvioController.class.getName());
            }
        }

    }

}
