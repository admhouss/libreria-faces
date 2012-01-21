package escom.libreria.info.suscripciones.jsf;

import escom.libreria.info.articulo.jsf.ComentarioController;
import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.carrito.jpa.PublicacionDTO;
import escom.libreria.info.carrito.jsf.CarritoController;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.facturacion.ejb.ArticuloFacade;
import escom.libreria.info.login.ejb.SistemaFacade;
import escom.libreria.info.suscripciones.Suscripcion;
import escom.libreria.info.suscripciones.SuscripcionPK;
import escom.libreria.info.suscripciones.jsf.util.JsfUtil;
import escom.libreria.info.suscripciones.jsf.util.PaginationHelper;
import escom.libreria.info.suscripciones.ejb.SuscripcionFacade;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

@ManagedBean (name="suscripcionController")
@SessionScoped
public class SuscripcionController implements Serializable{
    
    private Suscripcion current;
    private DataModel items = null;
    @EJB private escom.libreria.info.suscripciones.ejb.SuscripcionFacade ejbFacade;
    @EJB private SistemaFacade sistemaFacade;
    @EJB private ArticuloFacade articuloFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int suscripcion;
    private List<Suscripcion> listasuscripciones;
    private Articulo articuloSeleccionado;

     @ManagedProperty("#{comentarioController}")
     private ComentarioController comentarioController;

    public ComentarioController getComentarioController() {
        return comentarioController;
    }

    public void setComentarioController(ComentarioController comentarioController) {
        this.comentarioController = comentarioController;
    }




    public Articulo getArticuloSeleccionado() {
        return articuloSeleccionado;
    }



    public void setArticuloSeleccionado(Articulo articuloSeleccionado) {
        this.articuloSeleccionado = articuloSeleccionado;
    }

    public String regresarSuscripcionVentas(){
        return "/suscripcionVentas/List";
    }

    public String  prepareViewArticulo(Articulo articulo){
        articuloSeleccionado=articulo;
        return "Create_1";

    }
    public String prepareAgregarComentario(Articulo articulo){
        articuloSeleccionado=articulo;
        comentarioController.setArticulo(articulo);
        return "/suscripcionVentas/ViewVenta";
    }

    public String prepareSuscripcion(Articulo articulo){
       articuloSeleccionado=articulo;
       return "/suscripcionVentas/View";
    }

    

    public List<Suscripcion> getListasuscripciones() {
             if(listasuscripciones==null || listasuscripciones.isEmpty()){
               listasuscripciones=getFacade().getSuscripcionByID(suscripcion);
             }

        return listasuscripciones;
    }

    public void setListasuscripciones(List<Suscripcion> listasuscripciones) {
        this.listasuscripciones = listasuscripciones;
    }



public String buscar(){

    listasuscripciones=getFacade().getSuscripcionByID(suscripcion);
    return "/suscripcionVentas/List";
}
    public int getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(int suscripcion) {
        this.suscripcion = suscripcion;
    }



    public SuscripcionController() {
    }

