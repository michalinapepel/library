package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Loan;
import management.DataBaseLoans;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego wypisywania wypożyczeń
 */
public class ListLoansDialog extends JDialog implements LanguageChangeListener {

     private final List<Loan> loans = new ArrayList<>();
     private final List<Loan> filteredLoans = new ArrayList<>();
     private final DataBaseLoans dbLoans = new DataBaseLoans();
     private JTable loansTable;
     private JTextField searchField;
     private JComboBox<String> filterCombo;
     private JButton search;
     private JButton close;
     private JButton returnBook;
     private JButton edit;
     private JButton delete;

    public ListLoansDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.loans.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(700, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel(Localization.get("label.search")));
        filterCombo = new JComboBox<>(new String[]{
            Localization.get("label.all"),
            Localization.get("label.active"),
            Localization.get("label.returned"),
            Localization.get("label.overdue")
        });
        searchField = new JTextField(20);
        search = new JButton(Localization.get("button.search"));
        searchPanel.add(filterCombo);
        searchPanel.add(searchField);
        searchPanel.add(search);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
            Localization.get("label.book"),
            Localization.get("label.borrower"),
            Localization.get("label.loanDate"),
            Localization.get("label.dueDate"),
            Localization.get("label.returnDate"),
            Localization.get("label.status")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loansTable = new JTable(tableModel);
        loansTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        loansTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && row < filteredLoans.size()) {
                    Loan loan = filteredLoans.get(row);
                    if (!loan.isReturned() && loan.getDueDate() != null && loan.getDueDate().isBefore(LocalDate.now())) {
                        c.setBackground(new Color(255, 180, 180));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
        JScrollPane scrollPane = new JScrollPane(loansTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         returnBook = new JButton(Localization.get("button.returnBook"));
         edit = new JButton(Localization.get("button.edit"));
         delete = new JButton(Localization.get("button.delete"));
         delete.setForeground(Color.RED);
         close = new JButton(Localization.get("button.close"));

         returnBook.addActionListener(e -> returnSelectedBook());
         edit.addActionListener(e -> editSelectedLoan());
         delete.addActionListener(e -> deleteSelectedLoans());
         close.addActionListener(e -> dispose());

         buttonPanel.add(returnBook);
         buttonPanel.add(edit);
         buttonPanel.add(delete);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchLoans());
        loadAllLoans();
    }

    private void loadAllLoans() {
        filteredLoans.clear();
        filteredLoans.addAll(loans);
        refreshTable();
    }

    private void searchLoans() {
        String searchText = searchField.getText().trim().toLowerCase();
        String filterType = (String) filterCombo.getSelectedItem();

        filteredLoans.clear();

        for (Loan loan : loans) {
            boolean isOverdue = !loan.isReturned() && loan.getDueDate() != null && loan.getDueDate().isBefore(LocalDate.now());
            boolean statusMatch = Localization.get("label.all").equals(filterType) ||
                (Localization.get("label.active").equals(filterType) && !loan.isReturned()) ||
                (Localization.get("label.returned").equals(filterType) && loan.isReturned()) ||
                (Localization.get("label.overdue").equals(filterType) && isOverdue);

            boolean textMatch = searchText.isEmpty() ||
                (loan.getBook() != null && loan.getBook().getTitle().toLowerCase().contains(searchText)) ||
                (loan.getBorrower() != null && loan.getBorrower().getFirstName().toLowerCase().contains(searchText));

            if (statusMatch && textMatch) {
                filteredLoans.add(loan);
            }
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) loansTable.getModel();
        model.setRowCount(0);

        for (Loan loan : filteredLoans) {
            Object[] row = {
                loan.getBook() != null ? loan.getBook().getTitle() : "",
                loan.getBorrower() != null ?
                    loan.getBorrower().getFirstName() + " " + loan.getBorrower().getLastName() : "",
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.isReturned() ? Localization.get("label.returned") : Localization.get("label.active")
            };
            model.addRow(row);
        }
    }

     private void returnSelectedBook() {
         int selectedRow = loansTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.loan"));
             return;
         }

         Loan selectedLoan = filteredLoans.get(selectedRow);
         if (selectedLoan.isReturned()) {
             JOptionPane.showMessageDialog(this, Localization.get("message.loan.already.returned"));
             return;
         }

          EditLoanDialog dialog = new EditLoanDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedLoan);
          Loan returnedLoan = dialog.showDialog();

          if (returnedLoan != null) {
              refreshTable();
          }
     }

     private void editSelectedLoan() {
         int selectedRow = loansTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.loan"));
             return;
         }

         Loan selectedLoan = filteredLoans.get(selectedRow);
          EditLoanDialog dialog = new EditLoanDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedLoan);
          Loan editedLoan = dialog.showDialog();

          if (editedLoan != null) {
              refreshTable();
          }
     }

     private void deleteSelectedLoans() {
         int[] selectedRows = loansTable.getSelectedRows();
         if (selectedRows.length == 0) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.loan"));
             return;
         }
         int confirm = JOptionPane.showConfirmDialog(this,
             Localization.get("message.confirm.delete.selected"),
             Localization.get("dialog.confirm.delete.title"),
             JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         if (confirm == JOptionPane.YES_OPTION) {
             List<Loan> toDelete = new ArrayList<>();
             for (int row : selectedRows) toDelete.add(filteredLoans.get(row));
             for (Loan loan : toDelete) {
                 dbLoans.deleteLoan(loan.getId());
                 loans.remove(loan);
             }
             loadAllLoans();
         }
     }

    public void setLoans(List<Loan> loans) {
        this.loans.clear();
        this.loans.addAll(loans);
        loadAllLoans();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.loans.title"));
         search.setText(Localization.get("button.search"));
         returnBook.setText(Localization.get("button.returnBook"));
         edit.setText(Localization.get("button.edit"));
         delete.setText(Localization.get("button.delete"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
