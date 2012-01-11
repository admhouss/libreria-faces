/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones.ejb;

import escom.libreria.info.suscripciones.SuscripcionEnvios;
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
public class SuscripcionEnviosFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(SuscripcionEnvios suscripcionEnvios) {
        em.persist(suscripcionEnvios);
    }

    public void edit(SuscripcionEnvios suscripcionEnvios) {
        em.merge(suscripcionEnvios);
    }

    public void remove(SuscripcionEnvios suscripcionEnvios) {
        em.remove(em.merge(suscripcionEnvios));
    }

    public SuscripcionEnvios find(Object id) {
        return em.find(SuscripcionEnvios.class, id);
    }

    public List<SuscripcionEnvios> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SuscripcionEnvios.class));
        return em.createQuery(cq).getResultList();
    }

    public List<SuscripcionEnvios> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SuscripcionEnvios.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SuscripcionEnvios> rt = cq.from(SuscripcionEnvios.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
