/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.temacliente.ejb;

import com.escom.info.preferenciaCliente.jpa.TemaCliente;
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
public class TemaClienteFacade {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    public void create(TemaCliente temaCliente) {
        em.persist(temaCliente);
    }

    public void edit(TemaCliente temaCliente) {
        em.merge(temaCliente);
    }

    public void remove(TemaCliente temaCliente) {
        em.remove(em.merge(temaCliente));
    }

    public TemaCliente find(Object id) {
        return em.find(TemaCliente.class, id);
    }

    public List<TemaCliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TemaCliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<TemaCliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TemaCliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<TemaCliente> rt = cq.from(TemaCliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }


    public List<TemaCliente> getListaTemaCliente(String idCliente){
        List<TemaCliente> l=null;
        try{
        TypedQuery<TemaCliente> query=em.createQuery("SELECT DISTINCT t FROM TemaCliente t WHERE t.idCliente = :idCliente",TemaCliente.class)
                .setParameter("idCliente", idCliente);

        l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }
     public List<TemaCliente> getListALL(){
        List<TemaCliente> l=null;
        try{
        TypedQuery<TemaCliente> query=em.createQuery("SELECT DISTINCT t FROM TemaCliente t ",TemaCliente.class);


        l=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return l;
    }

    public boolean buscarTemaCliente(String codigoTitulo, String cliente) {
        TemaCliente t=null;
         try{
        TypedQuery<TemaCliente> query=em.createQuery("SELECT DISTINCT t FROM TemaCliente t WHERE t.idCliente:cliente AND t.idArticulo:articulo ",TemaCliente.class)
             .setParameter("articulo",codigoTitulo)
             .setParameter("cliente",cliente)
             .setMaxResults(1);


        t=query.getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
        }

        return t==null?false:true;


    }
}
