package app;

/**
 * Centralna klasa konfiguracji aplikacji.
 * Wszystkie stałe parametryczne są zebrane tutaj zamiast być rozrzucone po kodzie.
 */
public final class AppConfig {

    /** Rozmiar puli wątków używanej do operacji w tle. */
    public static final int THREAD_POOL_SIZE = 4;

    /** Domyślna liczba dni wypożyczenia książki. */
    public static final int DEFAULT_LOAN_DAYS = 14;

    /** Minimalny numer karty bibliotecznej dla nowych czytelników. */
    public static final int MIN_CARD_NUMBER = 20000;

    /** Nazwa pliku właściwości połączenia z bazą danych. */
    public static final String DB_PROPERTIES_FILE = "database.properties";

    private AppConfig() {}
}
