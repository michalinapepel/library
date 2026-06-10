package management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;

//basically dane o db z ktora sie laczy
public class DatabaseConnection {
	static String url = "jdbc:postgresql://localhost:5432/java";
	static String uname = "postgres";
	static String pass = "12341234";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, uname, pass);
	}
}
