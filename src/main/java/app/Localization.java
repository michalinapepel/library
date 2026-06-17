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

    private static ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle("Lang", locale);
    }

    public static void setLanguage(Locale locale) {
        currentLocale = locale;
        bundle = loadBundle(locale);

        // powiadom wszystkie panele
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged();
        }
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

    public static void removeLanguageChangeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }
}


