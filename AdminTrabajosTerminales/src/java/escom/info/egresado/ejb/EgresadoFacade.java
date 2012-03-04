/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.egresado.ejb;

import escom.info.egresado.jpa.Egresado;
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
public class EgresadoFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Egresado egresado) {
        em.persist(egresado);
    }

    public void edit(Egresado egresado) {
        em.merge(egresado);
    }

    public void remove(Egresado egresado) {
        em.remove(em.merge(egresado));
    }

    public Egresado find(Object id) {
        return em.find(Egresado.class, id);
    }

    public List<Egresado> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Egresado.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Egresado> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Egresado.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Egresado> rt = cq.from(Egresado.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
