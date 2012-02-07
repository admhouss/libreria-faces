/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras.ejb;

import escom.libreria.info.compras.EnvioFisico;
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
public class EnvioFisicoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(EnvioFisico envioFisico) {
        em.persist(envioFisico);
    }

    public void edit(EnvioFisico envioFisico) {
        em.merge(envioFisico);
    }

    public void remove(EnvioFisico envioFisico) {
        em.remove(em.merge(envioFisico));
    }

    public EnvioFisico find(Object id) {
        return em.find(EnvioFisico.class, id);
    }

    public List<EnvioFisico> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EnvioFisico.class));
        return em.createQuery(cq).getResultList();
    }

    public List<EnvioFisico> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EnvioFisico.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<EnvioFisico> rt = cq.from(EnvioFisico.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<EnvioFisico> getLisEnvioFisicoIdCliente(String id) {

        List<EnvioFisico> l=null;
        try{
        TypedQuery<EnvioFisico> query=em.createQuery("SELECT e FROM EnvioFisico e WHERE e.pedido.cliente.id = :idCliente", EnvioFisico.class)
                  .setParameter("idCliente", id);
        l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }

        return l;

    }

}
