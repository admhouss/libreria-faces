/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.cliente.ejb;

import escom.libreria.info.cliente.jpa.Cliente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
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
public class ClienteFacade  {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Cliente cliente) {
        em.persist(cliente);
    }

    public void edit(Cliente cliente) {
        em.merge(cliente);
    }

    public void remove(Cliente cliente) {
        em.remove(em.merge(cliente));
    }

    public Cliente find(Object id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Cliente.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Cliente> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Cliente.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Cliente> rt = cq.from(Cliente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Cliente> getListClientesActive() {
        TypedQuery<Cliente> l=em.createQuery("SELECT c FROM Cliente c WHERE c.estatus=true AND c.recibeInfor=1 ORDER BY c.nombre,c.paterno,c.materno ASC",Cliente.class);
        List<Cliente> c= l.getResultList();
        return c;
    }
  private Cliente cliente;
    public Cliente buscarUsuario(String usuario, String password) {
        cliente=null;
        try{
            TypedQuery<Cliente> l=em.createQuery("SELECT c FROM Cliente c WHERE c.id=:correo AND c.password=:password",Cliente.class)
            .setParameter("correo",usuario)
            .setParameter("password", password).setMaxResults(1);
             cliente=l.getSingleResult();
        }catch(Exception e){}
         return cliente;


    }
     public Cliente buscarUsuario(String correo) {
        cliente=null;
        try{
            TypedQuery<Cliente> l=em.createQuery("SELECT c FROM Cliente c WHERE c.id=:correo",Cliente.class)
            .setParameter("correo",correo)
            .setMaxResults(1);
             cliente=l.getSingleResult();
        }catch(Exception e){}
         return cliente;
    }


      public List<Cliente> buscarCliente(String query,String correo,String nombre) {
        List<Cliente> cliente=null;

       //System.out.println("SELECT c FROM Cliente c WHERE "+query+" ORDER BY c.nombre ASC");
        try{
            TypedQuery<Cliente> l=em.createQuery("SELECT c FROM Cliente c WHERE "+query+" ORDER BY c.nombre ASC",Cliente.class);
            if(correo!=null && !correo.equals(""))
             l.setParameter("correo","%"+correo+"%");
            if(nombre!=null && !nombre.equals(""))
             l.setParameter("nombre", "%"+nombre+"%");
            cliente=l.getResultList();
        }catch(Exception e){}
         return cliente;
    }


}
