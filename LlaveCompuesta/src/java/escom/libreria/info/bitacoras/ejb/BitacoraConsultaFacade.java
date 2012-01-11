/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.bitacoras.ejb;

import escom.libreria.info.bitacoras.BitacoraConsulta;
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
public class BitacoraConsultaFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(BitacoraConsulta bitacoraConsulta) {
        em.persist(bitacoraConsulta);
    }

    public void edit(BitacoraConsulta bitacoraConsulta) {
        em.merge(bitacoraConsulta);
    }

    public void remove(BitacoraConsulta bitacoraConsulta) {
        em.remove(em.merge(bitacoraConsulta));
    }

    public BitacoraConsulta find(Object id) {
        return em.find(BitacoraConsulta.class, id);
    }

    public List<BitacoraConsulta> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BitacoraConsulta.class));
        return em.createQuery(cq).getResultList();
    }

    public List<BitacoraConsulta> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BitacoraConsulta.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<BitacoraConsulta> rt = cq.from(BitacoraConsulta.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
