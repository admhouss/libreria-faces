/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.descuentos.ejb;

import escom.libreria.info.descuentos.DescuentoArticulo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author xxx
 */
@Stateless
public class DescuentoArticuloFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(DescuentoArticulo descuentoArticulo) {
        em.persist(descuentoArticulo);
    }

    public void edit(DescuentoArticulo descuentoArticulo) {
        em.merge(descuentoArticulo);
    }

    public void remove(DescuentoArticulo descuentoArticulo) {
        em.remove(em.merge(descuentoArticulo));
    }

    public DescuentoArticulo find(Object id) {
        return em.find(DescuentoArticulo.class, id);
    }

    public List<DescuentoArticulo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DescuentoArticulo.class));
        return em.createQuery(cq).getResultList();
    }

    public List<DescuentoArticulo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DescuentoArticulo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<DescuentoArticulo> rt = cq.from(DescuentoArticulo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public BigDecimal getDescuentoValidoArticulo(int idArticulo) {
         BigDecimal descuentoTOTAL=BigDecimal.ZERO;
        try{DescuentoArticulo d;
        TypedQuery<BigDecimal> l=em.createQuery("SELECT MAX(d.descuento) FROM DescuentoArticulo d WHERE ( d.fechaInicio <= d.fechaFinal  AND  d.fechaFinal >=:fechaActual ) AND d.articulo.id=:idArticulo ",BigDecimal.class)
        .setParameter("idArticulo", idArticulo)
        .setParameter("fechaActual", new Date(), TemporalType.TIMESTAMP);

        descuentoTOTAL=l.getSingleResult();
        if(descuentoTOTAL==null)
            return BigDecimal.ZERO;
        }catch(Exception e){e.printStackTrace();}
         return descuentoTOTAL;

    }

    public List<DescuentoArticulo> getDescuentoValidoListArticulo(int idArticulo) {
         List<DescuentoArticulo> descuentoArticulos=null;
        try{DescuentoArticulo d;

         TypedQuery<DescuentoArticulo> l=em.createQuery("SELECT d FROM DescuentoArticulo d WHERE ( d.fechaInicio <= d.fechaFinal  AND  d.fechaFinal >=:fechaActual ) AND d.articulo.id=:idArticulo ",DescuentoArticulo.class)
        .setParameter("idArticulo", idArticulo)
        .setParameter("fechaActual", new Date(), TemporalType.TIMESTAMP);
         descuentoArticulos=l.getResultList();
        }catch(Exception e){
        }
         return descuentoArticulos;


    }


}
