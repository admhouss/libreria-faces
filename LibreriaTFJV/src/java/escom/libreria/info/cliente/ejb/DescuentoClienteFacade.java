/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.ejb;

import escom.libreria.info.cliente.jpa.DescuentoCliente;
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
public class DescuentoClienteFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(DescuentoCliente descuentoCliente) {
        em.persist(descuentoCliente);
    }

    public void edit(DescuentoCliente descuentoCliente) {
        em.merge(descuentoCliente);
    }

    public void remove(DescuentoCliente descuentoCliente) {
        em.remove(em.merge(descuentoCliente));
    }

    public DescuentoCliente find(Object id) {
        return em.find(DescuentoCliente.class, id);
    }

    public List<DescuentoCliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DescuentoCliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<DescuentoCliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DescuentoCliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<DescuentoCliente> rt = cq.from(DescuentoCliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public DescuentoCliente buscarDescuentoCliente(String id, Integer id0) {
        DescuentoCliente descuento=null;
        try{
        TypedQuery<DescuentoCliente> query=em.createQuery("SELECT d FROM DescuentoCliente d WHERE  d.descuentoClientePK.idCliente=:cliente AND d.descuentoClientePK.idDescuento=:descuento ",DescuentoCliente.class)
        .setParameter("cliente", id)
        .setParameter("descuento", id0).setMaxResults(1);
        descuento=query.getSingleResult();
        }catch(Exception e){}
        return descuento;
    }

    public BigDecimal obtenerMaxioDescuento(String correo) {
        BigDecimal max=null;
        Date fechaActual=new Date();
        try{
        TypedQuery<BigDecimal> query=em.createQuery("SELECT MAX(d.descuento.porcentaje) FROM DescuentoCliente d WHERE  (d.cliente.id=:cliente  AND d.fechaInicio<=d.fechaFin AND d.fechaFin>=:fa)",BigDecimal.class)
        .setParameter("cliente", correo)
        .setParameter("fa", fechaActual,TemporalType.TIMESTAMP);

        max= query.getSingleResult();
        }catch(Exception e){e.printStackTrace();}
        if(max==null)
        max=BigDecimal.ZERO;
        return max;

    }

    //public  Big

}
