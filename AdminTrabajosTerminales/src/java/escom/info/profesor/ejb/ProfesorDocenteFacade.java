/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.profesor.ejb;

import escom.info.profesor.jpa.ProfesorDocente;
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
public class ProfesorDocenteFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(ProfesorDocente profesorDocente) {
        em.persist(profesorDocente);
    }

    public void edit(ProfesorDocente profesorDocente) {
        em.merge(profesorDocente);
    }

    public void remove(ProfesorDocente profesorDocente) {
        em.remove(em.merge(profesorDocente));
    }

    public ProfesorDocente find(Object id) {
        return em.find(ProfesorDocente.class, id);
    }

    public List<ProfesorDocente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ProfesorDocente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<ProfesorDocente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ProfesorDocente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<ProfesorDocente> rt = cq.from(ProfesorDocente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
