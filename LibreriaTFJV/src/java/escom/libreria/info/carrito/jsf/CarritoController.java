/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.login.sistema.SistemaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author xxx
 */
@ManagedBean(name="carritoController")
@SessionScoped
public class CarritoController implements Serializable{

    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    @EJB private  escom.libreria.info.login.ejb.SistemaFacade sistemaFacade;

    private List<CarritoDTO> listcarritoDTO;


    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }

    /** Creates a new instance of CarritoController */
    public CarritoController() {

        
    }

    private CarritoCompraTemporalLocal ObtenerCarrito(){
         if(carritoCompraTemporalLocal==null){
            try{
          carritoCompraTemporalLocal=sistemaFacade.getObtenerBandejaTemporal();
            }catch(Exception e){
                System.out.println("No carrito creado");
            }
        }
         return carritoCompraTemporalLocal;
    }
 public void agregarArticulo(Publicacion articulo){
         if(sistemaController.getCliente()!=null){
             carritoCompraTemporalLocal=ObtenerCarrito();
             carritoCompraTemporalLocal.addPublicacion(articulo);
             JsfUtil.addSuccessMessage("Articulo agregado Satisfactoriamente");
         }else{
            
              try {
                        JsfUtil.addErrorMessage("Es necesario que se identifique");
                        ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
                        external.redirect(external.getRequestContextPath() + "/faces/login/Create.xhtml");

              } catch (IOException ex) {
                         Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
              }

         }
    }
    public void borrarArticulo(Publicacion articulo){
          carritoCompraTemporalLocal.removePublicacion(articulo);
    }
    public List<CarritoDTO> getListArticulos(){
        if(carritoCompraTemporalLocal!=null){
         listcarritoDTO=carritoCompraTemporalLocal.getListPublicacion();
         listcarritoDTO=listcarritoDTO==null?new ArrayList<CarritoDTO>():listcarritoDTO;
        }
        return listcarritoDTO;
    }

    public String destroy(CarritoDTO  item){

        borrarArticulo(item.getPublicacion());
        JsfUtil.addSuccessMessage("Articulo Eliminado satisfactoriamente!!");
        return "/carrito/Carrito";

    }

    public int getcountElement(){
        if(carritoCompraTemporalLocal!=null)
        return carritoCompraTemporalLocal.getCount();
        return 0;
    }

     private CarritoCompraTemporalLocal carritoCompraTemporalLocal=ObtenerCarrito();//obetenemso carrito compra

}
