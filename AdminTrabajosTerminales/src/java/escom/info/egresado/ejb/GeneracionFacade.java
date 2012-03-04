/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.egresado.ejb;

import escom.info.egresado.jpa.Generacion;
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
public class GeneracionFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Generacion generacion) {
        em.persist(generacion);
    }

    public void edit(Generacion generacion) {
        em.merge(generacion);
    }

    public void remove(Generacion generacion) {
        em.remove(em.merge(generacion));
    }

    public Generacion find(Object id) {
        return em.find(Generacion.class, id);
    }

    public List<Generacion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Generacion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Generacion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Generacion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Generacion> rt = cq.from(Generacion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
