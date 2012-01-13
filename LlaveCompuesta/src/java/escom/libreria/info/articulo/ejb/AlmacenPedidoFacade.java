/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.AlmacenPedido;
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
public class AlmacenPedidoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(AlmacenPedido almacenPedido) {
        em.persist(almacenPedido);
    }

    public void edit(AlmacenPedido almacenPedido) {
        em.merge(almacenPedido);
    }

    public void remove(AlmacenPedido almacenPedido) {
        em.remove(em.merge(almacenPedido));
    }

    public AlmacenPedido find(Object id) {
        return em.find(AlmacenPedido.class, id);
    }

    public List<AlmacenPedido> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(AlmacenPedido.class));
        return em.createQuery(cq).getResultList();
    }

    public List<AlmacenPedido> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(AlmacenPedido.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<AlmacenPedido> rt = cq.from(AlmacenPedido.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<AlmacenPedido> getListMasVendido(){


        return null;
    }

}
