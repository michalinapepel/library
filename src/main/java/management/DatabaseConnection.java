package management;

import app.AppConfig;
import app.Localization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

/**
 * Odpowiada za nawiązywanie połączenia z bazą danych.
 * <p>
 * Parametry połączenia (adres URL, nazwa użytkownika, hasło) są wczytywane
 * jednorazowo z pliku właściwości wskazanego w {@link AppConfig#DB_PROPERTIES_FILE}
 * podczas inicjalizacji klasy.
 */
public class DatabaseConnection {

	/**
	 * Adres URL bazy danych
	 */
	private static String url;

	/**
	 * Nazwa użytkownika bazy danych
	 */
	private static String uname;

	/**
	 * Hasło użytkownika bazy danych
	 */
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


