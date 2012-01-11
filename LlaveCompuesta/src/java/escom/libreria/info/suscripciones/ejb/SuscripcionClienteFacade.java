/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones.ejb;

import escom.libreria.info.suscripciones.SuscripcionCliente;
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
public class SuscripcionClienteFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(SuscripcionCliente suscripcionCliente) {
        em.persist(suscripcionCliente);
    }

    public void edit(SuscripcionCliente suscripcionCliente) {
        em.merge(suscripcionCliente);
    }

    public void remove(SuscripcionCliente suscripcionCliente) {
        em.remove(em.merge(suscripcionCliente));
    }

    public SuscripcionCliente find(Object id) {
        return em.find(SuscripcionCliente.class, id);
    }

    public List<SuscripcionCliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SuscripcionCliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<SuscripcionCliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SuscripcionCliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SuscripcionCliente> rt = cq.from(SuscripcionCliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
