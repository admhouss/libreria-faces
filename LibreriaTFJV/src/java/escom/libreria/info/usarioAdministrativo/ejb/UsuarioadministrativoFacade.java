/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.usarioAdministrativo.ejb;

import escom.libreria.info.usarioAdministrativo.jpa.Usuarioadministrativo;
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
public class UsuarioadministrativoFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Usuarioadministrativo usuarioadministrativo) {
        em.persist(usuarioadministrativo);
    }

    public void edit(Usuarioadministrativo usuarioadministrativo) {
        em.merge(usuarioadministrativo);
    }

    public void remove(Usuarioadministrativo usuarioadministrativo) {
        em.remove(em.merge(usuarioadministrativo));
    }

    public Usuarioadministrativo find(Object id) {
        return em.find(Usuarioadministrativo.class, id);
    }

    public List<Usuarioadministrativo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Usuarioadministrativo.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Usuarioadministrativo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Usuarioadministrativo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Usuarioadministrativo> rt = cq.from(Usuarioadministrativo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
