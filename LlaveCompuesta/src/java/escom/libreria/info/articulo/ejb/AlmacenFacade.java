/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.Almacen;
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
public class AlmacenFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Almacen almacen) {
        em.persist(almacen);
    }

    public void edit(Almacen almacen) {
        em.merge(almacen);
    }

    public void remove(Almacen almacen) {
        em.remove(em.merge(almacen));
    }

    public Almacen find(Object id) {
        return em.find(Almacen.class, id);
    }

    public List<Almacen> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Almacen.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Almacen> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Almacen.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Almacen> rt = cq.from(Almacen.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Almacen> getArticulosExistenciaero() {

        List<Almacen> l=null;
        try{
                TypedQuery<Almacen> query=em.createQuery("SELECT a FROM Almacen a WHERE a.existencia=0", Almacen.class);
                l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
          return l;
    }

}
