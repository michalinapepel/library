package app;

import app.panels.ToolBar;

import javax.swing.*;
import java.awt.*;

public class MainWindowBorrower extends JFrame implements LanguageChangeListener {
    private final ToolBar toolbar;
    private final JTextArea textArea;
    private final JButton borrowButton;
    public MainWindowBorrower() {
        Localization.addLanguageChangeListener(this);

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        borrowButton = new JButton(Localization.get("button.borrow"));
        borrowButton.addActionListener(e -> {});
        borrowButton.setPreferredSize(new Dimension(500, 50));
        toolbar = new ToolBar(true);
        textArea = new JTextArea();


        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(borrowButton, BorderLayout.SOUTH);

        textArea.setEditable(false);
        textArea.setLineWrap(true);

        //lista scroll wypozyczen (do maks 5)
        //przycisk wypożycz i wyszukiwarka książek z pomocą tytułu lub autora
        setSize(500, 300);
        setLocationRelativeTo(null);

    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        borrowButton.setText(Localization.get("button.borrow"));
        revalidate();
        repaint();
    }
}
