/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.Promocion;
import escom.libreria.info.facturacion.Articulo;
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
public class PromocionFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Promocion promocion) {
        em.persist(promocion);
    }

    public void edit(Promocion promocion) {
        em.merge(promocion);
    }

    public void remove(Promocion promocion) {
        em.remove(em.merge(promocion));
    }

    public Promocion find(Object id) {
        return em.find(Promocion.class, id);
    }

    public List<Promocion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Promocion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Promocion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Promocion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Promocion> rt = cq.from(Promocion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Integer> getIndicesPromocion() {
        List<Integer> l=null;
        try{
        TypedQuery<Integer> query=em.createQuery("SELECT DISTINCT p.promocionPK.id FROM Promocion p ORDER BY p.promocionPK.id ASC ",Integer.class);
        l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }

    public List<Promocion> buscarPromocionByIndicie(Integer codigoPromocion) {
         List<Promocion> l=null;
        try{
        TypedQuery<Promocion> query=em.createQuery("SELECT p FROM Promocion p WHERE p.promocionPK.id=:idPromocion ",Promocion.class)
         .setParameter("idPromocion", codigoPromocion);

         l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
         return l;
    }

    public List<Promocion> getOfertaDelDia() {


//SELECT * FROM `libreriademo`.`promocion` WHERE 01 =MONTH(DIA_INICIO) AND 2012=YEAR(DIA_FIN) HAVING MAX(PRECIO_PUBLICO) ;
        Date fecha=new Date();
        List<Promocion> l=null;
        try{
        TypedQuery<Promocion> query=em.createQuery("SELECT p FROM Promocion p WHERE :fecha BETWEEN p.diaInicio AND p.diaFin",Promocion.class)
               .setParameter("fecha",fecha );
               //.setParameter("anio", anio);
        l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }


         return l;
       
    }

    public BigDecimal getPromocionArticulo(Articulo articulo) {

         BigDecimal promocionArticulo=null;
         Date fecha= new Date();
         try{
                 TypedQuery<BigDecimal> query=em.createQuery("SELECT p.precioPublico FROM Promocion p WHERE ( :fecha >= p.diaInicio AND  :fecha<=p.diaFin ) AND  ( p.articulo.id=:idArticulo ) ORDER BY p.diaFin", BigDecimal.class)
                .setParameter("idArticulo", articulo.getId())
                .setParameter("fecha", fecha ,TemporalType.DATE)
                .setMaxResults(1);
             promocionArticulo= query.getSingleResult();

        }catch(Exception e){
            
        }
         if(promocionArticulo==null){
            promocionArticulo=BigDecimal.ZERO ;
         }

        return promocionArticulo;
    }


}
