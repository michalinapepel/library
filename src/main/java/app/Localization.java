package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

//klasa do zmiany "lokalizacji" - czyli de facto języka
public final class Localization {

    private static Locale currentLocale = Locale.forLanguageTag("pl");
    private static ResourceBundle bundle = loadBundle(currentLocale);

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


