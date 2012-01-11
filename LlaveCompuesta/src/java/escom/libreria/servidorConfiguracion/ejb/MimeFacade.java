/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.servidorConfiguracion.ejb;

import escom.libreria.servidorConfiguracion.Mime;
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
public class MimeFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Mime mime) {
        em.persist(mime);
    }

    public void edit(Mime mime) {
        em.merge(mime);
    }

    public void remove(Mime mime) {
        em.remove(em.merge(mime));
    }

    public Mime find(Object id) {
        return em.find(Mime.class, id);
    }

    public List<Mime> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Mime.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Mime> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Mime.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Mime> rt = cq.from(Mime.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
