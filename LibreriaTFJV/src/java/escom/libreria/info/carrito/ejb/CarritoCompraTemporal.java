/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.carrito.jsf.CarritoDTO;
import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.login.sistema.SistemaController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author xxx
 */
@Stateful
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 30)
public class CarritoCompraTemporal implements CarritoCompraTemporalLocal {
    private List<CarritoDTO> listaPublicacion=new ArrayList<CarritoDTO>();
    private CarritoDTO carritoDTO_Temporal;
    private BigDecimal descuento;
    private Cliente cliente;
   

   @EJB private escom.libreria.info.cliente.ejb.DescuentoClienteFacade descuentoClienteFacade;
   @ManagedProperty("#{sistemaController}")
   private SistemaController sistemaController;

    public SistemaController getSistemaController() {
        return sistemaController;
    }

    public void setSistemaController(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
    }




    


    private BigDecimal obtenerDescuentoMayor(String idCorreo){
        String correo=idCorreo;
        BigDecimal descuentoMax=descuentoClienteFacade.obtenerMaxioDescuento(correo);
        if(descuentoMax==null)
        descuentoMax=BigDecimal.ZERO;
        return descuentoMax;
    }



    @Override
    public void addPublicacion(Publicacion articulo) {
          boolean inserta_update=false;
          int tam;

          carritoDTO_Temporal=buscarArticulo(articulo);

          if(carritoDTO_Temporal==null){// no existe
              tam=getCount()+1;
              //descuento=obtenerDescuentoMayor();
              descuento=BigDecimal.ONE;
              carritoDTO_Temporal=new CarritoDTO(tam,tam,descuento,articulo);
              listaPublicacion.add(carritoDTO_Temporal);
          }else //ya existe articulo
             carritoDTO_Temporal.setCantidad(carritoDTO_Temporal.getCantidad()+1);
           
          
    }

    @Override
    public void removePublicacion(Publicacion articulo) {
        //throw new UnsupportedOperationException("Not supported yet.");
           carritoDTO_Temporal=buscarArticulo(articulo);
           if(listaPublicacion.contains(carritoDTO_Temporal)){
             if(carritoDTO_Temporal.getCantidad()>1)
             carritoDTO_Temporal.setCantidad(carritoDTO_Temporal.getCantidad()-1);
             else
             listaPublicacion.remove(carritoDTO_Temporal);

        }
           
    }

   

    @Override
    public boolean Emtity() {
       return listaPublicacion.isEmpty();//true no tiene elementos
    }

    @Override
    public int getCount() {
        if(Emtity())
         return 0;
         return listaPublicacion.size();

    }

    @Override
    public List<CarritoDTO> getListPublicacion() {

        return listaPublicacion;
    }

    @Override
    public CarritoDTO buscarArticulo(Publicacion p) {
        CarritoDTO temporal=null;
        for(CarritoDTO publicacion:listaPublicacion){
               if(publicacion.getPublicacion().equals(p)){ //ya existe el articulo
                    temporal=publicacion;
                   break;
               }
          }
        return temporal;
    }

    
   
}
