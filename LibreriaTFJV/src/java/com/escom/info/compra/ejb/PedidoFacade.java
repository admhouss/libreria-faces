/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra.ejb;

import com.escom.info.compra.Pedido;
import com.paypal.jsf.CompraDTO;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
    @PersistenceContext(unitName = "LibreriaTFJVPU")
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
                TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido >= :start", Pedido.class)
                .setParameter("idCliente", id)
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
                TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido >= :start", Pedido.class)
                .setParameter("idCliente", id)
                .setParameter("start", start, TemporalType.DATE)
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

    public BigDecimal getPedidoMontoTotal(String id,Date fecha) {
        BigDecimal  pedidos=BigDecimal.ZERO;
        try{
        TypedQuery<BigDecimal> query=em.createQuery("SELECT SUM(p.precioTotal) FROM Pedido p WHERE p.cliente.id =:idCliente AND  p.fechaPedido>=:fecha",BigDecimal.class)
                                 .setParameter("idCliente", id)
                                 .setParameter("fecha", fecha);
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
}
