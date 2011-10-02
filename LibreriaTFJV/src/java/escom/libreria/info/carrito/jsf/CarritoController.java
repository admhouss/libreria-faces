/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.jsf;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.carrito.ejb.CarritoCompraTemporalLocal;
import escom.libreria.info.login.sistema.SistemaController;
import java.util.List;
import javax.ejb.EJB;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author xxx
 */
@ManagedBean(name="carritoController")
@SessionScoped
public class CarritoController {

    @ManagedProperty("#{sistemaController}")
    private SistemaController sistemaController;
    @EJB private  escom.libreria.info.login.ejb.SistemaFacade sistemaFacade;


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
 public void agregarArticulo(Articulo articulo){
      
         carritoCompraTemporalLocal.addArticulo(articulo);
         JsfUtil.addSuccessMessage("Articulo agregado Satisfactoriamente");
    }
    public void borrarArticulo(Articulo articulo){
          carritoCompraTemporalLocal.removeArticulo(articulo);
    }
    public List<Articulo> getListArticulos(){
        return carritoCompraTemporalLocal.getListArticulos();
    }

     private CarritoCompraTemporalLocal carritoCompraTemporalLocal=ObtenerCarrito();

}
