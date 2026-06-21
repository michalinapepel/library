package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Bookcase;
import domain.Shelf;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa okna dialogowego dodawania półki
 */
public class AddShelfDialog extends JDialog implements LanguageChangeListener {

    private Shelf result = null;
    private final Bookcase[] bookcases;
    private JComboBox<Bookcase> bookcaseCombo;
    private JTextField nameField;
    private JButton ok;
    private JButton cancel;

    public AddShelfDialog(Window parent, Bookcase[] bookcases) {
        super(parent, Localization.get("dialog.add.shelf.title"), ModalityType.APPLICATION_MODAL);
        this.bookcases = bookcases;
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(400, 170);
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
        bookcaseCombo = new JComboBox<>(bookcases);
        add(bookcaseCombo, gbc);

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

            Bookcase selectedBookcase = (Bookcase) bookcaseCombo.getSelectedItem();
            if (selectedBookcase == null) {
                JOptionPane.showMessageDialog(this, Localization.get("message.select.bookcase"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.name.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (name.length() > 100) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.name.maxLength"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Shelf();
            result.setName(name);
            result.setBookcaseId(selectedBookcase.getId());
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
