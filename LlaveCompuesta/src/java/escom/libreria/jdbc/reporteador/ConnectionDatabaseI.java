package escom.libreria.jdbc.reporteador;

import java.sql.Connection;
import java.sql.ResultSet;



public interface ConnectionDatabaseI {	
	public Connection getConnection() throws Exception;
	public ResultSet  getResulsetQuery()throws Exception;
	public void   closeDataBaseProcessor(Connection con);
}
