package app;

import app.panels.ToolBar;

import javax.swing.*;
import java.awt.*;

/**
 * Główna klasa uruchamiająca aplikację biblioteczną.
 * Inicjalizuje interfejs użytkownika i rejestruje się jako obserwator zmian języka.
 */
public class LibraryApp implements LanguageChangeListener {

    /**
     * Główne okno aplikacji.
     */
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryApp::new);
    }

    public LibraryApp() {
        initUI();
        Localization.addLanguageChangeListener(this);
    }

    private void initUI() {
        frame = new JFrame(Localization.get("app.title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        ToolBar toolbar = new ToolBar();
        frame.add(toolbar, BorderLayout.NORTH);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void onLanguageChanged() {
        frame.setTitle(Localization.get("app.title"));
    }
}
