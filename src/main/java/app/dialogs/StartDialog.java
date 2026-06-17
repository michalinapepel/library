package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import app.UserType;
import app.panels.ToolBar;

import javax.swing.*;
import java.awt.*;

public class StartDialog extends JDialog implements LanguageChangeListener {

    private UserType result = UserType.CANCEL;
    private final ToolBar toolBar = new ToolBar(false);
    private final JButton borrower;
    private final JButton employee;
    private final JButton cancel;

    public StartDialog(JFrame parent) {
        super(parent, Localization.get("dialog.start.title"), true);
        Localization.addLanguageChangeListener(this);

        setLayout(new GridLayout(4, 1, 10, 10));

        borrower = new JButton(Localization.get("button.borrower"));
        employee = new JButton(Localization.get("button.employee"));
        cancel = new JButton(Localization.get("button.cancel"));

        borrower.addActionListener(e -> {
            result = UserType.BORROWER;
            dispose();
        });

        employee.addActionListener(e -> {
            result = UserType.EMPLOYEE;
            dispose();
        });

        cancel.addActionListener(e -> {
            result = UserType.CANCEL;
            dispose();
        });

        add(toolBar);
        add(borrower);
        add(employee);
        add(cancel);

        setSize(300, 220);
        setLocationRelativeTo(parent);
    }

    public UserType showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.start.title"));
        borrower.setText(Localization.get("button.borrower"));
        employee.setText(Localization.get("button.employee"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
