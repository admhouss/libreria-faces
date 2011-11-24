/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.comun.ValidarNumero;
import escom.libreria.info.articulo.jpa.Publicacion;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class PublicacionFacade {
    @PersistenceContext(unitName = "LibreriaTFJVPU")
    private EntityManager em;

    public void create(Publicacion publicacion) {
        em.persist(publicacion);
    }

    public void edit(Publicacion publicacion) {
        em.merge(publicacion);
    }

    public void remove(Publicacion publicacion) {
        em.remove(em.merge(publicacion));
    }

    public Publicacion find(Object id) {
        return em.find(Publicacion.class, id);
    }

    public List<Publicacion> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Publicacion.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Publicacion> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Publicacion.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Publicacion> rt = cq.from(Publicacion.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

       

    public String getCategoria(int opc) {
        String categoria="";
         switch(opc){
                    case 1: categoria="Derecho Fiscal"; break;
                    case 2: categoria="Derecho Internacional"; break;
                    case 3: categoria="Derecho Administrativo"; break;
                    case 4: categoria="Juicio en  Linea"; break;
                    case 5: categoria="Juicio Orales"; break;
                    case 6: categoria="Jurisprudencia"; break;
                    default:
                     categoria = "Derecho Fiscal";
                }
        return categoria;
    }
    public List<Publicacion> buscarLibroByCategoria(String categoria) {
         TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p WHERE p.articulo.asunto LIKE :categoria ORDER BY p.articulo.titulo ASC",Publicacion.class)
         .setParameter("categoria","%"+categoria+"%");
         List<Publicacion> l=query.getResultList();
          return l.isEmpty()?null:l;
    }

     public List<Publicacion> buscarAccesorio() {
         TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p WHERE NOT(p.articulo.tipoArticulo.descripcion =:libro OR  p.articulo.tipoArticulo.descripcion =:libros ) ORDER BY p.articulo.titulo ASC",Publicacion.class)
         .setParameter("libro","libro")
         .setParameter("libros", "libros");
          List<Publicacion> l=query.getResultList();
          return l;
    }


 private String queryTemporal=null;
 private int convertirNumero=0;

    public List<Publicacion> buscarDinamica(String campo,Object dinamico){

        ValidarNumero validarNumero=new ValidarNumero();
     try{
            queryTemporal="SELECT p FROM Publicacion p WHERE "+campo+" LIKE :general  ORDER BY p.articulo.titulo ASC";
            
            TypedQuery<Publicacion> query=em.createQuery(queryTemporal,Publicacion.class);
            if(campo.equals("p.numero") || campo.equals("p.issn")){
                    if(validarNumero.validarNumero(dinamico.toString().trim())){
                        String numero=dinamico.toString().trim();
                        convertirNumero=Integer.parseInt(numero);
                     }
                     queryTemporal="SELECT p FROM Publicacion p WHERE "+campo+" =:general  ORDER BY p.articulo.titulo ASC";
                     query=em.createQuery(queryTemporal,Publicacion.class);
                     query.setParameter("general",convertirNumero);//y

             }else{
              query.setParameter("general","%" + (String)dinamico+ "%");//y
         }
                List<Publicacion>l=query.getResultList();return l;
        }catch(Exception e){e.printStackTrace();}
    return null;

    }

     public List<String> buscarDinamica(String campo){

     try{
            queryTemporal="SELECT "+campo+" FROM Publicacion p  ORDER BY p.articulo.titulo ASC";
            TypedQuery<String> query=em.createQuery(queryTemporal,String.class);
            List<String>l=query.getResultList();
            return l;
        }catch(Exception e){e.printStackTrace();}
         return null;
    }



    public List<Publicacion> buscarDinamicaPeriodo(String campo,Date dinamico){

     try{
             queryTemporal="SELECT p FROM Publicacion p WHERE "+campo+" =:general  ORDER BY p.articulo.titulo ASC";
             TypedQuery<Publicacion> query=em.createQuery(queryTemporal,Publicacion.class);
             query.setParameter("general",dinamico,TemporalType.TIMESTAMP);//y
             List<Publicacion>l=query.getResultList();return l;
        }catch(Exception e){}
    return null;

    }





    public List<Publicacion> buscarArticuloNovedades() {

       /* Date fechaActual=new Date();
        GregorianCalendar calendario=new GregorianCalendar(fechaActual.getYear(), fechaActual.getMonth(),1);
        Date fechaInicial=calendario.getTime();
//WHERE (p.articulo.fechaRegistro >=:fi AND p.articulo.fechaRegistro<=:fa )
        TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p  ORDER BY p.articulo.titulo ASC",Publicacion.class)
        .setParameter("fa", fechaActual,TemporalType.DATE)
        .setParameter("fi",fechaInicial,TemporalType.DATE);
        List<Publicacion>l=query.getResultList();*/

        //em.
        return null;
    }

    public List<Publicacion> buscarArticulo(String autor, String titulo, String tipoArticulo, Date periodo, int numero, int iSSN, String iSBN, String editorial,String asunto) {


                    TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p WHERE  (p.articulo.titulo LIKE :titulo AND  p.articulo.tipoArticulo.descripcion LIKE :tipo AND p.articulo.creador LIKE :autor AND p.articulo.asunto LIKE :asunto ) OR p.editorial LIKE :editorial OR p.periodoMes =:periodo  OR p.numero =:numero OR p.issn=:ISSN OR p.isbn LIKE :ISBN  ORDER BY p.articulo.titulo ASC",Publicacion.class)

                    .setParameter("autor","%"+autor+"%")//y
                    .setParameter("asunto","%"+asunto+"%")//yes
                    .setParameter("titulo","%"+titulo+"%")//yes
                    .setParameter("tipo","%"+ tipoArticulo+"%") //yes
                    .setParameter("periodo",periodo,TemporalType.TIMESTAMP)//yes
                    .setParameter("numero", numero)//yes
                    .setParameter("ISSN", iSSN)//Yes
                    .setParameter("ISBN", "%"+iSBN+"%")//Yes
                    .setParameter("editorial","%"+editorial+"%");//yes
                    List<Publicacion>l=query.getResultList();
                    return l;
    }

    public List<Publicacion> buscarArticuloDinamico(String autor, String titulo, String tipoArticulo, Date periodo, String numero, String iSSN, String iSBN, String editorial,String asunto,String querySQL) {

      int numeroEntero=0,numeroISSN=0;
      ValidarNumero validarNumero=new ValidarNumero();
                    TypedQuery<Publicacion> query=em.createQuery(querySQL,Publicacion.class);
                    if(!numero.trim().equals("")){
                        if(validarNumero.validarNumero(numero)){
                                   numeroEntero=Integer.parseInt(numero);
                                   query.setParameter("numero", numeroEntero);//yes
                        }else{
                             query.setParameter("numero", numeroEntero);//yes
                        }
                     }
                    if(!iSSN.trim().equals("")){
                         if(validarNumero.validarNumero(iSSN)){
                                  numeroISSN=Integer.parseInt(iSSN);
                                  query.setParameter("ISSN", numeroISSN);//Yes
                        }else{
                             query.setParameter("ISSN",numeroISSN);//yes
                        }
                        
                       
                     }

                    if(!autor.trim().equals(""))
                    query.setParameter("autor","%"+autor+"%");//y
                    if(!asunto.trim().equals(""))
                    query.setParameter("asunto","%"+asunto+"%");//yes
                    if(!titulo.trim().equals(""))
                    query.setParameter("titulo","%"+titulo+"%");//yes
                    if(!tipoArticulo.trim().equals(""))
                    query.setParameter("tipo","%"+ tipoArticulo+"%"); //yes
                    if(periodo!=null)
                    query.setParameter("periodo",periodo,TemporalType.TIMESTAMP);//yes

                    if(!iSBN.trim().equals(""))
                    query.setParameter("ISBN", "%"+iSBN+"%");//Yes
                    if(!editorial.trim().equals(""))
                    query.setParameter("editorial","%"+editorial+"%");//yes
                    List<Publicacion>l=query.getResultList();
                    return l;
    }

    public List<Publicacion> getListLibros() {

                    TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p WHERE p.articulo.tipoArticulo.descripcion LIKE :tipo  ORDER BY p.articulo.titulo ASC",Publicacion.class)
                    .setParameter("tipo","%libro%"); //yes
                    List<Publicacion>l=query.getResultList();
                    return l;
    }





    }

    

    


