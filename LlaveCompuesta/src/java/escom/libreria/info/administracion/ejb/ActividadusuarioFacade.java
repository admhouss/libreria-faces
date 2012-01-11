/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.administracion.ejb;

import escom.libreria.info.administracion.Actividadusuario;
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
public class ActividadusuarioFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Actividadusuario actividadusuario) {
        em.persist(actividadusuario);
    }

    public void edit(Actividadusuario actividadusuario) {
        em.merge(actividadusuario);
    }

    public void remove(Actividadusuario actividadusuario) {
        em.remove(em.merge(actividadusuario));
    }

    public Actividadusuario find(Object id) {
        return em.find(Actividadusuario.class, id);
    }

    public List<Actividadusuario> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Actividadusuario.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Actividadusuario> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Actividadusuario.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Actividadusuario> rt = cq.from(Actividadusuario.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
