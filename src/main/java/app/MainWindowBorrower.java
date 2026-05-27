package app;

import javax.swing.*;

public class MainWindowBorrower extends JFrame implements LanguageChangeListener {
    public MainWindowBorrower() {}

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        revalidate();
        repaint();
    }
}
