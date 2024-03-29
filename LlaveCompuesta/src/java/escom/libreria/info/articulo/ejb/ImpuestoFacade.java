/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.Impuesto;
import escom.libreria.info.facturacion.Articulo;
import java.math.BigDecimal;
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
public class ImpuestoFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Impuesto impuesto) {
        em.persist(impuesto);
    }

    public void edit(Impuesto impuesto) {
        em.merge(impuesto);
    }

    public void remove(Impuesto impuesto) {
        em.remove(em.merge(impuesto));
    }

    public Impuesto find(Object id) {
        return em.find(Impuesto.class, id);
    }

    public List<Impuesto> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Impuesto.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Impuesto> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Impuesto.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Impuesto> rt = cq.from(Impuesto.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Impuesto> buscarImpuestoByarticulo(Articulo p) {
        TypedQuery<Impuesto> q=em.createQuery("SELECT i FROM Impuesto  i WHERE i.articulo=:id ",Impuesto.class).setParameter("id", p);
        List<Impuesto> l=q.getResultList();
        return l;
    }

    public BigDecimal getImpuestoTotalArticulo(int idArticulo) {
        BigDecimal impuestoTOTOAL=null;
        try{
        TypedQuery<BigDecimal> q=em.createQuery("SELECT SUM(i.montoImpuesto) FROM Impuesto  i WHERE i.articulo.id=:id ",BigDecimal.class)
        .setParameter("id", idArticulo);
          impuestoTOTOAL=q.getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(impuestoTOTOAL==null){
            impuestoTOTOAL=BigDecimal.ZERO;
        }
        return impuestoTOTOAL;
    }

}
