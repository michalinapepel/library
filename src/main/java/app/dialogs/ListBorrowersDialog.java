package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Borrower;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListBorrowersDialog extends JDialog implements LanguageChangeListener {

     private final List<Borrower> borrowers = new ArrayList<>();
     private JTable borrowersTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;
     private final List<Borrower> filteredBorrowers = new ArrayList<>();

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
        borrowersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(borrowersTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         edit.addActionListener(e -> editSelectedBorrower());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
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

        if (dialog.isDeleted()) {
            borrowers.remove(selectedBorrower);
            loadAllBorrowers();
        } else if (editedBorrower != null) {
            refreshTable();
        }
    }

    private void deleteSelectedBorrower() {
        int selectedRow = borrowersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.borrower"));
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, Localization.get("message.confirm.delete"));
        if (confirm == JOptionPane.YES_OPTION) {
            borrowers.remove(selectedRow);
            refreshTable();
        }
    }

    public void setBorrowers(List<Borrower> borrowers) {
        this.borrowers.clear();
        this.borrowers.addAll(borrowers);
        refreshTable();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.borrowers.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
