/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.compras.ejb;

import escom.libreria.info.compras.Zona;
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
public class ZonaFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(Zona zona) {
        em.persist(zona);
    }

    public void edit(Zona zona) {
        em.merge(zona);
    }

    public void remove(Zona zona) {
        em.remove(em.merge(zona));
    }

    public Zona find(Object id) {
        return em.find(Zona.class, id);
    }

    public List<Zona> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Zona.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Zona> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Zona.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Zona> rt = cq.from(Zona.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public BigDecimal  getTarifaByPeso(String idZona, int pesoEntero,BigDecimal PesoOriginal) {
        BigDecimal tarifa=null;
       try{
            TypedQuery<BigDecimal>  query=em.createQuery("SELECT z.tarifa FROM Zona z WHERE ( z.idZona=:idZona AND z.peso BETWEEN :peso1 AND :peso2 )",BigDecimal.class)
            .setParameter("idZona", idZona)
            .setParameter("peso1",pesoEntero)
            .setParameter("peso2", PesoOriginal)
            .setMaxResults(1);
            tarifa=query.getSingleResult();
        }catch(Exception e){
           System.out.println("NO EXISTEN ZONA PARA EXTE ");
        }
       return tarifa;
    }

}
