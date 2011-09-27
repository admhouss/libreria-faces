/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.info.articulo.jpa.Articulo;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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



              TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.idIdc.editorial LIKE :editorial or a.idIdc.anio =:anio",Articulo.class)

             //.setParameter("titulo", resumen)

            // .setParameter("autor",autor )

              .setParameter("editorial", editorial+"%")
              //.setParameter("resuemn", resumen)
              .setParameter("anio", anio);

              List<Articulo> l=query.getResultList();
             
            
              return l;
    }
    private String catego;

    public String getCategoria(int opc) {
        String categoria="";
         switch(opc){
                    case 1: categoria="Derecho Fiscal"; break;
                    case 2:categoria="Derecho Internacional"; break;
                    case 3:categoria="Derecho Administrativo"; break;
                    case 4:categoria="Juicio en  Linea"; break;
                    case 5:categoria="Juicio Orales"; break;
                    case 6:categoria="Jurisprudencia"; break;
                    default:
                        categoria = "EN LINEA";
                }
        return categoria;
    }

    public void setCatego(String catego) {
        this.catego = catego;
    }

    public List<Articulo> buscarLibroByCategoria(int opc) {
        
        String categoria=getCategoria(opc)+"%";
              
               
         TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.idTipo.descripcion LIKE :categoria",Articulo.class)
         .setParameter("categoria", categoria);
            
          List<Articulo> l=query.getResultList();
          //System.out.println("cuantos ay"+l.size());


              return l;
    }

    public List<Articulo> buscarNovedades() {
          Date date=new Date();

          //String anio="211"+"-%-%";
      

         TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a ",Articulo.class);
         
          List<Articulo> l=query.getResultList();
          return l;
    }

    

}
