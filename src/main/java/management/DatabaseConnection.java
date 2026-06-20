package management;

import app.AppConfig;
import app.Localization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
	private static String url;
	private static String uname;
	private static String pass;

	static {
		loadProperties();
	}

	private static void loadProperties() {
		Properties props = new Properties();
		try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(AppConfig.DB_PROPERTIES_FILE)) {
			if (input == null) {
				throw new RuntimeException(Localization.get("error.db.properties.notFound"));
			}
			props.load(input);
			url = props.getProperty("db.url");
			uname = props.getProperty("db.username");
			pass = props.getProperty("db.password");

			if (url == null || uname == null || pass == null) {
				throw new RuntimeException(Localization.get("error.db.properties.missing"));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException(Localization.get("error.db.properties.loadError"), ex);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, uname, pass);
	}
}


