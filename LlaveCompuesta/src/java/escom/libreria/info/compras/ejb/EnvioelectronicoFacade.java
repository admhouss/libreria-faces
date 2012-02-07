/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras.ejb;

import escom.libreria.info.compras.Envioelectronico;
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
public class EnvioelectronicoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Envioelectronico envioelectronico) {
        em.persist(envioelectronico);
    }

    public void edit(Envioelectronico envioelectronico) {
        em.merge(envioelectronico);
    }

    public void remove(Envioelectronico envioelectronico) {
        em.remove(em.merge(envioelectronico));
    }

    public Envioelectronico find(Object id) {
        return em.find(Envioelectronico.class, id);
    }

    public List<Envioelectronico> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Envioelectronico.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Envioelectronico> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Envioelectronico.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Envioelectronico> rt = cq.from(Envioelectronico.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Envioelectronico> getEnvioElectronicoByIdCliente(String id) {
        List<Envioelectronico> l=null;
        try{
        TypedQuery<Envioelectronico> query=em.createQuery("SELECT e FROM Envioelectronico e WHERE e.pedido.cliente.id=:idCliente",Envioelectronico.class)
             .setParameter("idCliente", id);

        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }

}
