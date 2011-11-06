/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.contacto.ejb;

import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.contacto.jpa.Direnvio;
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
public class DirenvioFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Direnvio direnvio) {
        em.persist(direnvio);
    }

    public void edit(Direnvio direnvio) {
        em.merge(direnvio);
    }

    public void remove(Direnvio direnvio) {
        em.remove(em.merge(direnvio));
    }

    public Direnvio find(Object id) {
        return em.find(Direnvio.class, id);
    }

    public List<Direnvio> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Direnvio.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Direnvio> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Direnvio.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Direnvio> rt = cq.from(Direnvio.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Direnvio> getListDirEnvioByCliente(String correo) {
        TypedQuery<Direnvio> query=em.createQuery("SELECT d FROM Direnvio d WHERE d.idCliente.id=:correo", Direnvio.class).setParameter("correo", correo);
        List<Direnvio> l=query.getResultList();
        return l;
    }

}
