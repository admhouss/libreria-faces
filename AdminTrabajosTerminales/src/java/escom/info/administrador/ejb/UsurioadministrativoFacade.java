/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.administrador.ejb;

import escom.info.administrador.jpa.Usurioadministrativo;
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
public class UsurioadministrativoFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Usurioadministrativo usurioadministrativo) {
        em.persist(usurioadministrativo);
    }

    public void edit(Usurioadministrativo usurioadministrativo) {
        em.merge(usurioadministrativo);
    }

    public void remove(Usurioadministrativo usurioadministrativo) {
        em.remove(em.merge(usurioadministrativo));
    }

    public Usurioadministrativo find(Object id) {
        return em.find(Usurioadministrativo.class, id);
    }

    public List<Usurioadministrativo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Usurioadministrativo.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Usurioadministrativo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Usurioadministrativo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Usurioadministrativo> rt = cq.from(Usurioadministrativo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
