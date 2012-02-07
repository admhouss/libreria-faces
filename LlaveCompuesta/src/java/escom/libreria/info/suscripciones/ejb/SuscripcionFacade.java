/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.suscripciones.ejb;

import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.suscripciones.Suscripcion;
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
public class SuscripcionFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Suscripcion suscripcion) {
        em.persist(suscripcion);
    }

    public void edit(Suscripcion suscripcion) {
        em.merge(suscripcion);
    }

    public void remove(Suscripcion suscripcion) {
        em.remove(em.merge(suscripcion));
    }

    public Suscripcion find(Object id) {
        return em.find(Suscripcion.class, id);
    }

    public List<Suscripcion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Suscripcion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Suscripcion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Suscripcion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Suscripcion> rt = cq.from(Suscripcion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Suscripcion> getSuscripcionByID(int suscripcion) {
        List<Suscripcion> s=null;
        try{
        TypedQuery<Suscripcion> query=em.createQuery("SELECT s FROM Suscripcion s WHERE s.suscripcionPK.idSuscripcion = :idSuscripcion", Suscripcion.class)
                .setParameter("idSuscripcion", suscripcion);
        s=query.getResultList();


        }catch(Exception e){
           e.printStackTrace();
        }

        return s;

    }



    public List<Integer> getIdSuscripciones() {
         List<Integer> s=null;
        try{
        TypedQuery<Integer> query=em.createQuery("SELECT DISTINCT s.suscripcionPK.idSuscripcion FROM Suscripcion s ORDER BY s.suscripcionPK.idSuscripcion ASC", Integer.class);

        s=query.getResultList();


        }catch(Exception e){
           e.printStackTrace();
        }

        return s;
    }

    public List<Suscripcion> getSuscripcionesAgrupadas() {
        List<Suscripcion> s=null;
        try{
        TypedQuery<Suscripcion> query=em.createQuery("SELECT  s FROM Suscripcion s  GROUP BY s.suscripcionPK ", Suscripcion.class);

        s=query.getResultList();


        }catch(Exception e){
           e.printStackTrace();
        }

        return s;
    }

    public List<Articulo> getArticulosByID(int suscripcion) {
       List<Articulo> s=null;
        try{
            TypedQuery<Articulo> query=em.createQuery("SELECT  s.articulo FROM Suscripcion s WHERE s.suscripcionPK.idSuscripcion=:suscripcion", Articulo.class)
            .setParameter("suscripcion", suscripcion);
            s=query.getResultList();
        }catch(Exception e){
           e.printStackTrace();
        }
        return s;
    }

}
