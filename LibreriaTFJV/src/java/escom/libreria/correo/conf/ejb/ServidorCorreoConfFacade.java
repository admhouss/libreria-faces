/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo.conf.ejb;

import escom.libreria.correo.conf.ServidorCorreoConf;
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
public class ServidorCorreoConfFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(ServidorCorreoConf servidorCorreoConf) {
        em.persist(servidorCorreoConf);
    }

    public void edit(ServidorCorreoConf servidorCorreoConf) {
        em.merge(servidorCorreoConf);
    }

    public void remove(ServidorCorreoConf servidorCorreoConf) {
        em.remove(em.merge(servidorCorreoConf));
    }

    public ServidorCorreoConf find(Object id) {
        return em.find(ServidorCorreoConf.class, id);
    }

    public List<ServidorCorreoConf> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ServidorCorreoConf.class));
        return em.createQuery(cq).getResultList();
    }

    public List<ServidorCorreoConf> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ServidorCorreoConf.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<ServidorCorreoConf> rt = cq.from(ServidorCorreoConf.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
