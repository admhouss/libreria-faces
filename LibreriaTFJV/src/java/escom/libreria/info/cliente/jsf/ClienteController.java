package escom.libreria.info.cliente.jsf;

import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.cliente.jsf.util.JsfUtil;
import escom.libreria.info.cliente.jsf.util.PaginationHelper;
import escom.libreria.info.cliente.ejb.ClienteFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean (name="clienteController")
@SessionScoped
public class ClienteController implements Serializable{

    private Cliente current;
    private DataModel items = null;
    @EJB private escom.libreria.info.cliente.ejb.ClienteFacade ejbFacade;
    @EJB private escom.libreria.correo.ProcesoJMail procesarJMail;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String confirmaCorreo;
    private int go;
    private List<Cliente> listSeleccionCliente;

    private String telefonoCasa,telefonoOficina;

    public String getTelefonoCasa() {
        return telefonoCasa;
    }

    public void setTelefonoCasa(String telefonoCasa) {
        this.telefonoCasa = telefonoCasa;
    }

    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }
   
    public List<Cliente> getListSeleccionCliente() {
        return listSeleccionCliente;
    }

    public void setListSeleccionCliente(List<Cliente> listSeleccionCliente) {
        this.listSeleccionCliente = listSeleccionCliente;
    }
   

    public String getConfirmaCorreo() {
        return confirmaCorreo;
    }

    public void setConfirmaCorreo(String confirmaCorreo) {
        this.confirmaCorreo = confirmaCorreo;
    }
    

    public ClienteController() {
    }

   
    public Cliente getSelected() {
        if (current == null) {
            current = new Cliente();
          
            selectedItemIndex = -1;
        }

        return current;
    }

    public List<Cliente> getListaClientes(){
       List<Cliente> l= getFacade().getListClientesActive();
       return l;
    }
    private ClienteFacade getFacade() {
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
       // recreateModel();
        current=null;
       return "List";
     
    }

    public String prepareView(Cliente p) {
       current=p;
       
      
        return "/cliente/View";
    }

    public String prepareCreate() {
        current = new Cliente();
       

       
    //    selectedItemIndex = -1;
        
        return "/cliente/Create";
       
    }
    public String prepareCreate2() {
    //    current = new Cliente();
        current=null;

        System.out.println("go");
    //    selectedItemIndex = -1;
        return "./../index.xhtml";

       // return "./faces/cliente/Create";

    }

    public String create() {
        try {
             //List<Telefono> telefonos=new ArrayList<Telefono>();
             
            if(confirmaCorreo.equals(current.getId())){

                 if(getFacade().find(current.getId())==null){
                         current.setEstatus(true);
                         current.setFechaAlta(new Date());
                         current.setId(current.getId());
                         current.setModificacion(new Date());
                         current.setEmail(current.getEmail());
                         confirmaCorreo="";
                        getFacade().create(current);
                        JsfUtil.addSuccessMessage("Cliente creado satisfactoriamente");
                        return prepareView(current);
                 }else{
                     JsfUtil.addErrorMessage("La cuenta que intenta registrara ,ya existe!");
                     current.setId(" ");
                     return "/cliente/Create";
                 }
            }
            setTelefonoCasa("");
            setTelefonoOficina("");
           JsfUtil.addErrorMessage("El correo y la confirmacion no coinciden");
           return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Cliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Cliente p) {
       current=p;
      
       

       
        return "/cliente/Edit";
    }

    public String update() {
     //   List <Telefono> telefonos =new ArrayList<Telefono>();
        try {
                     
               

                   
          //   current.setTelefonoList(current.getTelefonoList());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Cliente actializado"));
            setConfirmaCorreo("");
                   setTelefonoCasa("");
                   setTelefonoOficina("");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Cliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Cliente p) {
       current=p;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreateModel();
       getFacade().remove(p);
       current=null;
       confirmaCorreo="";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Cliente").getString("ClienteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Cliente").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Cliente.class)
    public static class ClienteControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
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
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+ClienteController.class.getName());
            }
        }

    }

}
