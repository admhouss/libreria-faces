/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.info.documento.ejb;

import escom.info.documento.jpa.Documento;
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
public class DocumentoFacade {
    @PersistenceContext(unitName = "adminPU")
    private EntityManager em;

    public void create(Documento documento) {
        em.persist(documento);
    }

    public void edit(Documento documento) {
        em.merge(documento);
    }

    public void remove(Documento documento) {
        em.remove(em.merge(documento));
    }

    public Documento find(Object id) {
        return em.find(Documento.class, id);
    }

    public List<Documento> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Documento.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Documento> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Documento.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Documento> rt = cq.from(Documento.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
