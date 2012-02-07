package escom.libreria.jdbc.reporteador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ConnectionDataBaseImp implements ConnectionDatabaseI{

	private String user,password,dbname;	

	public ConnectionDataBaseImp(String user, String password, String dbname) {
		super();
		this.user = user;
		this.password = password;
		this.dbname = dbname;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	

	@Override
	public Connection getConnection() throws Exception {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+getDbname(), getUser(), getPassword());
		System.out.println("DATABASES UP ........!");
		return connection;
	}

	@Override
	public ResultSet getResulsetQuery() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void closeDataBaseProcessor(Connection conn) {
		// TODO Auto-generated method stub
		try{
			conn.close();
		}catch(Exception e){
			System.out.println("Databa wa closed");
		}
		
		
	}

}
