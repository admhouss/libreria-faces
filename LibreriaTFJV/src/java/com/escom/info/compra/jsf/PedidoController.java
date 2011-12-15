package com.escom.info.compra.jsf;

import com.escom.info.compra.Pedido;
import com.escom.info.compra.PedidoPK;
import com.escom.info.compra.jsf.util.JsfUtil;
import com.escom.info.compra.jsf.util.PaginationHelper;
import com.escom.info.compra.ejb.PedidoFacade;
import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.login.sistema.SistemaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

@ManagedBean (name="pedidoController")
@SessionScoped
public class PedidoController implements Serializable{

    private Pedido current;
    private DataModel items = null;
    @EJB private com.escom.info.compra.ejb.PedidoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    public int contador=0;
    private Date pedidoFecha;
    private Date endpedidoFecha;
    private String tipoEnvio;
     private Pedido mycurrent;

    public Pedido getMycurrent() {
        return mycurrent;
    }

    public void setMycurrent(Pedido mycurrent) {
        this.mycurrent = mycurrent;
    }



    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }


    public Date getEndpedidoFecha() {
        return endpedidoFecha;
    }

    public void setEndpedidoFecha(Date endpedidoFecha) {
        this.endpedidoFecha = endpedidoFecha;
    }

    public Date getPedidoFecha() {
        return pedidoFecha;
    }

    public void setPedidoFecha(Date pedidoFecha) {
        this.pedidoFecha = pedidoFecha;
    }


    public int getContador() {
        contador= contador+1;
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

public List<Pedido> listaPedidosCliente;


private Date getHoy(){
        try {
            Date date = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
            String cadena = formato2.format(date);
            Date fechaOtra = formato2.parse(cadena);

            String cadenaToday = formato.format(fechaOtra);
            Date hoy = formato2.parse(cadenaToday);
            return hoy;
        } catch (ParseException ex) {
            Logger.getLogger(PedidoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
}
public String publicacionByFecha(){
    Cliente cliente=sistemaController.getCliente();
    

    listaPedidosCliente=getFacade().getListPedidoByFechas(cliente.getId(),pedidoFecha,endpedidoFecha);
    
    return "/pedido/List";
}

    public List<Pedido> getListaPedidosCliente() {
        return listaPedidosCliente;
    }

    public void setListaPedidosCliente(List<Pedido> listaPedidosCliente) {
        this.listaPedidosCliente = listaPedidosCliente;
    }

    

public String actualizarTipoEnvio(Pedido p){
    try{

        current=p;
       /* p.setTipoEnvio(p.getTipoEnvio());
        p.setArticulo(p.getArticulo());
        p.setCliente(p.getCliente());
        p.setPedidoPK(p.getPedidoPK());
        getFacade().edit(p);
        JsfUtil.addSuccessMessage("Pedido Actualizado Satisfactoriamente");
        *
        */
        return "/compra/EditCompra";
    }catch(Exception e){e.printStackTrace();}
    JsfUtil.addErrorMessage("Error no fue posible actualizar su pedido");
    return "/compra/Create";

}

    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }

    public List<Pedido> getListPedidosByCliente(){
        Cliente cliente=sistemaController.getCliente();
        Date fechaActual=new Date();
        Date hoyD=getHoy();
        List<Pedido>pedido=getFacade().getListPedidoHotByCliernte(cliente.getId(), hoyD);
        try{
         if(!pedido.isEmpty())
         current=pedido.get(0);
        }catch(Exception e){}
        return pedido;
    }

  public BigDecimal getMontoTotal(){
       Cliente cliente=sistemaController.getCliente();
       BigDecimal  montoTOTAL=getFacade().getPedidoMontoTotal(cliente.getId(),getHoy());
        return montoTOTAL;
  }

  

  public String eliminarPedido(Pedido pedido){
      getFacade().remove(pedido);
      JsfUtil.addSuccessMessage("Pedido eliminado Satisfactoriamente");
      return "/carrito/Carrito";
  }


    public PedidoController() {
    }

    public Pedido getSelected() {
        if (current == null) {
            current = new Pedido();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PedidoFacade getFacade() {
        return ejbFacade;
    }

    public List<Pedido> getListPedidos(){
        List<Pedido> p=getFacade().findAll();

        return p;
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

    public String prepareView(Pedido p) {
        current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Pedido();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            PedidoPK pedidokey=new PedidoPK();
            pedidokey.setIdArticulo(current.getArticulo().getId());
            current.setPedidoPK(pedidokey);
            current.setArticulo(current.getArticulo());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Pedido Creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Pedido").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Pedido p) {
        current=p;
        
        return "/pedido/Edit";
    }

    public String updateConfig() {
        try {
            current=mycurrent;
            PedidoPK pedido = mycurrent.getPedidoPK();
            pedido.setIdArticulo(current.getArticulo().getId());
            pedido.setIdPedido(pedido.getIdPedido());
            current.setPedidoPK(pedido);
            current.setTipoEnvio(getTipoEnvio());
            current.setArticulo(current.getArticulo());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Pedido Actualizado Satisfactoriamente"));
            return "/compra/Create";
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Pedido").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    public String update() {
        try {
            PedidoPK pedido = current.getPedidoPK();
            pedido.setIdArticulo(current.getArticulo().getId());
            pedido.setIdPedido(pedido.getIdPedido());
            current.setPedidoPK(pedido);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Pedido Actualizado Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Pedido").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Pedido p) {
        current=p;
        getFacade().remove(p);
        JsfUtil.addSuccessMessage("Pedido Eliminado Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Pedido").getString("PedidoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Pedido").getString("PersistenceErrorOccured"));
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

    @FacesConverter(forClass=Pedido.class)
    public static class PedidoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PedidoController controller = (PedidoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pedidoController");
            return controller.ejbFacade.find(getKey(value));
        }

        com.escom.info.compra.PedidoPK getKey(String value) {
            com.escom.info.compra.PedidoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.escom.info.compra.PedidoPK();
            key.setIdPedido(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(com.escom.info.compra.PedidoPK value) {
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
            if (object instanceof Pedido) {
                Pedido o = (Pedido) object;
                return getStringKey(o.getPedidoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+PedidoController.class.getName());
            }
        }

    }

}
