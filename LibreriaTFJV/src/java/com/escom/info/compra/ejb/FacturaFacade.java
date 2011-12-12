/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra.ejb;

import com.escom.info.compra.Factura;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author xxx
 */
@Stateless
public class FacturaFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Factura factura) {
        em.persist(factura);
    }

    public void edit(Factura factura) {
        em.merge(factura);
    }

    public void remove(Factura factura) {
        em.remove(em.merge(factura));
    }

    public Factura find(Object id) {
        return em.find(Factura.class, id);
    }

    public List<Factura> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Factura.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Factura> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Factura.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Factura> rt = cq.from(Factura.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
