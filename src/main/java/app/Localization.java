package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Pomocnicza klasa do zarządzania lokalizacją (językiem) aplikacji.
 * Umożliwia ustawienie bieżącego {@link Locale}, ładowanie odpowiedniego
 * {@link ResourceBundle} oraz powiadamianie zarejestrowanych słuchaczy o zmianie języka.
 */
public final class Localization {

    /**
     * Aktualnie wybrany język aplikacji (locale).
     */
    private static Locale currentLocale = Locale.forLanguageTag("pl");

    /**
     * Zestaw zasobów (bundle) zawierający tłumaczenia dla aktualnego locale.
     */
    private static ResourceBundle bundle = loadBundle(currentLocale);

    /**
     * Lista obserwatorów, które zostaną powiadomione przy zmianie języka.
     */
    private static final List<LanguageChangeListener> listeners = new ArrayList<>();

    /**
     * Ładuje zestaw zasobów z tłumaczeniami dla podanego locale.
     *
     * @param locale język, dla którego ma zostać wczytany zestaw zasobów
     * @return zestaw zasobów odpowiadający podanemu locale
     */
    private static ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle("Lang", locale);
    }

    /**
     * Ustawia bieżący język aplikacji i powiadamia zarejestrowanych słuchaczy.
     *
     * @param locale nowy język aplikacji
     */
    public static void setLanguage(Locale locale) {
        currentLocale = locale;
        bundle = loadBundle(locale);

        // powiadom wszystkie panele
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged();
        }
    }

    /**
     * Zwraca przetłumaczony tekst dla podanego klucza w bieżącym języku.
     *
     * @param key klucz lokalizacji
     * @return tekst przypisany do klucza w bieżącym języku
     */
    public static String get(String key) {
        return bundle.getString(key);
    }

    /**
     * Rejestruje słuchacza, który będzie powiadamiany o zmianie języka.
     *
     * @param listener słuchacz do zarejestrowania
     */
    public static void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

}


