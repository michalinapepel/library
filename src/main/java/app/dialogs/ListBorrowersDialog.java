package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Borrower;
import management.DataBaseBorrowers;
import management.DataBaseLoans;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego wypisywania wypożyczających
 */
public class ListBorrowersDialog extends JDialog implements LanguageChangeListener {

     private final List<Borrower> borrowers = new ArrayList<>();
     private final List<Borrower> filteredBorrowers = new ArrayList<>();
     private final DataBaseBorrowers dbBorrowers = new DataBaseBorrowers();
     private final DataBaseLoans dbLoans = new DataBaseLoans();
     private JTable borrowersTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;
     private JButton delete;

    public ListBorrowersDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.borrowers.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(700, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel(Localization.get("label.search")));
        searchField = new JTextField(20);
        search = new JButton(Localization.get("button.search"));
        searchPanel.add(searchField);
        searchPanel.add(search);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
            Localization.get("label.firstName"),
            Localization.get("label.lastName"),
            Localization.get("label.city"),
            Localization.get("label.cardNumber")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        borrowersTable = new JTable(tableModel);
        borrowersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(borrowersTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         delete = new JButton(Localization.get("button.delete"));
         delete.setForeground(Color.RED);
         edit.addActionListener(e -> editSelectedBorrower());
         delete.addActionListener(e -> deleteSelectedBorrowers());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(delete);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchBorrowers());
        loadAllBorrowers();
    }

    private void loadAllBorrowers() {
        filteredBorrowers.clear();
        filteredBorrowers.addAll(borrowers);
        refreshTable();
    }

    private void searchBorrowers() {
        String searchText = searchField.getText().trim().toLowerCase();
        filteredBorrowers.clear();

        for (Borrower borrower : borrowers) {
            boolean matches = searchText.isEmpty() || 
                (borrower.getFirstName() != null && borrower.getFirstName().toLowerCase().contains(searchText)) ||
                (borrower.getLastName() != null && borrower.getLastName().toLowerCase().contains(searchText)) ||
                (borrower.getAddressCity() != null && borrower.getAddressCity().toLowerCase().contains(searchText));
            
            if (matches) {
                filteredBorrowers.add(borrower);
            }
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) borrowersTable.getModel();
        model.setRowCount(0);

        for (Borrower borrower : filteredBorrowers) {
            Object[] row = {
                borrower.getFirstName(),
                borrower.getLastName(),
                borrower.getAddressCity(),
                borrower.getCardNumber()
            };
            model.addRow(row);
        }
    }

    private void editSelectedBorrower() {
        int selectedRow = borrowersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.borrower"));
            return;
        }

        Borrower selectedBorrower = filteredBorrowers.get(selectedRow);
        EditBorrowerDialog dialog = new EditBorrowerDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedBorrower);
        Borrower editedBorrower = dialog.showDialog();

        if (editedBorrower != null) {
            refreshTable();
        }
    }

    private void deleteSelectedBorrowers() {
        int[] selectedRows = borrowersTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.borrower"));
            return;
        }

        List<Borrower> toDelete = new ArrayList<>();
        for (int row : selectedRows) toDelete.add(filteredBorrowers.get(row));

        // Check for active loans — hard block
        List<String> withActive = new ArrayList<>();
        for (Borrower b : toDelete) {
            if (dbLoans.hasActiveLoans(b.getId())) {
                withActive.add(b.getFirstName() + " " + b.getLastName());
            }
        }
        if (!withActive.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                Localization.get("message.delete.active.loans") + "\n" +
                String.join("\n", withActive) + "\n\n" + Localization.get("message.delete.return.first"),
                Localization.get("dialog.active.loans.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for historical loans — warn that history will also be deleted
        List<String> withHistory = new ArrayList<>();
        for (Borrower b : toDelete) {
            if (dbLoans.hasAnyLoans(b.getId())) {
                withHistory.add(b.getFirstName() + " " + b.getLastName());
            }
        }

        String confirmMsg;
        if (!withHistory.isEmpty()) {
            confirmMsg = Localization.get("message.delete.history") + "\n" +
                String.join("\n", withHistory) + "\n\n" + Localization.get("message.confirm.delete.selected");
        } else {
            confirmMsg = Localization.get("message.confirm.delete.selected");
        }

        int confirm = JOptionPane.showConfirmDialog(this, confirmMsg,
            Localization.get("dialog.confirm.delete.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            for (Borrower borrower : toDelete) {
                dbLoans.deleteLoansByBorrower(borrower.getId());
                dbBorrowers.deleteBorrower(borrower.getId());
                borrowers.remove(borrower);
            }
            loadAllBorrowers();
        }
    }

    public void setBorrowers(List<Borrower> borrowers) {
        this.borrowers.clear();
        this.borrowers.addAll(borrowers);
        loadAllBorrowers();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.borrowers.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         delete.setText(Localization.get("button.delete"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
