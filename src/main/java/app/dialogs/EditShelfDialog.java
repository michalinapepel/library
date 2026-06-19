package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Bookcase;
import domain.Shelf;
import management.DataBaseShelfs;

import javax.swing.*;
import java.awt.*;

public class EditShelfDialog extends JDialog implements LanguageChangeListener {

    private Shelf result = null;
    private final Shelf originalShelf;
    private final Bookcase[] bookcases;
    private final DataBaseShelfs dbShelves;
    private JComboBox<Bookcase> bookcaseCombo;
    private JTextField nameField;
    private JButton ok;
    private JButton cancel;

    public EditShelfDialog(JFrame parent, Shelf shelf, Bookcase[] bookcases) {
        super(parent, Localization.get("dialog.edit.shelf.title"), true);
        this.originalShelf = shelf;
        this.bookcases = bookcases;
        this.dbShelves = new DataBaseShelfs();
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadShelfData();
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
            Bookcase selectedBookcase = (Bookcase) bookcaseCombo.getSelectedItem();
            if (selectedBookcase == null) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.bookcase.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            result = originalShelf;
            result.setName(nameField.getText().trim());
            result.setBookcaseId(selectedBookcase.getId());
            dbShelves.updateShelf(result);
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

    private void loadShelfData() {
        if (originalShelf != null) {
            nameField.setText(originalShelf.getName() != null ? originalShelf.getName() : "");
            for (Bookcase bookcase : bookcases) {
                if (bookcase.getId() == originalShelf.getBookcaseId()) {
                    bookcaseCombo.setSelectedItem(bookcase);
                    break;
                }
            }
        }
    }

    public Shelf showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.shelf.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
