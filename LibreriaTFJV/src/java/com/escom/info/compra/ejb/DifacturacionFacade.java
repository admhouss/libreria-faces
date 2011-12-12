/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra.ejb;

import com.escom.info.compra.Difacturacion;
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
public class DifacturacionFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Difacturacion difacturacion) {
        em.persist(difacturacion);
    }

    public void edit(Difacturacion difacturacion) {
        em.merge(difacturacion);
    }

    public void remove(Difacturacion difacturacion) {
        em.remove(em.merge(difacturacion));
    }

    public Difacturacion find(Object id) {
        return em.find(Difacturacion.class, id);
    }

    public List<Difacturacion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Difacturacion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Difacturacion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Difacturacion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Difacturacion> rt = cq.from(Difacturacion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Difacturacion> getDireccionFacturaCliente(String id) {
         List<Difacturacion> direcciones=null;
        try{
        TypedQuery<Difacturacion> query=em.createQuery("SELECT d FROM Difacturacion d WHERE d.cliente.id = :idCliente",Difacturacion.class)
                .setParameter("idCliente", id);
         direcciones=query.getResultList();
        }catch(Exception e){}

        return direcciones;

    }

}
