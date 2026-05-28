package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;

import javax.swing.*;
import java.awt.*;

public class EmployeeDialog extends JDialog implements LanguageChangeListener {

    private String code = null;
    private JButton ok;
    private JButton cancel;

    public EmployeeDialog(JFrame parent) {
        super(parent, Localization.get("dialog.employee.title"), true);

        setLayout(new BorderLayout(10, 10));

        JPasswordField field = new JPasswordField();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            code = new String(field.getPassword()).trim();
            dispose();
        });

        cancel.addActionListener(e -> {
            code = null;
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
        return code;
    }


    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.employee.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
