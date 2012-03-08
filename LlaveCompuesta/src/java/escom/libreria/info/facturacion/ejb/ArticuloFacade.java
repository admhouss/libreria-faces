/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.facturacion.ejb;


import escom.libreria.info.articulo.Impuesto;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.info.proveedor.ProveedorArticulo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
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
    @PersistenceContext(unitName = "LlaveCompuestaPU")
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
          
              editorial="%"+editorial+"%";
               //System.out.println("Editoria"+editorial);
              TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.publicacion.editorial LIKE :editorial OR a.publicacion.anio LIKE :anio ",Articulo.class)
              .setParameter("editorial", editorial)
              .setParameter("anio", anio+"%");
              List<Articulo> l=query.getResultList();
             
            
              return l;
    }

    public List<String> buscarAutor(String autor) {

        TypedQuery<String> query=em.createQuery("SELECT DISTINCT a.creador FROM Articulo a WHERE a.creador LIKE :autor ORDER BY a.creador ASC",String.class)
        .setParameter("autor","%"+autor+"%");
        List<String> l= query.getResultList();
        return l;
    }

    public void borrarProveedorArticulo(Integer proveedorID,Integer articuloID) {
        System.out.println("proveedor"+proveedorID+"articulo"+articuloID);
         TypedQuery<ProveedorArticulo> query=em.createQuery("DELETE FROM ProveedorArticulo p WHERE p.proveedorArticuloPK.idArticulo = :idArticulo AND p.proveedorArticuloPK.idProveedor =:idProveedor",ProveedorArticulo.class)
        .setParameter("idArticulo",articuloID)
        .setParameter("idProveedor", proveedorID);
         query.executeUpdate();
         
    }

    public List<String> getAsuntoArticulos(){

        List<String> asuntoList=null;
        try{
                TypedQuery<String> query=em.createQuery( "SELECT DISTINCT LOWER(a.asunto) FROM Articulo a  ",String.class);
                asuntoList=query.getResultList();
        }catch(Exception e){
e.printStackTrace();
        }
        return asuntoList;
    }

    public List<Articulo> getAsuntoArticulos(String asunto){

        List<Articulo> asuntoList=null;
        try{
                TypedQuery<Articulo> query=em.createQuery( "SELECT a FROM Articulo a WHERE a.asunto LIKE :asunto ",Articulo.class)
                .setParameter("asunto", "%"+asunto+"%");

                asuntoList=query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return asuntoList;
    }


    public   Object[][] getCostoArticulo(Integer idArticulo,String idCliente,Date fecha) throws SQLException{

        Object[][] datos;
        ResultSet resultado;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Crear el objeto de conexion a la base de datos
            //Crear el objeto de conexion a la base de datos
            Connection conexion = null;
            CallableStatement proc = null;
            try {
                conexion = DriverManager.getConnection("jdbc:mysql://localhost/libreriademo", "root", "root");
                System.out.println("libreriademo demo conectado");
                proc=conexion.prepareCall("{ call sp1(?, ?, ?) }");
                proc.setInt(1, idArticulo);
                proc.setString(2, idCliente);
                proc.setDate(3,new java.sql.Date( fecha.getTime()));
                if(proc.execute()){
                 resultado=proc.getResultSet();
                 while(resultado.next()){
                      System.out.println(resultado.getBigDecimal(0));
                 }
                }





            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("no me connecte a la base");
                Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
                conexion.close();
            }
            //Crear objeto Statement para realizar queries a la base de datos
            //Crear objeto Statement para realizar queries a la base de datos
           
            




        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Object[]>  getCostoArticulo(int suscripcion) {

               

                List<Object[]>  resultado=null;
                TypedQuery<Object[]> query=em.createQuery("SELECT a.costo ,SUM(a.costo*i.montoImpuesto) ,SUM(a.costo*i.montoImpuesto) + a.costo FROM Articulo a  WHERE a.id=:idArticulo",Object[].class)
                .setParameter("idArticulo", suscripcion);

                resultado= query.getResultList();

                return resultado;

    }



    public List<Articulo> getPublicaciones() {
        List<Articulo> articulos=null;
        try{
             TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE ( a.tipoArticulo.descripcion LIKE :uno OR a.tipoArticulo.descripcion LIKE :dos OR a.tipoArticulo.descripcion LIKE :tres OR a.tipoArticulo.descripcion LIKE :cuatro ) ORDER BY a.titulo ASC",Articulo.class)
              .setParameter("uno", "%LIBRO%")
              .setParameter("dos", "%CD%")
              .setParameter("tres", "%DVD%")
              .setParameter("cuatro", "%REVISTA%");
               articulos=query.getResultList();

        }catch(Exception e){
              System.out.println("FACADE ARTICULO TIPO_ARTICULO PUBLICACION FALLO");
        }
         return articulos;
           

    }

    public List<Articulo> getSuscripcion() {
        List<Articulo> articulos=null;
        try{
             TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE ( a.tipoArticulo.descripcion LIKE :uno ) ORDER BY a.titulo ASC",Articulo.class)
              .setParameter("uno", "%SUSCRIPCION%");

               articulos=query.getResultList();

        }catch(Exception e){

        }
         return articulos;
    }

    public List<Articulo> getArticuloMayordescuento() {
       List<Articulo> l=null;
         try{
        TypedQuery<Articulo> query=em.createQuery("SELECT a FROM Articulo a WHERE a.descuentoArticulo.descuento=(SELECT MAX(a2.descuentoArticulo.descuento) FROM Articulo a2)", Articulo.class);
        l=query.getResultList();
        }catch(Exception e){
          e.printStackTrace();
        }
         return l;
    }

    public List<Articulo> getReporteArticulo(List<Integer> idArticulo) {
        List<Articulo> l=null;

          TypedQuery<Articulo> queryt=em.createQuery("SELECT a FROM Articulo a  WHERE a.id IN (:idArticulos)",Articulo.class)
               // .setParameter("idProveedor", idProveedor)
                .setParameter("idArticulos",idArticulo);

                //.setParameter("idPromocion",idPromocion);

            l=queryt.getResultList();
            return l;


    }




   

    

   

    

}
