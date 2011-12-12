/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra.ejb;

import com.escom.info.compra.FacturaGeneral;
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
public class FacturaGeneralFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(FacturaGeneral facturaGeneral) {
        em.persist(facturaGeneral);
    }

    public void edit(FacturaGeneral facturaGeneral) {
        em.merge(facturaGeneral);
    }

    public void remove(FacturaGeneral facturaGeneral) {
        em.remove(em.merge(facturaGeneral));
    }

    public FacturaGeneral find(Object id) {
        return em.find(FacturaGeneral.class, id);
    }

    public List<FacturaGeneral> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(FacturaGeneral.class));
        return em.createQuery(cq).getResultList();
    }

    public List<FacturaGeneral> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(FacturaGeneral.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<FacturaGeneral> rt = cq.from(FacturaGeneral.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
