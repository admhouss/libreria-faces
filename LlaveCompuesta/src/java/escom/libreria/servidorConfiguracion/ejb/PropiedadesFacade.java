/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.servidorConfiguracion.ejb;

import escom.libreria.servidorConfiguracion.Propiedades;
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
public class PropiedadesFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Propiedades propiedades) {
        em.persist(propiedades);
    }

    public void edit(Propiedades propiedades) {
        em.merge(propiedades);
    }

    public void remove(Propiedades propiedades) {
        em.remove(em.merge(propiedades));
    }

    public Propiedades find(Object id) {
        return em.find(Propiedades.class, id);
    }

    public List<Propiedades> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Propiedades.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Propiedades> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Propiedades.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Propiedades> rt = cq.from(Propiedades.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
