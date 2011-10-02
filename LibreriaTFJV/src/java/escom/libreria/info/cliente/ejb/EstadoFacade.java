package escom.libreria.info.cliente.ejb;



import escom.libreria.info.cliente.jpa.Estado;
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
public class EstadoFacade  {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Estado estado) {
        em.persist(estado);
    }

    public void edit(Estado estado) {
        em.merge(estado);
    }

    public void remove(Estado estado) {
        em.remove(em.merge(estado));
    }

    public Estado find(Object id) {
        return em.find(Estado.class, id);
    }

    public List<Estado> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Estado.class));

        return em.createQuery(cq).getResultList();
    }

    public List<Estado> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Estado.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Estado> rt = cq.from(Estado.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Estado> getListaEstados(){
       TypedQuery<Estado> q=em.createQuery("SELECT e FROM Estado e ORDER BY e.nombre",Estado.class);
       List<Estado> estados=q.getResultList();
       return estados;
    }

  

}
