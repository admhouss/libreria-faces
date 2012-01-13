/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras.ejb;

import escom.libreria.info.compras.Compra;
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
public class CompraFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Compra compra) {
        em.persist(compra);
    }

    public void edit(Compra compra) {
        em.merge(compra);
    }

    public void remove(Compra compra) {
        em.remove(em.merge(compra));
    }

    public Compra find(Object id) {
        return em.find(Compra.class, id);
    }

    public List<Compra> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Compra.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Compra> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Compra.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Compra> rt = cq.from(Compra.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Compra> getComprasByCliente(String idCliente) {
        List<Compra> l=null;
        try{
        TypedQuery<Compra> query=em.createQuery("SELECT c FROM Compra c WHERE c.idCliente = :idCliente ORDER BY c.idPedido DESC",Compra.class)
                .setParameter("idCliente", idCliente);
                

        l= query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }

     public void cambiarEstadoCompra(int idPedido,String type) { //type CANCELADO,COMPRADO,PROCESANDO
        Query query = em.createQuery("UPDATE Compra c SET c.estado=:estado WHERE c.idPedido=:idPedido",Compra.class)
        .setParameter("idPedido",idPedido)
        .setParameter("estado", type);

        int deleted = query.executeUpdate();
    }

}
