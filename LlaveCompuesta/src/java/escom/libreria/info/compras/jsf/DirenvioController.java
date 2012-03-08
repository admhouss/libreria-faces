package escom.libreria.info.compras.jsf;

import escom.libreria.info.administracion.jsf.util.JsfUtil;
import escom.libreria.info.administracion.jsf.util.PaginationHelper;
import escom.libreria.info.compras.Direnvio;
import escom.libreria.info.compras.ejb.DirenvioFacade;
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
    @EJB private DirenvioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;

    private Direnvio direccionEnvioSelected;

    public Direnvio getDireccionEnvioSelected() {

        return direccionEnvioSelected;
    }

    public void setDireccionEnvioSelected(Direnvio direccionEnvioSelected) {

        this.direccionEnvioSelected = direccionEnvioSelected;
    }


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


    public String dirEnvioSeleccionado(Direnvio envio){
        current=envio;
        JsfUtil.addSuccessMessage("Direccion de envio seleccionada Satisfactoriamente");
        return "/carrito/Carrito";
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
        return "/direnvio/View";
    }

    public String prepareCreate() {
        current = new Direnvio();
        selectedItemIndex = -1;
        return "Create";
    }

    public String clientePrepareCreate(){
        return "/direnvio/List";
    }
    public String create() {

        try {
      current.setCliente(sistemaController.getCliente());
            if(current.getCliente()!=null){
             getFacade().create(current);
              JsfUtil.addSuccessMessage(("Direccion de Envio creada Satisfactoriamente"));
              return prepareView(current);
            }
            JsfUtil.addErrorMessage("No existe cliente asociado");
            return "/login/Create";
        } catch (Exception e) {
            e.printStackTrace();

                    JsfUtil.addErrorMessage(e, ("Error al crear la direccion de envio"));
            return null;
        }

    }



    public String prepareEdit(Direnvio p) {
       current=p;//
       //current.setIdCliente(p.get);
        return "/direnvio/Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Direncion Acutalizada Satisfactoriamente"));
            return "/direnvio/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(("Error al actualizar direcion de envio"));
            return null;
        }
    }

    public String destroy(Direnvio p) {
       current=p;//
       ejbFacade.remove(current);
       JsfUtil.addSuccessMessage("Direccion Eliminada satisfactoriamente");
        return "/direnvio/List";
    }

     public String destroysds() {
        FacesContext faces = FacesContext.getCurrentInstance();
        //faces.g
        return null;

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
            JsfUtil.addSuccessMessage(("DirenvioDeleted"));
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
