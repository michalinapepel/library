package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Section;
import management.DataBaseSections;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalityType;

/**
 * Klasa okna dialogowego edytowania działu (sekcji)
 */
public class EditSectionDialog extends JDialog implements LanguageChangeListener {

    private Section result = null;
    private final Section originalSection;
    private final DataBaseSections dbSections;
    private JTextField nameField;
    private JTextField descriptionField;
    private JButton ok;
    private JButton cancel;

    public EditSectionDialog(Window parent, Section section) {
        super(parent, Localization.get("dialog.edit.section.title"), ModalityType.APPLICATION_MODAL);
        this.originalSection = section;
        this.dbSections = new DataBaseSections();
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadSectionData();
        setSize(400, 180);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.name")), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.description")), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        add(descriptionField, gbc);

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
            result = new Section(originalSection.getId(), nameField.getText().trim(), descriptionField.getText().trim());
            dbSections.updateSection(result);
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

    private void loadSectionData() {
        if (originalSection != null) {
            nameField.setText(originalSection.getKey() != null ? originalSection.getKey() : "");
            descriptionField.setText(originalSection.getDescription() != null ? originalSection.getDescription() : "");
        }
    }

    public Section showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.section.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
