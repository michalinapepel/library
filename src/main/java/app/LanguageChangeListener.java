package app;

/**
 * Interfejs, który powinny implementować komponenty chcące otrzymywać powiadomienia
 * o zmianie języka w aplikacji.
 */
public interface LanguageChangeListener {
    /**
     * Wywoływane, gdy następuje zmiana języka/locale aplikacji.
     * Implementacja powinna zaktualizować widoczne teksty i (opcjonalnie) przeprowadzić
     * ponowne przerysowanie komponentu.
     */
    void onLanguageChanged();
}

