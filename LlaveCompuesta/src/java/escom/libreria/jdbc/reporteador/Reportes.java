/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.jdbc.reporteador;

import com.escom.info.generadorCDF.ConstantesFacturacion;
import escom.libreria.info.cliente.Cliente;
import escom.libreria.info.facturacion.Articulo;
import escom.libreria.jdbc.reporteador.dto.ArticuloDTO;
import escom.libreria.jdbc.reporteador.dto.ClienteDTO;
import escom.libreria.jdbc.reporteador.dto.CompraDTO;
import escom.libreria.jdbc.reporteador.dto.SuscripcionDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author xxx
 */
public class Reportes {
    private Cliente[] arregloClienteDTOs;
    private Articulo[] arregloArticulos;
    private int size;
    private String articulosINTO,nombreProveedor;
    private BigDecimal descuento;
    private static  Logger logger = Logger.getLogger(Reportes.class);

    public Reportes(Cliente[] ClienteDTOArray) {
        this.arregloClienteDTOs=ClienteDTOArray;
        this.size=this.arregloClienteDTOs.length;

    }

   // public ReporteCliente(Articulo[] arregloArticulos) {
     //   this.arregloArticulos = arregloArticulos;
      //  this.size=this.arregloArticulos.length;
   // }

    public Reportes(String arrticulosIN, String nombreProveedor, BigDecimal descuento) {
        this.articulosINTO=arrticulosIN;
        this.nombreProveedor=nombreProveedor;
        this.descuento=descuento;
    }

	/**
	 * @param args
	 */

	/*METODO QUE CREA EL QUERY SUSCRIPCIONES PARA LOS REPORTES----	!!*/
	public static String crearQueryToSuscripciones(){

		StringBuilder buffer=new StringBuilder();
		 buffer.  append("SELECT DISTINCT a.id_suscripcion AS SUSCRIPCION,")
				 .append("CONCAT(b.codigo,CONCAT('-',b.titulo)) AS ARTICULO,ta.descripcion,	")
				 .append("c.numero,")
				 .append("d.nombre,	")
				 .append("d.paterno,	")
				 .append("d.materno,	")
				 .append("a.estado_envio,")
				 .append("a.fecha_envio	")
				 .append("FROM suscripcion_cliente a, articulo b, suscripcion c, cliente d ,tipo_articulo ta	")
				 .append("WHERE b.id= a.id_articulo ")
				 .append("AND a.id_suscripcion=c.id_suscripcion ")
                                 .append("AND ta.id=b.id_tipo	")
                                 .append("AND d.id=? ") //ID_CLIENTE
                                 .append("AND b.id_tipo=?  ")//and a.id_articulo=? ") //tipoARTICULO & idArticulo
                                 .append("AND a.estado_envio= ? 	")//ESTADO ENIO
                                 .append("AND (a.fecha_envio BETWEEN ? AND ?  )	") //FECHAS_ENVIO
				 .append("And a.id_cliente= d.id ;");

	      	return buffer.toString().toLowerCase();


	}

