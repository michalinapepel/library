package domain.base;

/**
 * Klasa reprezentująca ogólną lokalizację w bibliotece.
 * Może być używana jako klasa bazowa dla bardziej konkretnych typów lokalizacji,
 * takich jak regały czy półki. Zawiera podstawowe pola wspólne dla lokalizacji.
 */
public class AbstractLocation {
    /**
     * Nazwa lokalizacji (np. nazwa regału lub półki).
     * Może być {@code null}, jeśli nieustawiona.
     */
    private String name;

    /**
     * Unikalny identyfikator lokalizacji.
     * Służy do odróżniania obiektów w kolekcjach/ bazie danych.
     */
    private int id;
}
