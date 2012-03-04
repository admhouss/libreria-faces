/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.profesor.ejb;

import escom.info.profesor.jpa.Profesor;
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
public class ProfesorFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Profesor profesor) {
        em.persist(profesor);
    }

    public void edit(Profesor profesor) {
        em.merge(profesor);
    }

    public void remove(Profesor profesor) {
        em.remove(em.merge(profesor));
    }

    public Profesor find(Object id) {
        return em.find(Profesor.class, id);
    }

    public List<Profesor> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Profesor.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Profesor> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Profesor.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Profesor> rt = cq.from(Profesor.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
