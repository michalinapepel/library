package management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

import org.postgresql.ds.PGSimpleDataSource;

public class DatabaseConnection {
	private static String url;
	private static String uname;
	private static String pass;

	static {
		loadProperties();
	}

	private static void loadProperties() {
		Properties props = new Properties();
		try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
			if (input == null) {
				System.err.println("Plik database.properties nie znaleziony!");
				return;
			}
			props.load(input);
			url = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/java");
			uname = props.getProperty("db.username", "postgres");
			pass = props.getProperty("db.password", "12341234");
		} catch (IOException ex) {
			ex.printStackTrace();
			// Fallback values
			url = "jdbc:postgresql://localhost:5432/java";
			uname = "postgres";
			pass = "12341234";
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, uname, pass);
	}
}


