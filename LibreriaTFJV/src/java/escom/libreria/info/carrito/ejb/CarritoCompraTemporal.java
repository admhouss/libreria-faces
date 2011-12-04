/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.carrito.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.Publicacion;
import escom.libreria.info.carrito.jpa.PublicacionDTO;

import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.login.sistema.SistemaController;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
    private List<PublicacionDTO> listaPublicacion=new ArrayList<PublicacionDTO>();
    private PublicacionDTO carritoDTO_Temporal;
    private BigDecimal descuento;
    private Cliente cliente;
   

   @EJB private escom.libreria.info.cliente.ejb.DescuentoClienteFacade descuentoClienteFacade;
  
    @Override
    public void addPublicacion(PublicacionDTO articulo) {
         
       
          carritoDTO_Temporal=buscarArticulo(articulo);
          
         if(carritoDTO_Temporal==null){// no existe
              articulo.setIndice(getCount());
              listaPublicacion.add(articulo);
          }else //ya existe articulo
             carritoDTO_Temporal.setCantidad(carritoDTO_Temporal.getCantidad()+1);
          
          
    }

    @Override
    public void removePublicacion(PublicacionDTO articulo) {
        //throw new UnsupportedOperationException("Not supported yet.");
           carritoDTO_Temporal=articulo;
           if(listaPublicacion.contains(carritoDTO_Temporal)){
               listaPublicacion.remove(carritoDTO_Temporal);
            }
           
    }




    @Override
    public boolean Emtity() {
       return listaPublicacion.size()==0?true:false;//true no tiene elementos
    }

    @Override
    public int getCount() {
        try{
        if(Emtity())
         return 1;
         return listaPublicacion.size()+1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<PublicacionDTO> getListPublicacion() {

        return listaPublicacion;
    }

    @Override
    public PublicacionDTO buscarArticulo(PublicacionDTO p) {
       PublicacionDTO temporal=null;
      
        for(PublicacionDTO publicacion:listaPublicacion){
               if(publicacion.getIdArticulo()==p.getIdArticulo() && publicacion.getIdPublicacion()==p.getIdPublicacion()){ //ya existe el articulo
                    temporal=publicacion;
                    //temporal=p;
                   
                   break;
               }
               
          }
        return temporal;
    }

    @Override
    public BigDecimal getMontoTotal() {
        BigDecimal montoTotal=BigDecimal.ZERO;
         if(!Emtity()){
          for(PublicacionDTO item:listaPublicacion){
               montoTotal=montoTotal.add(new BigDecimal(item.getTotal()));
          }
        }
              return montoTotal;
    }

    @Override
    public int getPosArticulo(PublicacionDTO p) {
        int  temporal=-1,count=0;

        for(PublicacionDTO publicacion:listaPublicacion){
               if(publicacion.getIdArticulo()==p.getIdArticulo() && publicacion.getIdPublicacion()==p.getIdPublicacion()){ //ya existe el articulo

                 temporal=count;
                  break;
               }

          }
        return temporal;
    }

    @Override
    public boolean actualizarArticulo(PublicacionDTO obj, int pos) {
        try{
        //listaPublicacion.remove(pos);
        listaPublicacion.set(pos, obj);
      
        return true;
        }catch(Exception e){e.printStackTrace();}
        return false;
    }

    @Override
    public PublicacionDTO buscarPublicacion(Publicacion p) {
          PublicacionDTO dt=new PublicacionDTO();
          dt.setIdArticulo(p.getArticulo().getId());
          dt.setIdPublicacion(p.getIdDc());
          return buscarArticulo(dt);
    }

   
   
}
