/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.facturacion.ejb;

import escom.libreria.info.facturacion.TipoArticulo;
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
public class TipoArticuloFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(TipoArticulo tipoArticulo) {
        em.persist(tipoArticulo);
    }

    public void edit(TipoArticulo tipoArticulo) {
        em.merge(tipoArticulo);
    }

    public void remove(TipoArticulo tipoArticulo) {
        em.remove(em.merge(tipoArticulo));
    }

    public TipoArticulo find(Object id) {
        return em.find(TipoArticulo.class, id);
    }

    public List<TipoArticulo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TipoArticulo.class));
        return em.createQuery(cq).getResultList();
    }

    public List<TipoArticulo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TipoArticulo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<TipoArticulo> rt = cq.from(TipoArticulo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
