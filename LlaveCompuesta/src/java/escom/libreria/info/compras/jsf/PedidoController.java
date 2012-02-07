package escom.libreria.info.compras.jsf;

import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.compras.Pedido;
import escom.libreria.info.compras.PedidoPK;
import escom.libreria.info.compras.jsf.util.JsfUtil;
import escom.libreria.info.compras.jsf.util.PaginationHelper;
import escom.libreria.info.compras.ejb.PedidoFacade;
import escom.libreria.info.encriptamientoMD5.EncriptamientoImp;
import escom.libreria.info.login.sistema.SistemaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import java.util.logging.Level;


@ManagedBean (name="pedidoController")
@SessionScoped
public class PedidoController implements Serializable{
   public int contado=0;//contador;
    private Pedido current;
    private DataModel items = null;
    @EJB private escom.libreria.info.compras.ejb.PedidoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    private int identificadorPedido;
    private static  Logger logger = Logger.getLogger(PedidoController.class);

    public int getIdentificadorPedido() {
        return identificadorPedido;
    }

    public void setIdentificadorPedido(int identificadorPedido) {
        this.identificadorPedido = identificadorPedido;
    }


    public int getContado() {
        contado=contado+1;
        return contado;
    }

    public void setContado(int contado) {
        this.contado = contado;
    }


    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }


    public String geturlcancelado(){
         String generarURL="http://localhost:8080/LibreriaTFJV/faces/compra/Cancelada.xhtml?pedidoCancelado=";

        try {
            String idCliente=sistemaController.getCliente().getId();
            identificadorPedido=getFacade().buscarIdPeidoMaximo(idCliente,"CONFIRMADO");
            System.out.println("cancelado"+identificadorPedido);
            String saludo = generarURLMD5(identificadorPedido);
            generarURL+=saludo;
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            String caca=external.encodeResourceURL(generarURL);
            return caca;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("error al generar url cancelar", ex);
        }
         return "";
    }


    public String geturlcomprado(){


        String generarURL="http://localhost:8080/LibreriaTFJV/faces/compra/Comprado.xhtml?pedidoComprado=";
        try {
            String idCliente=sistemaController.getCliente().getId();
            identificadorPedido=getFacade().buscarIdPeidoMaximo(idCliente,"CONFIRMADO");
            String saludo = generarURLMD5(identificadorPedido);
            generarURL+=saludo;
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            String caca=external.encodeResourceURL(generarURL);
            return caca;
        } catch (Exception ex) {
            //ex.printStackTrace();
           logger.error("error al generar url comprado", ex);
        }

        return "";
    }


    private String generarURLMD5(int idPedido){
         byte[] binario;
         String digestion=null;
        try {

            EncriptamientoImp encriptamientoImp = new EncriptamientoImp();
            binario = encriptamientoImp.encrypt(idPedido+"");
            System.out.println("ENVIANDO"+idPedido);
            digestion = encriptamientoImp.convertToHex(binario);
             System.out.println("ENVIANDO"+digestion);
            return digestion;
        } catch (Exception ex) {
           logger.error("error generar digestion:", ex);
        }
            return digestion;
    }


    public List<Pedido> getLisPedidosByCliente(){
        List<Pedido> pedidos=null;
        try
        {
            Cliente cliente=sistemaController.getCliente();
            identificadorPedido=getFacade().buscarIdPeidoMaximo(cliente.getId(),"PROCESANDO");
            pedidos= getFacade().getListaPedidosByidPedios(identificadorPedido);
        }catch(Exception e){

            JsfUtil.addErrorMessage("Error al obtener pedidos by cliente");
        }

        return pedidos;
    }

     public List<Pedido> getLisPedidosByClienteALL(){
        List<Pedido> pedidos=null;
        try
        {
            Cliente cliente=sistemaController.getCliente();
            pedidos=getFacade().getListPedidoByCliente(cliente.getId());
         //   pedidos= getFacade().getListaPedidosByidPedios(identificadorPedido);
        }catch(Exception e){

            logger.error("ERROR LISTA DE PEIDDOS BU CLIENTES ALL");
        }

        return pedidos;
    }

    public List<Pedido> getLisPedidosByClienteConfirmado(){

        List<Pedido> pedidos=null;
        try{
            Cliente cliente=sistemaController.getCliente();
            identificadorPedido=getFacade().buscarIdPeidoMaximo(cliente.getId(),"CONFIRMADO");
            pedidos= getFacade().getListaPedidosByidPedios(identificadorPedido);
        }catch(Exception e){

            JsfUtil.addErrorMessage("Error al obtener pedidos by cliente");
        }
        return pedidos;
    }


    public BigDecimal getMontoTotal(){
        BigDecimal montoTotal=BigDecimal.ZERO;

         try{
            Cliente cliente=sistemaController.getCliente();
            int idPedido=getFacade().buscarIdPeidoMaximo(cliente.getId(),"PROCESANDO");
             montoTotal= getFacade().getPedidoMontoTotal(idPedido);
             if(montoTotal!=null){
                montoTotal=montoTotal.setScale(2, BigDecimal.ROUND_UP) ;
             }
        }catch(Exception e){

            JsfUtil.addErrorMessage("Error al obtener pedidos by cliente");
        }
        return montoTotal;
    }


    public String generarqueryCancelado(){

    Cliente cliente=sistemaController.getCliente();
    int idPedido=getFacade().buscarIdPeidoMaximo(cliente.getId(),"CONFIRMADO");
    return "";

    }
    public BigDecimal getMontoTotalConfirmado(){
        BigDecimal montoTotal=BigDecimal.ZERO;

         try{
            Cliente cliente=sistemaController.getCliente();
            int idPedido=getFacade().buscarIdPeidoMaximo(cliente.getId(),"CONFIRMADO");
             montoTotal= getFacade().getPedidoMontoTotal(idPedido);
             if(montoTotal!=null){
                montoTotal=montoTotal.setScale(2, BigDecimal.ROUND_UP) ;
             }
        }catch(Exception e){

            JsfUtil.addErrorMessage("Error al obtener pedidos by cliente");
        }
        return montoTotal;
    }
    public List<Pedido> getListPedido(){

        List<Pedido> pedidos=getFacade().findAll();
        return pedidos;
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
        return "Create";
    }

    public String create() {
        try {
            PedidoPK pk=new PedidoPK();
            pk.setIdArticulo(current.getArticulo().getId());
            current.setPedidoPK(pk);
            current.setFechaPedido(new Date());
            current.setEstado("PROCESANDO");
            getFacade().create(current);
            JsfUtil.addSuccessMessage(("Pedido creado Satisfactoriamente"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/compras").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Pedido p) {
        current = p;//(Pedido)getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/compras").getString("PedidoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/compras").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Pedido p) {
        current =p;// (Pedido)getItems().getRowData();
        getFacade().remove(p);
        JsfUtil.addSuccessMessage("PEDIDO ELIMINADO SATISFACTORIAMENTE");
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/compras").getString("PedidoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/compras").getString("PersistenceErrorOccured"));
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

        escom.libreria.info.compras.PedidoPK getKey(String value) {
            escom.libreria.info.compras.PedidoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.compras.PedidoPK();
            key.setIdPedido(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.compras.PedidoPK value) {
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
