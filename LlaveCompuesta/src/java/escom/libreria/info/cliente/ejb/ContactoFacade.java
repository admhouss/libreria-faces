/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.ejb;

import escom.libreria.info.cliente.Contacto;
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
public class ContactoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Contacto contacto) {
        em.persist(contacto);
    }

    public void edit(Contacto contacto) {
        em.merge(contacto);
    }

    public void remove(Contacto contacto) {
        em.remove(em.merge(contacto));
    }

    public Contacto find(Object id) {
        return em.find(Contacto.class, id);
    }

    public List<Contacto> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Contacto.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Contacto> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Contacto.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Contacto> rt = cq.from(Contacto.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Contacto> getObtenerContactosByCliente(String idCliente) {
        TypedQuery<Contacto> query=em.createQuery("SELECT c FROM Contacto c WHERE c.cliente.id =:idCliente", Contacto.class)
        .setParameter("idCliente", idCliente);
        List<Contacto> l= query.getResultList();
       return l;

    }

}
