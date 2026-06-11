package app.panels;

import app.LanguageChangeListener;
import app.Localization;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Objects;

/**
 * Pasek narzędziowy z przyciskami zmiany języka aplikacji.
 * Klasa implementuje {@code LanguageChangeListener}, aby aktualizować napisy
 * po zmianie lokalizacji.
 */
public class ToolBar extends JPanel implements LanguageChangeListener {

    /**
     * Przycisk zmieniający język aplikacji na polski.
     */
    private final JButton plButton;

    /**
     * Przycisk zmieniający język aplikacji na angielski.
     */
    private final JButton enButton;

    public ToolBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        plButton = new JButton(loadIcon("/icons/pl.png"));
        enButton = new JButton(loadIcon("/icons/en.png"));

        add(plButton);
        add(enButton);

        plButton.addActionListener(e -> Localization.setLanguage(Locale.forLanguageTag("pl")));
        enButton.addActionListener(e -> Localization.setLanguage(Locale.forLanguageTag("en")));

        Localization.addLanguageChangeListener(this);

        updateTexts();
    }

    private ImageIcon loadIcon(String path) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void updateTexts() {
        plButton.setToolTipText(Localization.get("lang.polish"));
        enButton.setToolTipText(Localization.get("lang.english"));
    }

    @Override
    public void onLanguageChanged() {
        updateTexts();
        revalidate();
        repaint();
    }
}
