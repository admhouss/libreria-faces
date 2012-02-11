package escom.libreria.jdbc.reporteador;

import escom.libreria.info.facturacion.Articulo;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;




public class ReporteCliente {

    private escom.libreria.info.cliente.Cliente[] arregloClientes;
    private escom.libreria.info.facturacion.Articulo[] arregloArticulos;
    private int size;
    private String articulosINTO,nombreProveedor;
    private BigDecimal descuento;
    private static  Logger logger = Logger.getLogger(ReporteCliente.class);

    public ReporteCliente(escom.libreria.info.cliente.Cliente[] clienteArray) {
        this.arregloClientes=clienteArray;
        this.size=this.arregloClientes.length;

    }

    public ReporteCliente(Articulo[] arregloArticulos) {
        this.arregloArticulos = arregloArticulos;
        this.size=this.arregloArticulos.length;
    }

    public ReporteCliente(String arrticulosIN, String nombreProveedor, BigDecimal descuento) {
        this.articulosINTO=arrticulosIN;
        this.nombreProveedor=nombreProveedor;
        this.descuento=descuento;
    }

	/**
	 * @param args
	 */
	
	
	private String crearQueryToReporteCliente(){
		StringBuilder buffer=new StringBuilder();
		 buffer.append("SELECT DISTINCT e.id,d.porcentaje,c.concepto,df.rfc,CONCAT(CONCAT(a.codigo,'-'),a.titulo) AS Articulo ")
		.append("FROM CLIENTE e,categoria c,DESCUENTO_CLIENTE de,DESCUENTO d,DIFACTURACION df,PENDIENTE p,estado es,Articulo a ")
		.append("WHERE e.ID_CATEGORIA=c.id  AND de.ID_DESCUENTO=d.ID ")
		.append("AND df.ID_EDO=es.id AND df.ID_CLIENTE=e.ID AND p.id_cliente=e.id AND p.ID_ARTICULO=a.id AND ")
                .append("e.id IN (");
                for(int i=0;i<size;i++){
                   if(i==(size-1))
                    buffer.append("?");
                   else
                    buffer.append("?,");
                }
                 buffer.append(")");

		 return buffer.toString();

		
	}

        public  String createQuertToReporteArticulo(String LISTA_ARTICULOS){
         StringBuilder builder=new StringBuilder();
         builder.append("SELECT DISTINCT CONCAT(a.codigo,CONCAT('-',a.titulo)) AS ARTICULO,al.EXISTENCIA AS \"EXISTENCIAS TOTALES\",    ").append("al.EN_CONSIGNA AS \"EN CONSIGNA\", ")
        .append("al.EN_FIRME AS \"EN FIRME\",   ")
        .append("p.NOMBRE AS \"PROVEEDOR\", ")
        .append("a.PRECIO_UNITARIO AS \"PRECIO UNITARIO\",  ")
        .append("i.MONTO_IMPUESTO AS \"IMPUESTO\", ")
        .append("da.DESCUENTO AS \"DESCUENTO\",")
        .append("pro.PRECIO_PUBLICO \"PROMOCION\"   ")
        .append("FROM articulo a, almacen al, proveedor_articulo pa, proveedor p,   ")
        .append("impuesto i, descuento_articulo da, promocion pro   ")
        .append("WHERE (a.ID IN(")
        .append(LISTA_ARTICULOS) //CONTIENE la lista de los articulos seleccionados IN(1,2,3,4,5)
        .append(") OR p.NOMBRE LIKE ? OR da.DESCUENTO =?) ")
        .append("AND i.ID_ARTICULO = a.ID   ")
        .append("AND al.ID_ARTICULO =a.ID   ")
        .append("AND da.ID_ARTICULO = a.ID  ")
        .append("AND pro.ID_ARTICULO = a.ID ")
        .append("AND pa.ID_ARTICULO = a.ID  ")
        .append("AND pa.ID_PROVEEDOR = p.ID;    ");
         return builder.toString();

        }

