/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.ncompra.ejb;

import com.escom.info.compra.Compra;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author xxx
 */
@Stateless
public class CompraFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Compra compra) {
        em.persist(compra);
    }

    public void edit(Compra compra) {
        em.merge(compra);
    }

    public void remove(Compra compra) {
        em.remove(em.merge(compra));
    }

    public Compra find(Object id) {
        return em.find(Compra.class, id);
    }

    public List<Compra> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Compra.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Compra> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Compra.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Compra> rt = cq.from(Compra.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public boolean buscarCompra(int idPedido) {
         Compra compra=null;
        TypedQuery<Compra> query=em.createQuery("SELECT c FROM Compra c WHERE c.idPedido = :idPedido",Compra.class)
        .setParameter("idPedido", idPedido);
        try{
         compra=query.getSingleResult();
        }catch(Exception e){}

        if(compra==null)
         return false;
        else
         return true;
    }

    public List<Compra> buscarCompraByCliente(String idCliente) {
         List<Compra> compra=null;
         try{
         TypedQuery<Compra> query=em.createQuery("SELECT c FROM Compra c WHERE c.idCliente=:cliente",Compra.class)
            
            .setParameter("idCliente",idCliente);
            compra=query.getResultList();
        }catch(Exception e){}
         return compra;

    }

}
