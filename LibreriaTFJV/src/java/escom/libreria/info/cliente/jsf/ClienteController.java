package escom.libreria.info.cliente.jsf;

import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.cliente.jsf.util.JsfUtil;
import escom.libreria.info.cliente.jsf.util.PaginationHelper;
import escom.libreria.info.cliente.ejb.ClienteFacade;
import escom.libreria.info.cliente.jpa.Categoria;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.servlet.http.HttpServletRequest;

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
   // private int go;
    private List<Cliente> listSeleccionCliente;
    private String nombre,correo;//Criterios de busqueda
  

   
   
    public List<Cliente> getListSeleccionCliente() {
       if(listSeleccionCliente==null)
       listSeleccionCliente=getFacade().findAll();
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

   
    public Cliente getSelected() {
        if (current == null) {
            current = new Cliente();
          
            selectedItemIndex = -1;
        }

        return current;
    }

    public String busquedaGeneral(){
     listSeleccionCliente=getListaClientes();
      return "/cliente/List";
    }
    public List<Cliente> getListaClientes(){
       return  getFacade().findAll();//getListClientesActive();
      
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
       current=null;
       return "/cliente/List";
    }

    public String prepareView(Cliente p) {
        current=p;
        return "/cliente/View";
    }

    public String prepareCreate() {
        current = new Cliente();     
        return "/cliente/Create";
    }
   

    public void buscarCliente(){
        int bandera=0;
        String query="";


       if(correo!=null && !correo.trim().equals("")){
         query+="c.id LIKE :correo ";
         bandera=1;
       }
       if(nombre!=null && !nombre.equals("")){
           switch(bandera){
               case 0:query+="c.nombre LIKE :nombre OR  c.paterno LIKE :nombre  OR  c.materno LIKE :nombre ";break;
               case 1:query+="OR c.nombre LIKE :nombre OR  c.paterno LIKE :nombre  OR  c.materno LIKE :nombre ";break;
           }
        }
        if(query.equals("")){
            listSeleccionCliente=getFacade().findAll();//refill
            JsfUtil.addErrorMessage("El cliente no existe!");
        }
        else{
             listSeleccionCliente=getFacade().buscarCliente(query,correo,nombre);
             if(listSeleccionCliente==null || listSeleccionCliente.isEmpty())
             JsfUtil.addErrorMessage("El cliente no existe!");
        }

        setCorreo("");setNombre("");
    }

    

    public String create() {
        try {
            
             
            if(confirmaCorreo.equals(current.getId())){

                 if(getFacade().find(current.getId())==null){
                         current.setEstatus(false);
                         current.setFechaAlta(new Date());
                         current.setId(current.getId());
                         current.setModificacion(new Date());
                         current.setEmail(current.getEmail());
                         current.setCategoria(new Categoria(4, "CLIENTE"));
                         setConfirmaCorreo("");
                         getFacade().create(current);
                         procesarJMail.EnviarConfimarCorreo(current);
                         JsfUtil.addSuccessMessage("cliente creado satisfactoriamente!");
                         return prepareView(current);
                 }else{
                     JsfUtil.addErrorMessage("La cuenta ya fue registrada anteriormente!");
                     return "/cliente/Create";
                 }
            }
           
           JsfUtil.addErrorMessage("El correo y la confirmacion no coinciden");
           return "/cliente/Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Cliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }



    public String prepareEdit(Cliente p) {
       if(p==null)
       return "/login/Create";
       current=p;
       return "/cliente/Edit";
    }

    public String update() {
     
        try {
                current.setEstatus(current.getEstatus());
                current.setCategoria(current.getCategoria());
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(("Cliente actializado"));
                setConfirmaCorreo("");
                return "/cliente/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Cliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    

     public void destroyCliente(Cliente p) {

       current=p;
       getFacade().remove(current);
       listSeleccionCliente.remove(p);
       current=null;confirmaCorreo="";
       JsfUtil.addSuccessMessage("Cliente elminiado satisfactoriamente!!");
      
    }
     public void destroy(Cliente p) {

       p.setEstatus(false);
       getFacade().edit(p);
       confirmaCorreo="";
       JsfUtil.addSuccessMessage("Cliente desactivado satisfactoriamente!!");

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
