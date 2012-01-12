/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.articulo.ejb;

import escom.libreria.comun.ValidarNumero;
import escom.libreria.info.articulo.Publicacion;

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
    @PersistenceContext(unitName = "LlaveCompuestaPU")
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
                    case 5: categoria="Juicios Orales"; break;
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
              query.setParameter("general","%" +(String)dinamico+ "%");//y
         }
                List<Publicacion>l=query.getResultList();return l;
        }catch(Exception e){e.printStackTrace();}
    return null;

    }

     public List<String> buscarDinamica(String campo){

     try{
       
            queryTemporal="SELECT DISTINCT "+campo+" FROM Publicacion p  ORDER BY p.articulo.titulo ASC";
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
        TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p WHERE  p.articulo.fechaRegistro <= p.articulo.fechaDisponibilidad  ORDER BY p.articulo.titulo ASC",Publicacion.class);
        List<Publicacion>l=query.getResultList();
        return l;
        
    }

    public List<Publicacion> buscarArticulo(String autor, String titulo, String tipoArticulo, int numero, int iSSN, String iSBN, String editorial,String asunto,
            String tomo,String unidad,String divisa,String formato,String publicador) {




                    TypedQuery<Publicacion> query=em.createQuery(
                     "SELECT p FROM Publicacion p WHERE   p.articulo.titulo LIKE :titulo  OR p.articulo.creador LIKE :autor  "
                     + "OR p.articulo.tipoArticulo.descripcion LIKE :tipo OR "
                     + "OR p.articulo.asunto LIKE :asunto  OR p.editorial LIKE :editorial "
                     + "OR p.numero =:numero OR p.issn=:ISSN OR p.isbn LIKE :ISBN  "
                     + "OR a.unidad LIKE unidad OR p.tomo LIKE tomo OR a.divisa LIKE divisa OR a.formato LIKE formato "
                     + "OR a.publicador LIKE publicador OR a.resumenRecurso LIKE resumenRecurso "
                     + "OR a.nombreAlternativo LIKE nombreAlternativo OR a.referenciaBibliograficas LIKE referenciab"
                     + "OR a.creador LIKE creador OR a.tabla_contenidos LIKE tablaContenidos "
                     + "OR a.codigo LIKE codigo"

                     + "ORDER BY p.articulo.titulo ASC",Publicacion.class)

                    .setParameter("autor","%"+autor+"%")//yes
                    .setParameter("asunto","%"+asunto+"%")//yes
                    .setParameter("titulo","%"+titulo+"%")//yes
                    .setParameter("tipo","%"+ tipoArticulo+"%") //yes
                    .setParameter("numero", numero)//yes
                    .setParameter("ISSN", iSSN)//Yes
                    .setParameter("ISBN", "%"+iSBN+"%")//Yes
                    .setParameter("editorial","%"+editorial+"%")//yes
                            
                            //-----------
                    .setParameter("tomo","p.tomo")  // OR p.tomo LIKE tomo yy

                    .setParameter("unidad","a.unidad") // OR a.unidad LIKE unidad yy
                    .setParameter("divisa","a.divisa") // OR a.divisa LIKE divisa yy

                    .setParameter("formato","a.formato") //OR a.formato LIKE formato nyy
                    .setParameter("publicador","a.publicacdor") // OR a.publicador LIKE publicador YY
                    .setParameter("resumen_recurso-a","") // OR a.resumenRecurso LIKE resumenRecurso YY
                    .setParameter("nombre_alternativoa","") // OR a.nombreAlternativo LIKE nombreAlternativo YY
                    .setParameter("referencias_bibliograficas-a","")// OR a.referenciaBibliograficas LIKE refb yy
                    .setParameter("creador-a","")                 // OR a.creador LIKE creador yy
                    .setParameter("tabla_contenidos-a","")         //OR a.tabla_contenidos LIKE tablaContenidos yy
                    .setParameter("codigo-a","");                     // OR a.codigo LIKE codigo




                    List<Publicacion>l=query.getResultList();
                    return l;
    }

    public List<Publicacion> buscarPublicaciones_Articulo(String cadena, int numero)  {




                    TypedQuery<Publicacion> query=em.createQuery(
                     "SELECT p FROM Publicacion p WHERE   p.articulo.titulo LIKE :titulo  OR p.articulo.creador LIKE :autor  "
                     + "OR p.articulo.tipoArticulo.descripcion LIKE :tipo "
                     + "OR p.articulo.asunto LIKE :asunto  OR p.editorial LIKE :editorial "
                     + "OR p.numero =:numero OR p.issn=:ISSN OR p.isbn LIKE :ISBN  "
                     + "OR p.articulo.unidad LIKE :unidad OR p.tomo LIKE :tomo OR p.articulo.divisa LIKE :divisa OR p.articulo.formato LIKE :formato "
                     + "OR p.articulo.publicador LIKE :publicador OR p.articulo.resumenRecurso LIKE :resumenRecurso "
                     + "OR p.articulo.nombreAlternativo LIKE :nombreAlternativo OR p.articulo.referenciasBibliograficas LIKE :referenciab "
                     + "OR p.articulo.creador LIKE :creador OR p.articulo.tablaContenidos LIKE :tablaContenidos "
                     + "OR p.articulo.codigo LIKE :codigo "
                     + "ORDER BY p.articulo.titulo ASC",Publicacion.class)


                    .setParameter("autor","%"+cadena+"%")//yes
                    .setParameter("asunto","%"+cadena+"%")//yes
                    .setParameter("titulo","%"+cadena+"%")//yes
                    .setParameter("tipo","%"+cadena+"%") //yes
                    .setParameter("numero", numero)//yes
                    .setParameter("ISSN", numero)//Yes
                    .setParameter("ISBN", "%"+cadena+"%")//Yes
                    .setParameter("editorial","%"+cadena+"%")//yes

                    .setParameter("tomo","%"+cadena+"%")  // OR p.tomo LIKE tomo yy
                    .setParameter("unidad","%"+cadena+"%") // OR a.unidad LIKE unidad yy
                    .setParameter("divisa","%"+cadena+"%") // OR a.divisa LIKE divisa yy
                    .setParameter("formato","%"+cadena+"%") //OR a.formato LIKE formato nyy
                    .setParameter("publicador","%"+cadena+"%") // OR a.publicador LIKE publicador YY
                    .setParameter("resumenRecurso","%"+cadena+"%") // OR a.resumenRecurso LIKE resumenRecurso YY
                    .setParameter("nombreAlternativo","%"+cadena+"%") // OR a.nombreAlternativo LIKE nombreAlternativo YY
                    .setParameter("referenciab","%"+cadena+"%")// OR a.referenciaBibliograficas LIKE refb yy
                    .setParameter("creador","%"+cadena+"%")                 // OR a.creador LIKE creador yy
                    .setParameter("tablaContenidos","%"+cadena+"%")         //OR a.tabla_contenidos LIKE tablaContenidos yy
                    .setParameter("codigo","%"+cadena+"%");                     // OR a.codigo LIKE codigo

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

    public List<Publicacion> getListSuscripciones() {
                    TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p WHERE p.articulo.tipoArticulo.descripcion LIKE :tipo  ORDER BY p.articulo.titulo ASC",Publicacion.class)
                    .setParameter("tipo","%suscripcion%"); //yes
                    List<Publicacion>l=query.getResultList();
                    return l;
    }

    public Publicacion getOfertaDelDia() {

                     TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p ",Publicacion.class)
                             .setMaxResults(1);

                    Publicacion l=query.getSingleResult();
                   return l;
    }

    public Publicacion getOfertaMes() {

                     TypedQuery<Publicacion> query=em.createQuery("SELECT p FROM Publicacion p ",Publicacion.class)
                             .setMaxResults(1);

                    Publicacion l=query.getSingleResult();
                   return l;
    }





    }

    

    