    public Reportes() {

    }
	/* YA ESTA LISTO ESTE QUERY REPORTE DE SUSCRIPCIONES	*/
	public List<SuscripcionDTO> getReporteSuscripciones(Date fechaInicial,Date fechaFinal,String idCliente,Integer IdTipoArticulo,Integer estadoEnvio)
	{
		List<SuscripcionDTO> listaReportSuscripcion=new ArrayList<SuscripcionDTO>();

		java.sql.Date date = new java.sql.Date(0000-00-00);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Connection connection;

		ConnectionDataBaseImp coneConnectionDatabaseI=new ConnectionDataBaseImp(ConstantesFacturacion.usuarioBase, ConstantesFacturacion.passwordBaSE, ConstantesFacturacion.baseDatos); //ConnectionDataBaseImp(ConstantesFacturacion.usuarioBase,"root","libreriademo");

		System.out.println("QUERY GENERADOR:	"+crearQueryToSuscripciones());
		try {
	    connection = coneConnectionDatabaseI.getConnection();
             PreparedStatement preparando = connection.prepareStatement(crearQueryToSuscripciones());

        preparando.setString(1, idCliente);// idCliento
        preparando.setInt(2, 8); //idTipoArticulo
      //  preparando.setInt(3, idArticulo); //ID ARTICULO
        preparando.setInt(3, 0); //PAQUETERIA
        preparando.setDate(4,date.valueOf(simpleDateFormat.format(fechaInicial)));//FECHA
        preparando.setDate(5,date.valueOf(simpleDateFormat.format(fechaFinal)));//FECHA


        ResultSet resultadoQuery=preparando.executeQuery();
        ResultSetMetaData metaDatos = resultadoQuery.getMetaData();
        int nColumnas= metaDatos.getColumnCount();

         while(resultadoQuery.next())
        {
        	 	SuscripcionDTO suscripcion=new SuscripcionDTO();
                 for(int i=1;i<=nColumnas;i++)
                 System.out.println(metaDatos.getColumnName(i)+":	"+resultadoQuery.getString(i));
        	 suscripcion.setSuscripcion(resultadoQuery.getString(1));
        	 suscripcion.setArticulo(resultadoQuery.getString(2));
        	 suscripcion.setDescripcion(resultadoQuery.getString(3));
             suscripcion.setNumero((resultadoQuery.getInt(4)));
             suscripcion.setNombreCliente(resultadoQuery.getString(5)+" "+resultadoQuery.getString(5)+" "+resultadoQuery.getString(7));
             suscripcion.setEstado_envio(resultadoQuery.getInt(8));
             suscripcion.setFecha_envio(resultadoQuery.getDate(9));

              listaReportSuscripcion.add(suscripcion);
        }//end WHILE
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaReportSuscripcion;
	}
	private String crearQueryToReporteClienteDTO(){
		StringBuilder buffer=new StringBuilder();
		 buffer
                 .append("SELECT DISTINCT e.id,d.porcentaje,c.concepto,df.rfc,CONCAT(CONCAT(a.codigo,'-'),a.titulo) AS Articulo ")
		.append("FROM Cliente e,categoria c,DESCUENTO_Cliente de,DESCUENTO d,DIFACTURACION df,PENDIENTE p,estado es,Articulo a ")
		.append("WHERE e.ID_CATEGORIA=c.id  AND de.ID_DESCUENTO=d.ID ")
		.append("AND df.ID_EDO=es.id AND df.ID_Cliente=e.ID AND p.id_Cliente=e.id AND p.ID_ARTICULO=a.id AND ")
                .append("e.id IN (");
                for(int i=0;i<size;i++){
                   if(i==(size-1))
                    buffer.append("?");
                   else
                    buffer.append("?,");
                }
                 buffer.append(")");


                 System.out.println("QUERY GENERADO:"+buffer.toString());
		 return buffer.toString().toLowerCase();


	}



        public  String createQuertToReporteArticulo(String LISTA_ARTICULOS){
         StringBuilder builder=new StringBuilder();
         builder
        .append("SELECT DISTINCT CONCAT(a.codigo,CONCAT('-',a.titulo)) AS ARTICULO,al.EXISTENCIA AS \"EXISTENCIAS TOTALES\",    ").append("al.EN_CONSIGNA AS \"EN CONSIGNA\", ")
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
        .append(") OR p.NOMBRE LIKE ? ) ") //OR da.DESCUENTO =?) ")
        .append("AND i.ID_ARTICULO = a.ID   ")
        .append("AND al.ID_ARTICULO =a.ID   ")
        .append("AND da.ID_ARTICULO = a.ID  ")
        .append("AND pro.ID_ARTICULO = a.ID ")
        .append("AND pa.ID_ARTICULO = a.ID  ")
        .append("OR pa.ID_PROVEEDOR = p.ID;    ");
         return builder.toString().toLowerCase();

        }

        public List<ArticuloDTO> getReporteArticulos(){
             List<ArticuloDTO> articulosList=new ArrayList<ArticuloDTO>();
        try {

            String queryFinal = createQuertToReporteArticulo(this.articulosINTO);
            System.out.println("**EL QUERY QUE SE GENERO ES***" + queryFinal);

            ConnectionDataBaseImp coneConnectionDatabaseI = new ConnectionDataBaseImp(ConstantesFacturacion.usuarioBase, "root", "libreriademo");
            Connection connection = coneConnectionDatabaseI.getConnection();
            PreparedStatement preparando = connection.prepareStatement(queryFinal);
            preparando.setString(1, this.nombreProveedor);
            //preparando.setDouble(2,this.descuento.doubleValue());
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
	public List<ClienteDTO> getReporteCliente(){
            List<ClienteDTO> ClienteDTOsList = new ArrayList<ClienteDTO>();
        try {
           

            ConnectionDataBaseImp coneConnectionDatabaseI = new ConnectionDataBaseImp(ConstantesFacturacion.usuarioBase, ConstantesFacturacion.passwordBaSE, ConstantesFacturacion.baseDatos);
            Connection connection = coneConnectionDatabaseI.getConnection();
            PreparedStatement preparando = connection.prepareStatement(crearQueryToReporteClienteDTO());
            for(int i=0;i<size;i++)
            preparando.setString((i+1),arregloClienteDTOs[i].getId());
            ResultSet resultadoQuery = preparando.executeQuery();
            while (resultadoQuery.next())
            {
                ClienteDTO ClienteDTO = new ClienteDTO();
                ClienteDTO.setCorreo(resultadoQuery.getString(1));
                ClienteDTO.setPorcentaje(resultadoQuery.getBigDecimal(2));
                ClienteDTO.setConcepto(resultadoQuery.getString(3));
                ClienteDTO.setRfc(resultadoQuery.getString(4));
                ClienteDTO.setArticuloTitulo(resultadoQuery.getString(5));
                ClienteDTOsList.add(ClienteDTO);
            }
            coneConnectionDatabaseI.closeDataBaseProcessor(connection);

        } catch (Exception ex) {
            logger.error("ERROR AL GENER QUERY O NO SE CONECTO ", ex);
            //Logger.getLogger(ReporteClienteDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
             return ClienteDTOsList;
    }//end method



        	/*ME FUNCIONO BIEN ESTE QUERY PROPUESTO BY YAMIL */
public  String crearQueryToCompras(){

		StringBuilder buffer=new StringBuilder();
		 buffer.append("SELECT DISTINCT c.ID AS ID_COMPRA,cl.NOMBRE,	")
				 .append("cl.PATERNO,	")
				 .append("cl.MATERNO,	")
				 .append("c.ID_CLIENTE,	")
				 .append("c.PAGO_TOTAL,	e.proc_almacen ,")
				 .append("c.FECHA,	")
				 .append("c.OBSERVACIONES,	")
				 .append("c.TIPO_PAGO,	")
				 .append("c.estado,p.no_articulo_categoria ,p.precio_neto ,")
				 .append("CONCAT(a.codigo,CONCAT('-',a.titulo)) as ARTICULO , p.TIPO_ENVIO  ")
				 .append("FROM compra c, cliente cl,pedido p,articulo a , almacen_pedido e ")
				 .append("WHERE c.FECHA  BETWEEN ? AND ? AND cl.id =? AND c.estado LIKE ? AND  c.TIPO_PAGO LIKE ? AND p.TIPO_ENVIO LIKE ?	AND a.id=e.id_articulo	")
			     .append("AND c.ID_CLIENTE = cl.ID	")
				.append("ORDER BY cl.PATERNO,cl.MATERNO, cl.NOMBRE;	");
		return buffer.toString().toLowerCase();


	}
	public List<CompraDTO> getReporteCompra(Date fechaInicial,Date fechaFinal,String idCliente,String estadoCompra,String tipoPago,String paqueteria,String tipoEnvio){

		 List<CompraDTO> compra=new ArrayList<CompraDTO>();

		System.out.println(crearQueryToCompras());
		 ConnectionDataBaseImp coneConnectionDatabaseI=new ConnectionDataBaseImp(ConstantesFacturacion.usuarioBase,ConstantesFacturacion.passwordBaSE,ConstantesFacturacion.baseDatos);
         Connection connection;
		try {
		connection = coneConnectionDatabaseI.getConnection();


         PreparedStatement preparando = connection.prepareStatement(crearQueryToCompras());
        preparando.setDate(1,new java.sql.Date(fechaInicial.getTime()));//FECHA
        preparando.setDate(2,new java.sql.Date(fechaFinal.getTime()));//FECHA

        preparando.setString(3, idCliente);// ESTADO DE COMPRA
         preparando.setString(4, estadoCompra);// ESTADO DE COMPRA
         preparando.setString(5, tipoPago); //TIPO DE PAGO
         //preparando.setString(5, "%"+paqueteria+"%"); //PAQUETERIA
         preparando.setString(6, tipoEnvio);  //TIPO DE ENVIO


         ResultSet resultadoQuery=preparando.executeQuery();
         ResultSetMetaData metaDatos = resultadoQuery.getMetaData();
         int nColumnas= metaDatos.getColumnCount();
         System.out.println("Numero de columnas:"+nColumnas);
                 while(resultadoQuery.next())
                {
                	 CompraDTO compraDTO=new CompraDTO();

                	 compraDTO.setIdCompra(resultadoQuery.getInt(1));
                	 compraDTO.setNombre(resultadoQuery.getString(2)+" "+resultadoQuery.getString(3)+" "+resultadoQuery.getString(4));
                	 compraDTO.setIdCliente(resultadoQuery.getString(5));
                	 //FALTA SACAR PAGO TOTAL.
                	 compraDTO.setProcedencia_almacen(resultadoQuery.getString(7));
                	 compraDTO.setFechaCompra(resultadoQuery.getDate(8));
                	 compraDTO.setTipo_pago(resultadoQuery.getString(10));
                	 compraDTO.setEstado(resultadoQuery.getString(11));

                	 compraDTO.setCantidad(resultadoQuery.getInt(12));
                	 compraDTO.setPago_neto(resultadoQuery.getBigDecimal(13));
                	 compraDTO.setDescripcio(resultadoQuery.getString(14));
                          compraDTO.setTipoEnvio(resultadoQuery.getString(15));

                	 for(int i=1;i<=nColumnas;i++)
                         System.out.println(metaDatos.getColumnName(i)+":	"+resultadoQuery.getString(i));
                	     System.out.println("********************************");
                	     compra.add(compraDTO);
                }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compra;
	}
}
