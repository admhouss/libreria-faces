/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
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
public class ArticuloFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Articulo articulo) {
        em.persist(articulo);
    }

    public void edit(Articulo articulo) {

        em.merge(articulo);
    }

    public void remove(Articulo articulo) {
        em.remove(em.merge(articulo));
    }

    public Articulo find(Object id) {
        return em.find(Articulo.class, id);
    }

    public List<Articulo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Articulo.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Articulo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Articulo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Articulo> rt = cq.from(Articulo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Articulo> buscarLibro(String titulo, String autor, String editorial, String resumen,String anio) {
              //TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.idIdc.editorial like :editorial and a.idIdc.anio like :anio",Articulo.class)
              TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.idIdc.editorial =:editorial or a.idIdc.anio =:anio",Articulo.class)

             //.setParameter("titulo", resumen)

            // .setParameter("autor",autor )
              .setParameter("editorial", editorial)
              //.setParameter("resuemn", resumen)
              .setParameter("anio", anio);

              List<Articulo> l=query.getResultList();
             
            
              return l;
    }

    public List<Articulo> buscarLibroByCategoria(String categoria) {
         TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.idTipo.descripcion =:categoria",Articulo.class)
         .setParameter("categoria", categoria);
            
          List<Articulo> l=query.getResultList();


              return l;
    }

    

}