        public List<ArticuloDTO> getReporteArticulos(){
             List<ArticuloDTO> articulosList=new ArrayList<ArticuloDTO>();
        try {
           
            String queryFinal = createQuertToReporteArticulo(this.articulosINTO);
            System.out.println("**EL QUERY QUE SE GENERO ES***" + queryFinal);

            ConnectionDataBaseImp coneConnectionDatabaseI = new ConnectionDataBaseImp("root", "root", "libreriademo");
            Connection connection = coneConnectionDatabaseI.getConnection();
            PreparedStatement preparando = connection.prepareStatement(queryFinal);
            preparando.setString(1, this.nombreProveedor);
            preparando.setDouble(2,this.descuento.doubleValue());
            ResultSet resultadoQuery = preparando.executeQuery();
            while (resultadoQuery.next())
            {
                ArticuloDTO articuloDTO=new ArticuloDTO();
                articuloDTO.setArticulo(resultadoQuery.getString(1));
                articuloDTO.setExistencia(resultadoQuery.getInt(2));
                articuloDTO.setConsigna(resultadoQuery.getInt(3));
                articuloDTO.setFirme(resultadoQuery.getInt(4));
                articuloDTO.setProveedor(resultadoQuery.getString(5));
                articuloDTO.setPrecio(resultadoQuery.getBigDecimal(6));
                
               // System.out.println("EXISTENCIA:    "+ resultadoQuery.getInt(2));//existencia
               // System.out.println(" EN CONSIGNA:   "+ resultadoQuery.getInt(3));// en consigna
               // System.out.println("EN FIRME:    "+ resultadoQuery.getInt(4));// en firme
               //System.out.println("NOMBRE_PROVEEDOR:    "+ resultadoQuery.getString(5));//nombre proveedor
               // System.out.println("ARITUCLO:    "+ resultadoQuery.getString(1));
               // System.out.println("PRECIO_UNITARIO    "+ resultadoQuery.getBigDecimal(6));

                articuloDTO.setImpuesto(resultadoQuery.getBigDecimal(7));
                articuloDTO.setDescuento(resultadoQuery.getBigDecimal(8));
                articuloDTO.setPromocion(resultadoQuery.getBigDecimal(9));
               // System.out.println("IMPUESTO :  "+ resultadoQuery.getBigDecimal(7));
                //System.out.println(" DESCUENTO:"+ resultadoQuery.getBigDecimal(8));
                 //System.out.println(" PROMOCION:   "+resultadoQuery.getBigDecimal(9));




                 //(size, size, size, size, nombreProveedor, articulosINTO, descuento, descuento, descuento, descuento)

                 articulosList.add(articuloDTO);
            }
                    coneConnectionDatabaseI.closeDataBaseProcessor(connection);


        } catch (Exception ex) {
           logger.error("ERROR AL CREAR REPORTE",ex);
        }

         return articulosList;

        }
	public List<Cliente> getReporteClientes(){
            List<Cliente> clientesList = new ArrayList<Cliente>();
        try {
            // TODO Auto-generated method stub
            //          System.out.println("PROCESANDO QUERY GENERANDO ");
            //            System.out.println(crearQueryToReporteCliente());

            ConnectionDataBaseImp coneConnectionDatabaseI = new ConnectionDataBaseImp("root", "root", "libreriademo");
            Connection connection = coneConnectionDatabaseI.getConnection();
            PreparedStatement preparando = connection.prepareStatement(crearQueryToReporteCliente());
            for(int i=0;i<size;i++)
            preparando.setString((i+1),arregloClientes[i].getId());
            ResultSet resultadoQuery = preparando.executeQuery();
            while (resultadoQuery.next())
            {
                Cliente cliente = new Cliente();
                cliente.setCorreo(resultadoQuery.getString(1));
                cliente.setPorcentaje(resultadoQuery.getBigDecimal(2));
                cliente.setConcepto(resultadoQuery.getString(3));
                cliente.setRfc(resultadoQuery.getString(4));
                cliente.setArticuloTitulo(resultadoQuery.getString(5));
                clientesList.add(cliente);
            }
            coneConnectionDatabaseI.closeDataBaseProcessor(connection);

        } catch (Exception ex) {
            //Logger.getLogger(ReporteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
             return clientesList;
    }//end method
}