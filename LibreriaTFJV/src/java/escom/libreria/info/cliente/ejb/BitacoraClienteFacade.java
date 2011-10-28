/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.ejb;

import escom.libreria.info.cliente.jpa.BitacoraCliente;
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
public class BitacoraClienteFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(BitacoraCliente bitacoraCliente) {
        em.persist(bitacoraCliente);
    }

    public void edit(BitacoraCliente bitacoraCliente) {
        em.merge(bitacoraCliente);
    }

    public void remove(BitacoraCliente bitacoraCliente) {
        em.remove(em.merge(bitacoraCliente));
    }

    public BitacoraCliente find(Object id) {
        return em.find(BitacoraCliente.class, id);
    }

    public List<BitacoraCliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BitacoraCliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<BitacoraCliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BitacoraCliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<BitacoraCliente> rt = cq.from(BitacoraCliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
