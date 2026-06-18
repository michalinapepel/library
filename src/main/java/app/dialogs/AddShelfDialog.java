package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Shelf;

import javax.swing.*;
import java.awt.*;

public class AddShelfDialog extends JDialog implements LanguageChangeListener {

    private Shelf result = null;
    private JTextField bookCaseField;
    private JTextField nameField;
    private JButton ok;
    private JButton cancel;

    public AddShelfDialog(JFrame parent) {
        super(parent, Localization.get("dialog.add.shelf.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(400, 150);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Bookcase
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.bookcaseName")), gbc);
        gbc.gridx = 1;
        bookCaseField = new JTextField(20);
        add(bookCaseField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.shelfName")), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            String name = nameField.getText().trim();
            String bookCase = bookCaseField.getText().trim();

            if (bookCase.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nazwa półki jest wymagana!", "Błąd walidacji", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (bookCase.length() > 100) {
                JOptionPane.showMessageDialog(this, "Nazwa regału nie może być dłuższa niż 100 znaków!", "Błąd walidacji", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nazwa półki jest wymagana!", "Błąd walidacji", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (name.length() > 100) {
                JOptionPane.showMessageDialog(this, "Nazwa półki nie może być dłuższa niż 100 znaków!", "Błąd walidacji", JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Shelf();
            result.setName(name);
            result.setBookcaseName(bookCaseField.getText().trim());
            dispose();
        });

        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        add(buttonPanel, gbc);
    }

    public Shelf showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.shelf.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
