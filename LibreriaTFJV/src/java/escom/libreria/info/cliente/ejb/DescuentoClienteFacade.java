/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.ejb;

import escom.libreria.info.cliente.jpa.DescuentoCliente;
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
public class DescuentoClienteFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(DescuentoCliente descuentoCliente) {
        em.persist(descuentoCliente);
    }

    public void edit(DescuentoCliente descuentoCliente) {
        em.merge(descuentoCliente);
    }

    public void remove(DescuentoCliente descuentoCliente) {
        em.remove(em.merge(descuentoCliente));
    }

    public DescuentoCliente find(Object id) {
        return em.find(DescuentoCliente.class, id);
    }

    public List<DescuentoCliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DescuentoCliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<DescuentoCliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DescuentoCliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<DescuentoCliente> rt = cq.from(DescuentoCliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
