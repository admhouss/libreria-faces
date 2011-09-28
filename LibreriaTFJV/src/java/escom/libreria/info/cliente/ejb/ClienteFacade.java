/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.ejb;

import escom.libreria.info.cliente.jpa.Cliente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author xxx
 */
@Stateless
public class ClienteFacade  {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Cliente cliente) {
        em.persist(cliente);
    }

    public void edit(Cliente cliente) {
        em.merge(cliente);
    }

    public void remove(Cliente cliente) {
        em.remove(em.merge(cliente));
    }

    public Cliente find(Object id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Cliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Cliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Cliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Cliente> rt = cq.from(Cliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Cliente> getListClientesActive() {
        String activado="activado%";
        TypedQuery<Cliente> l=em.createQuery("SELECT c FROM Cliente c WHERE c.idEstado.nombre LIKE :activado ORDER BY c.nombre,c.paterno,c.materno",Cliente.class)
        .setParameter("activado",activado);
       List<Cliente> c= l.getResultList();
       return c;
    }


}
