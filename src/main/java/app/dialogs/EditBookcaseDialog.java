package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Bookcase;

import javax.swing.*;
import java.awt.*;

public class EditBookcaseDialog extends JDialog implements LanguageChangeListener {

    private Bookcase result = null;
    private final Bookcase originalBookcase;
    private JTextField nameField;
    private JButton ok;
    private JButton cancel;
    private JButton delete;
    private boolean deleted = false;

    public EditBookcaseDialog(JFrame parent, Bookcase bookcase) {
        super(parent, Localization.get("dialog.edit.bookcase.title"), true);
        this.originalBookcase = bookcase;
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadBookcaseData();
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
        delete = new JButton(Localization.get("button.delete"));
        delete.setForeground(Color.RED);

        ok.addActionListener(e -> {
            result = originalBookcase;
            result.setName(nameField.getText().trim());
            dispose();
        });

        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, Localization.get("message.confirm.delete"));
            if (confirm == JOptionPane.YES_OPTION) {
                deleted = true;
                dispose();
            }
        });

        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        buttonPanel.add(delete);
        add(buttonPanel, gbc);
    }

    private void loadBookcaseData() {
        if (originalBookcase != null) {
            nameField.setText(originalBookcase.getName() != null ? originalBookcase.getName() : "");
        }
    }

    public Bookcase showDialog() {
        setVisible(true);
        return result;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.bookcase.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        delete.setText(Localization.get("button.delete"));
        revalidate();
        repaint();
    }
}
