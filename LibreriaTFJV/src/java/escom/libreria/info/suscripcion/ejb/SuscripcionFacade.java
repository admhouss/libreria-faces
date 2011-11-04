/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripcion.ejb;

import escom.libreria.info.articulo.jpa.Suscripcion;
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
public class SuscripcionFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Suscripcion suscripcion) {
        em.persist(suscripcion);
    }

    public void edit(Suscripcion suscripcion) {
        em.merge(suscripcion);
    }

    public void remove(Suscripcion suscripcion) {
        em.remove(em.merge(suscripcion));
    }

    public Suscripcion find(Object id) {
        return em.find(Suscripcion.class, id);
    }

    public List<Suscripcion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Suscripcion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Suscripcion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Suscripcion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Suscripcion> rt = cq.from(Suscripcion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
