package escom.libreria.jdbc.reporteador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ReporteCliente {

    private escom.libreria.info.cliente.Cliente[] arregloClientes;
    private int size;

    public ReporteCliente(escom.libreria.info.cliente.Cliente[] clienteArray) {
        this.arregloClientes=clienteArray;
        this.size=this.arregloClientes.length;

    }

	/**
	 * @param args
	 */
	
	
	private String crearQuery(){
		StringBuilder buffer=new StringBuilder();
		 buffer.append("SELECT DISTINCT e.id,d.porcentaje,c.concepto,df.rfc,a.titulo ")
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
	public List<Cliente> getReporteClientes(){
            List<Cliente> clientesList = new ArrayList<Cliente>();
        try {
            // TODO Auto-generated method stub
            System.out.println("PROCESANDO QUERY GENERANDO ");
            System.out.println(crearQuery());

            ConnectionDataBaseImp coneConnectionDatabaseI = new ConnectionDataBaseImp("root", "root", "libreriademo");
            Connection connection = coneConnectionDatabaseI.getConnection();
            PreparedStatement preparando = connection.prepareStatement(crearQuery());
            for(int i=0;i<size;i++)
            preparando.setString((i+1),arregloClientes[i].getId());

            // preparando.setString(2, "yamildelgado@hotmail.com");
            //preparando.setString(3, "yamildelgado@hotmail.com");
            ResultSet resultadoQuery = preparando.executeQuery();
            while (resultadoQuery.next()) {
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
            Logger.getLogger(ReporteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
             return clientesList;
    }//end method
}