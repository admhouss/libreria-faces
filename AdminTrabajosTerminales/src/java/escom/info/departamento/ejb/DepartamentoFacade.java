/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.departamento.ejb;

import escom.info.departamento.jpa.Departamento;
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
public class DepartamentoFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Departamento departamento) {
        em.persist(departamento);
    }

    public void edit(Departamento departamento) {
        em.merge(departamento);
    }

    public void remove(Departamento departamento) {
        em.remove(em.merge(departamento));
    }

    public Departamento find(Object id) {
        return em.find(Departamento.class, id);
    }

    public List<Departamento> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Departamento.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Departamento> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Departamento.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Departamento> rt = cq.from(Departamento.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
