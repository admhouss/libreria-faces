/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.proveedorArticulo.ejb;

import escom.libreria.info.proveedor.jpa.ProveedorArticulo;
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
public class ProveedorArticuloFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(ProveedorArticulo proveedorArticulo) {
        em.persist(proveedorArticulo);
    }

    public void edit(ProveedorArticulo proveedorArticulo) {
        em.merge(proveedorArticulo);
    }

    public void remove(ProveedorArticulo proveedorArticulo) {
        em.remove(em.merge(proveedorArticulo));
    }

    public ProveedorArticulo find(Object id) {
        return em.find(ProveedorArticulo.class, id);
    }

    public List<ProveedorArticulo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ProveedorArticulo.class));
        return em.createQuery(cq).getResultList();
    }

    public List<ProveedorArticulo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ProveedorArticulo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<ProveedorArticulo> rt = cq.from(ProveedorArticulo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
