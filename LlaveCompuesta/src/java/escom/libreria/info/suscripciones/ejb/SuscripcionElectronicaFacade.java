/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones.ejb;

import escom.libreria.info.suscripciones.SuscripcionElectronica;
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
public class SuscripcionElectronicaFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(SuscripcionElectronica suscripcionElectronica) {
        em.persist(suscripcionElectronica);
    }

    public void edit(SuscripcionElectronica suscripcionElectronica) {
        em.merge(suscripcionElectronica);
    }

    public void remove(SuscripcionElectronica suscripcionElectronica) {
        em.remove(em.merge(suscripcionElectronica));
    }

    public SuscripcionElectronica find(Object id) {
        return em.find(SuscripcionElectronica.class, id);
    }

    public List<SuscripcionElectronica> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SuscripcionElectronica.class));
        return em.createQuery(cq).getResultList();
    }

    public List<SuscripcionElectronica> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SuscripcionElectronica.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SuscripcionElectronica> rt = cq.from(SuscripcionElectronica.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SuscripcionElectronica> getSuscripcionesElectronicasIdCliente(String id) {
        List<SuscripcionElectronica> l=null;
        try{
            TypedQuery<SuscripcionElectronica> query=em.createQuery("SELECT s FROM SuscripcionElectronica s WHERE s.suscripcionElectronicaPK.idCliente = :idCliente",SuscripcionElectronica.class)
            .setParameter("idCliente", id);
            l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }

}
