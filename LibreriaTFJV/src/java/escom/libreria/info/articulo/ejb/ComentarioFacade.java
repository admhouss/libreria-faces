/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
import escom.libreria.info.articulo.jpa.Comentario;
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
public class ComentarioFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Comentario comentario) {
        em.persist(comentario);
    }

    public void edit(Comentario comentario) {
        em.merge(comentario);
    }

    public void remove(Comentario comentario) {
        em.remove(em.merge(comentario));
    }

    public Comentario find(Object id) {
        return em.find(Comentario.class, id);
    }

    public List<Comentario> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Comentario.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Comentario> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Comentario.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Comentario> rt = cq.from(Comentario.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Comentario> getComentariosByArticulo(Articulo articulo) {
         List<Comentario> l=null;
         try{
            TypedQuery<Comentario> query=em.createQuery("SELECT c FROM Comentario c WHERE c.articulo.id =:id ORDER BY c.autor DESC",Comentario.class)
            .setParameter("id", articulo.getId());
            l=query.getResultList();
        }catch(Exception e){e.printStackTrace();}
        return l;
    }

}
