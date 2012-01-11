/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.Promocion;
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
public class PromocionFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Promocion promocion) {
        em.persist(promocion);
    }

    public void edit(Promocion promocion) {
        em.merge(promocion);
    }

    public void remove(Promocion promocion) {
        em.remove(em.merge(promocion));
    }

    public Promocion find(Object id) {
        return em.find(Promocion.class, id);
    }

    public List<Promocion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Promocion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Promocion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Promocion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Promocion> rt = cq.from(Promocion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
