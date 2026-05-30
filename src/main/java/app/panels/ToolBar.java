package app.panels;

import app.*;
import app.dialogs.StartDialog;
import com.sun.java.accessibility.util.SwingEventMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Objects;

public class ToolBar extends JPanel implements LanguageChangeListener {

    private final JButton plButton;
    private final JButton enButton;
    private final JButton logoutButton;
    private final boolean canLogout;

    public ToolBar(boolean canLogout) {
        this.canLogout = canLogout;
        setLayout(new FlowLayout(FlowLayout.RIGHT));


        plButton = new JButton(loadIcon("/icons/pl.png"));
        enButton = new JButton(loadIcon("/icons/en.png"));

        add(plButton);
        add(enButton);

        logoutButton = new JButton(Localization.get("button.logout"));
        logoutButton.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this);
            if (parent instanceof JFrame frame) {
                frame.dispose();
                new LibraryApp().startApp();
            }

        });
        if (canLogout) add(logoutButton);

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
        logoutButton.setText(Localization.get("button.logout"));
    }

    @Override
    public void onLanguageChanged() {
        updateTexts();
        revalidate();
        repaint();
    }
}
