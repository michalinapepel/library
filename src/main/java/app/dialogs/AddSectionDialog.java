package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Section;

import javax.swing.*;
import java.awt.*;

public class AddSectionDialog extends JDialog implements LanguageChangeListener {

    private Section result = null;
    private JTextField nameField;
    private JButton ok;
    private JButton cancel;

    public AddSectionDialog(JFrame parent) {
        super(parent, Localization.get("dialog.add.section.title"), true);
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

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.name")), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            String sectionKey = nameField.getText().trim();

            if (sectionKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.name.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (sectionKey.length() > 100) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.name.maxLength"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Section(0, sectionKey);
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

    public Section showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.section.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
