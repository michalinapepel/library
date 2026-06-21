package domain;

import app.Localization;
import domain.base.AbstractLocation;

/**
 * Reprezentuje dział (kategorię/sekcję) tematyczny książek w bibliotece.
 * <p>
 * Klucz działu ({@code key}) jest kluczem lokalizacji (np. {@code "section.fantasy"}),
 * który w czasie działania programu jest tłumaczony na nazwę wyświetlaną
 * w bieżącym języku.
 */
public class Section extends AbstractLocation {
    /**
     * Klucz lokalizacji działu używany do pobrania nazwy w bieżącym języku
     */

    private final String key;

    /**
     * Tworzy nowy obiekt działu z podanym identyfikatorem i kluczem lokalizacji.
     *
     * @param id  unikalny identyfikator działu
     * @param key klucz lokalizacji działu
     */
    public Section(int id, String key) {
        this.id = id;
        this.key = key;
    }

    /**
     * Zwraca klucz lokalizacji działu.
     *
     * @return klucz lokalizacji
     */
    public String getKey() { return key; }

    /**
     * Zwraca nazwę działu przetłumaczoną na bieżący język.
     * <p>
     * Jeśli tłumaczenie dla klucza nie jest dostępne, zwracany jest sam klucz.
     *
     * @return nazwa działu w bieżącym języku lub klucz lokalizacji w razie błędu
     */
    public String getName() {
        try {
            return Localization.get(key);
        } catch (Exception ex) {
            return key;
        }
    }

    /**
     * Ustawia identyfikator działu.
     *
     * @param id unikalny identyfikator działu
     */
    public void setId(int id) { this.id = id; }

    /**
     * Zwraca tekstową reprezentację działu, czyli jego nazwę w bieżącym języku.
     *
     * @return nazwa działu
     */
    @Override
    public String toString() { return getName(); }
}
