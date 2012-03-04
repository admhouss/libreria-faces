/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.departamento.ejb;

import escom.info.departamento.jpa.Responsable;
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
public class ResponsableFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Responsable responsable) {
        em.persist(responsable);
    }

    public void edit(Responsable responsable) {
        em.merge(responsable);
    }

    public void remove(Responsable responsable) {
        em.remove(em.merge(responsable));
    }

    public Responsable find(Object id) {
        return em.find(Responsable.class, id);
    }

    public List<Responsable> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Responsable.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Responsable> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Responsable.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Responsable> rt = cq.from(Responsable.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