    public Suscripcion getSelected() {
        if (current == null) {
            current = new Suscripcion();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Suscripcion> getListSuscripcion(){
       List<Suscripcion> l=getFacade().findAll();
       return l;
    }

    public List<Integer> getIdSuscripciones(){
       List<Integer> l= getFacade().getIdSuscripciones();
       return l;
    }

    private SuscripcionFacade getFacade() {
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


    public String agregarArticulo(){
        try {
            PublicacionDTO rellenarESTE;

            // ejecutarStoreProcedure(getListasuscripciones());
            //List<Articulo> articulos= getFacade().getArticulosByID(suscripcion);
            //          carritoController.agregarArticulo(null)
            //idDelasusico
            return "";
        } catch (Exception ex) {
            Logger.getLogger(SuscripcionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }
    public String prepareList() {
        //recreateModel();
        return "List";
    }



    /*public  PublicacionDTO ejecutarStoreProcedure(List<Suscripcion> suscripcionesArray){
        Object[][] datos;
        ResultSet resultado;


            //Crear el objeto de conexion a la base de datos
            //Crear el objeto de conexion a la base de datos
            Connection conexion = null;
            PreparedStatement proc = null;
            Statement statement;
            try {
                conexion=getConnection();
                

                proc=conexion.prepareStatement("SELECT a.id as id,a.COSTO,SUM(a.costo*i.monto_impuesto) "
                    + "AS IVA,SUM(a.costo*i.monto_impuesto)+a.costo AS TOTAL from impuesto i,articulo a "
                    +"WHERE a.id=i.id_articulo AND a.id=?; ");

                for(Suscripcion s:suscripcionesArray)
                {
                    proc.setInt(1,s.getArticulo().getId());
                //proc.setString(2, "yamildelgado@hotmail.com");
                //proc.setDate(3,new java.sql.Date( new Date().getTime()));
                
                 resultado=proc.executeQuery();
                 while(resultado.next()){

                      System.out.println(resultado.getBigDecimal(1));
                 }
                }





            } catch (SQLException ex) {
                try {
                    ex.printStackTrace();
                    System.out.println("no me connecte a la base");
                    Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
                    conexion.close();
                }
                //Crear objeto Statement para realizar queries a la base de datos
                //Crear objeto Statement para realizar queries a la base de datos
                catch (SQLException ex1) {
                    Logger.getLogger(SuscripcionController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            //Crear objeto Statement para realizar queries a la base de datos
            //Crear objeto Statement para realizar queries a la base de datos







        return null;
    }*/



/*    public Connection getConnection(){

 Connection conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Crear el objeto de conexion a la base de datos
            //Crear el objeto de conexion a la base de datos
           

            try {
                conexion = DriverManager.getConnection("jdbc:mysql://localhost/libreriademo", "root", "root");
                System.out.println("libreriademo demo conectado");


            } catch (SQLException ex) {
                try {
                    ex.printStackTrace();
                    System.out.println("no me connecte a la base");
                    Logger.getLogger(Articulo.class.getName()).log(Level.SEVERE, null, ex);
                    conexion.close();
                }
                //Crear objeto Statement para realizar queries a la base de datos
                //Crear objeto Statement para realizar queries a la base de datos
                catch (SQLException ex1) {
                    Logger.getLogger(SuscripcionController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            //Crear objeto Statement para realizar queries a la base de datos
            //Crear objeto Statement para realizar queries a la base de datos






        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;


    }*/
    public String prepareView(Suscripcion p) {
        current=p;
       // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Suscripcion();
        
        return "Create";
    }

    private int idSuscripcion;//idSuscripcion

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    /*public Object crearCarritoCompra(){
        CarritoCompraTemporalLocal carrito = sistemaFacade.getObtenerBandejaTemporal();
    }*/
    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }


    public String create() {

        try {

            if(!articuloSeleccionado.equals(current.getArticulo()) && articuloSeleccionado.getFormato().equals(current.getArticulo().getFormato()) )
            {
                SuscripcionPK pk=new SuscripcionPK();
                pk.setIdArticulo(current.getArticulo().getId());
                pk.setIdSuscripcion(articuloSeleccionado.getId());

                current.setSuscripcionPK(pk);
                getFacade().create(current);
                JsfUtil.addSuccessMessage(("Suscripcion creada Satisfactoriamente"));
                return "Create_1";
            }

            JsfUtil.addErrorMessage("Error el articulo seleccionado no tiene el mismo formato");
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al crear suscripcion"));
            return null;
        }
    }

    public String prepareEdit(Suscripcion p) {
        current=p;
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(("Suscripcion Actualizada Satisfactoriamente"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ("Error al actualizar suscripcion"));
            return null;
        }
    }

    public String destroy(Suscripcion p) {
        current=p;
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Suscripcion eliminada Satisfactoriamente");
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
            JsfUtil.addSuccessMessage(("SuscripcionDeleted"));
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

    @FacesConverter(forClass=Suscripcion.class)
    public static class SuscripcionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SuscripcionController controller = (SuscripcionController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "suscripcionController");
            return controller.ejbFacade.find(getKey(value));
        }

        escom.libreria.info.suscripciones.SuscripcionPK getKey(String value) {
            escom.libreria.info.suscripciones.SuscripcionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new escom.libreria.info.suscripciones.SuscripcionPK();
            key.setIdSuscripcion(Integer.parseInt(values[0]));
            key.setIdArticulo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(escom.libreria.info.suscripciones.SuscripcionPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdSuscripcion());
            sb.append(SEPARATOR);
            sb.append(value.getIdArticulo());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Suscripcion) {
                Suscripcion o = (Suscripcion) object;
                return getStringKey(o.getSuscripcionPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+SuscripcionController.class.getName());
            }
        }

    }

}
