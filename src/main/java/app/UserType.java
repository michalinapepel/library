package app;

/**
 * Określa rodzaj użytkownika wybrany na ekranie startowym aplikacji.
 */
public enum UserType {
    /** Użytkownik będący czytelnikiem (wypożyczającym). */
    BORROWER,
    /** Użytkownik będący pracownikiem biblioteki. */
    EMPLOYEE,
    /** Wybór anulowany przez użytkownika. */
    CANCEL
}

