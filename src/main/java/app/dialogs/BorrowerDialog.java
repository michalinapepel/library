package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;

import javax.swing.*;
import java.awt.*;

public class BorrowerDialog extends JDialog implements LanguageChangeListener {

    private String id = null;
    private JButton ok;
    private JButton cancel;

    public BorrowerDialog(JFrame parent) {
        super(parent, Localization.get("borrower.dialog.title"), true);

        setLayout(new BorderLayout(10, 10));

        JTextField field = new JTextField();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            id = field.getText().trim();
            dispose();
        });

        cancel.addActionListener(e -> {
            id = null;
            dispose();
        });

        add(field, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(ok);
        bottom.add(cancel);
        add(bottom, BorderLayout.SOUTH);

        setSize(300, 100);
        setLocationRelativeTo(parent);
    }

    public String showDialog() {
        setVisible(true);
        return id;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("start.dialog.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
