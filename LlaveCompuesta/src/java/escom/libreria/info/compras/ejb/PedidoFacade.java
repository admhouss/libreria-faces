/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras.ejb;

import com.paypal.jsf.CompraDTO;
import escom.libreria.info.compras.Pedido;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author xxx
 */
@Stateless
public class PedidoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Pedido pedido) {
        em.persist(pedido);
    }

    public void edit(Pedido pedido) {
        em.merge(pedido);
    }

    public void remove(Pedido pedido) {
        em.remove(em.merge(pedido));
    }

    public Pedido find(Object id) {
        return em.find(Pedido.class, id);
    }

    public List<Pedido> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Pedido.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Pedido> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Pedido.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Pedido> rt = cq.from(Pedido.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

     public List<Pedido> getListPedidoHotByCliernte(String id,Date start) {
        //try {


            List<Pedido> pedidos = null;


            try {
               // TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido BETWEEN :start AND :end", Pedido.class).setParameter("idCliente", id)
                TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido >= :start AND p.estado LIKE :estado", Pedido.class)
                .setParameter("idCliente", id)
                 .setParameter("estado","%procesando%")
                .setParameter("start", start, TemporalType.DATE);
                //.setParameter("end",endfecha, TemporalType.DATE);
                pedidos = query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pedidos;

    }

    public Pedido getListPedidoHotByCliernteOne(String id,Date start) {
        //try {


            Pedido pedidos = null;


            try {
               // TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido BETWEEN :start AND :end", Pedido.class).setParameter("idCliente", id)
                TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido >= :start AND p.estado LIKE :estado", Pedido.class)
                .setParameter("idCliente", id)
                .setParameter("start", start, TemporalType.DATE)
                .setParameter("estado","%procesando%")
                .setMaxResults(1);

                pedidos = query.getSingleResult();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("No ay ningun pedido con esta fecha");
            }
            return pedidos;

    }


     public List<Pedido> getListPedidoByFechas(String id,Date start,Date endfecha) {
        //try {


            List<Pedido> pedidos = null;


            try {
                TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido BETWEEN :start AND :end", Pedido.class).setParameter("idCliente", id)
               // TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido >= :start", Pedido.class)
                .setParameter("idCliente", id)
                .setParameter("start", start, TemporalType.DATE)
                .setParameter("end",endfecha, TemporalType.DATE);
                pedidos = query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pedidos;
        //} catch (ParseException ex) {
          //  Logger.getLogger(PedidoFacade.class.getName()).log(Level.SEVERE, null, ex);
        //}

    }
    public List<Pedido> getListPedidoByCliente(String id) {
        List<Pedido>  pedidos=null;




        try{
        TypedQuery<Pedido> query=em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente ORDER BY p.fechaPedido ASC",Pedido.class)
                 .setParameter("idCliente", id);

                                  pedidos= query.getResultList();
        }catch(Exception e){e.printStackTrace();}

       return pedidos;
    }

    public BigDecimal getPedidoMontoTotal(int idPedido) {
        BigDecimal  pedidos=BigDecimal.ZERO;
        try{
        TypedQuery<BigDecimal> query=em.createQuery("SELECT SUM(p.precioTotal) FROM Pedido p  WHERE p.pedidoPK.idPedido=:idPedido AND p.estado LIKE :estado",BigDecimal.class)
                 .setParameter("idPedido", idPedido)
                 .setParameter("estado", "PROCESANDO");
                //("SELECT SUM(p.precioTotal) FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido>=:fecha AND p.estado LIKE :estado",BigDecimal.class)

                                 //.setParameter("idCliente", id)

                                 //.setParameter("fecha", fecha);
         pedidos= query.getSingleResult();
         if(pedidos==null)
             pedidos=BigDecimal.ZERO;
        }catch(Exception e){}

       return pedidos;
    }
    public Pedido getPedidoByCliente(String id,Integer idArticulo,int idPedido) {
       Pedido  pedidos=null;
        try{
        TypedQuery<Pedido> query=em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND p.pedidoPK.idArticulo=:idArticulo AND p.pedidoPK.idPedido=:idPedido",Pedido.class)
                                 .setParameter("idCliente", id)
                                 .setParameter("idArticulo", idArticulo)
                                  .setParameter("idPedido",idPedido)
                                 .setMaxResults(1);
         pedidos= query.getSingleResult();
        }catch(Exception e){}

       return pedidos;
    }



    public Pedido getPedidoByDay(Date startfecha,String idCliente) {
       Pedido  pedidos=null;
        try{
        TypedQuery<Pedido> query=em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id=:idCliente ANDp.fechaPedido BETWENN :start AND :end",Pedido.class)
                                 .setParameter("start",startfecha,TemporalType.DATE)
                                 .setParameter("end",new Date(),TemporalType.DATE)
                                 .setParameter("idCliente",idCliente)
                                 .setMaxResults(1);
         pedidos= query.getSingleResult();
        }catch(Exception e){}

       return pedidos;
    }

    public CompraDTO getSuperTotal(int idPedido) {
        CompraDTO compra=new CompraDTO();
         TypedQuery<Object[]> query=em.createQuery("SELECT SUM(p.impuesto),SUM(p.descuento),SUM(p.precioTotal) FROM Pedido p WHERE p.pedidoPK.idPedido=:idPedido",Object[].class)
                                 .setParameter("idPedido",idPedido);
         List<Object[]> results = query.getResultList();
  for (Object[] result : results) {
  compra.setImpuesto((BigDecimal) result[0]);
  compra.setDescuento((BigDecimal) result[1]);
  compra.setTotalMonto((BigDecimal) result[2]);
      //System.out.println("impuesto: " + result[0] + ", descuento: " + result[1]+"precio toal"+result[2]);
  }

  return compra;


    }

    public List<Pedido> getAllpedidosByid(int idPedido) {
         List<Pedido> lista_pedidos=null;
         try{
         TypedQuery<Pedido> query=em.createQuery("SELECT p FROM Pedido p WHERE p.pedidoPK.idPedido=:idPedido",Pedido.class)
                  .setParameter("idPedido",idPedido);
         lista_pedidos=query.getResultList();
        }catch(Exception e){
        }
         return lista_pedidos;
    }


public Date getHoy(){
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
            Logger.getLogger(PedidoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
}

    public int borrarPedido(Pedido pedido) {
       int idPedido= pedido.getPedidoPK().getIdPedido();
       int idArticulo=pedido.getPedidoPK().getIdArticulo();

        Query query = em.createQuery("DELETE  FROM Pedido p WHERE p.pedidoPK.idPedido=:idPedido AND  p.pedidoPK.idArticulo=:idArticulo",Pedido.class)
                  .setParameter("idPedido",idPedido)
                  .setParameter("idArticulo",idArticulo);
        int deleted = query.executeUpdate();
        return deleted;

    }

    public Pedido buscarArticulo(int idPedido, Integer idArticulo) {
         Pedido pedido=null;
         try{
         TypedQuery<Pedido> query=em.createQuery("SELECT p FROM Pedido p WHERE p.pedidoPK.idPedido=:idPedido AND p.pedidoPK.idArticulo=:idArticulo",Pedido.class)
                  .setParameter("idPedido",idPedido)
                  .setParameter("idArticulo",idArticulo);
         pedido=query.getSingleResult();
        }catch(Exception e){
        }
         return pedido;
    }

    public void cambiarEstadoPedidoAll(int idPedido,String type) { //type CANCELADO,PROCESANDO,COMPRADO
         Query query = em.createQuery("UPDATE Pedido p SET p.estado=:estado WHERE p.pedidoPK.idPedido=:idPedido",Pedido.class)
        .setParameter("idPedido",idPedido)
        .setParameter("estado", type);

        int deleted = query.executeUpdate();


    }

    public List<Pedido> getListaPedidosByidPedios(int idPedido) {
        List<Pedido> pedido=null;
         try{
         TypedQuery<Pedido> query=em.createQuery("SELECT p FROM Pedido p WHERE p.pedidoPK.idPedido=:idPedido",Pedido.class)
                  .setParameter("idPedido",idPedido);

         pedido=query.getResultList();
        }catch(Exception e){
        }
         return pedido;
    }

    public int getIdPedidoProcesando(String idCliente, String string) {
       TypedQuery<Pedido> query=(TypedQuery<Pedido>) em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id=:idCliente AND p.estado=:estado",Pedido.class)
               .setParameter("idCliente", idCliente)
               .setParameter("estado", string);

       return 1;
    }

    public int buscarIdPeidoMaximo(String idCliente,String estado) {
        Integer idPedido=null;
        TypedQuery<Integer> query=em.createQuery("SELECT MAX(p.pedidoPK.idPedido) FROM Pedido p WHERE p.cliente.id=:idCliente AND p.estado=:estado",Integer.class)
                  .setParameter("idCliente",idCliente)
                  .setParameter("estado", estado);
        idPedido=query.getSingleResult();
        return idPedido;

    }

}
