/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras.ejb;

import escom.libreria.info.compras.Enviorealizado;
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
public class EnviorealizadoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Enviorealizado enviorealizado) {
        em.persist(enviorealizado);
    }

    public void edit(Enviorealizado enviorealizado) {
        em.merge(enviorealizado);
    }

    public void remove(Enviorealizado enviorealizado) {
        em.remove(em.merge(enviorealizado));
    }

    public Enviorealizado find(Object id) {
        return em.find(Enviorealizado.class, id);
    }

    public List<Enviorealizado> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Enviorealizado.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Enviorealizado> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Enviorealizado.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Enviorealizado> rt = cq.from(Enviorealizado.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Enviorealizado> getEnviosRealizadosByCliente(String id) {
        return null;
    }



}
