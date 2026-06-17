package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Bookcase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListBookcasesDialog extends JDialog implements LanguageChangeListener {

     private final List<Bookcase> bookcases = new ArrayList<>();
     private final List<Bookcase> filteredBookcases = new ArrayList<>();
     private JTable bookcasesTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;

    public ListBookcasesDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.bookcases.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(600, 400);
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
            Localization.get("label.id"),
            Localization.get("label.name")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookcasesTable = new JTable(tableModel);
        bookcasesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookcasesTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         edit.addActionListener(e -> editSelectedBookcase());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchBookcases());
         loadAllBookcases();
     }

     private void editSelectedBookcase() {
         int selectedRow = bookcasesTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.bookcase"));
             return;
         }

          Bookcase selectedBookcase = filteredBookcases.get(selectedRow);
          EditBookcaseDialog dialog = new EditBookcaseDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedBookcase);
          Bookcase editedBookcase = dialog.showDialog();

          if (dialog.isDeleted()) {
              bookcases.remove(selectedBookcase);
              loadAllBookcases();
          } else if (editedBookcase != null) {
              refreshTable();
          }
      }

     private void loadAllBookcases() {
         filteredBookcases.clear();
         filteredBookcases.addAll(bookcases);
         refreshTable();
     }

     private void searchBookcases() {
        String searchText = searchField.getText().trim().toLowerCase();
        filteredBookcases.clear();

        for (Bookcase bookcase : bookcases) {
            if (searchText.isEmpty() || 
                (bookcase.getName() != null && bookcase.getName().toLowerCase().contains(searchText))) {
                filteredBookcases.add(bookcase);
            }
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) bookcasesTable.getModel();
        model.setRowCount(0);

        for (Bookcase bookcase : filteredBookcases) {
            Object[] row = {
                bookcase.getId(),
                bookcase.getName()
            };
            model.addRow(row);
        }
    }

    public void setBookcases(List<Bookcase> bookcases) {
        this.bookcases.clear();
        this.bookcases.addAll(bookcases);
        loadAllBookcases();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.bookcases.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
