/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.documento.ejb;

import escom.info.documento.jpa.TipoDocente;
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
public class TipoDocenteFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(TipoDocente tipoDocente) {
        em.persist(tipoDocente);
    }

    public void edit(TipoDocente tipoDocente) {
        em.merge(tipoDocente);
    }

    public void remove(TipoDocente tipoDocente) {
        em.remove(em.merge(tipoDocente));
    }

    public TipoDocente find(Object id) {
        return em.find(TipoDocente.class, id);
    }

    public List<TipoDocente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TipoDocente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<TipoDocente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TipoDocente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<TipoDocente> rt = cq.from(TipoDocente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
