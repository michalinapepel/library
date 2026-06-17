package app;

import app.dialogs.BorrowBookDialog;
import app.panels.ToolBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainWindowBorrower extends JFrame implements LanguageChangeListener {
    private final ToolBar toolbar;
    private final JTable loansTable;
    private final JButton borrowButton;
    public MainWindowBorrower() {
        Localization.addLanguageChangeListener(this);

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        borrowButton = new JButton(Localization.get("button.borrow"));
        borrowButton.addActionListener(e -> showListBooksDialog());
        borrowButton.setPreferredSize(new Dimension(500, 50));
        toolbar = new ToolBar(true);


        String[] columnNames = {
                Localization.get("label.book"),
                Localization.get("label.loanDate"),
                Localization.get("label.dueDate"),
                Localization.get("label.returnDate"),
                Localization.get("label.status"),
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loansTable = new JTable(tableModel);
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(loansTable);

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(borrowButton, BorderLayout.SOUTH);

        //lista scroll wypozyczen (do maks 5)
        //przycisk wypożycz i wyszukiwarka książek z pomocą tytułu lub autora
        setSize(600, 300);
        setLocationRelativeTo(null);

    }

    private void showListBooksDialog() {
        BorrowBookDialog dialog = new BorrowBookDialog(this);
        dialog.showDialog();
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        borrowButton.setText(Localization.get("button.borrow"));
        revalidate();
        repaint();
    }
}
