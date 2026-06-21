package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Bookcase;
import domain.Shelf;
import management.DataBaseBookcase;
import management.DataBaseShelfs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego wypisywania regałów
 */
public class ListBookcasesDialog extends JDialog implements LanguageChangeListener {

     private final List<Bookcase> bookcases = new ArrayList<>();
     private final List<Bookcase> filteredBookcases = new ArrayList<>();
     private final DataBaseBookcase dbBookcase = new DataBaseBookcase();
     private final DataBaseShelfs dbShelves = new DataBaseShelfs();
     private JTable bookcasesTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;
     private JButton delete;

    public ListBookcasesDialog(Window parent) {
        super(parent, Localization.get("dialog.list.bookcases.title"), ModalityType.APPLICATION_MODAL);
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
            Localization.get("label.name")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookcasesTable = new JTable(tableModel);
        bookcasesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        bookcasesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && bookcasesTable.getSelectedRow() != -1) {
                    showShelvesForBookcase(filteredBookcases.get(bookcasesTable.getSelectedRow()));
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(bookcasesTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         delete = new JButton(Localization.get("button.delete"));
         delete.setForeground(Color.RED);
         edit.addActionListener(e -> editSelectedBookcase());
         delete.addActionListener(e -> deleteSelectedBookcases());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(delete);
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

          if (editedBookcase != null) {
              refreshTable();
          }
      }

     private void deleteSelectedBookcases() {
         int[] selectedRows = bookcasesTable.getSelectedRows();
         if (selectedRows.length == 0) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.bookcase"));
             return;
         }

         List<String> blocked = new ArrayList<>();
         List<Bookcase> toDelete = new ArrayList<>();
         for (int row : selectedRows) {
             Bookcase bookcase = filteredBookcases.get(row);
             if (dbBookcase.hasBooksOnAnyShelff(bookcase.getId())) {
                 blocked.add(bookcase.getName());
             } else {
                 toDelete.add(bookcase);
             }
         }

         if (!blocked.isEmpty()) {
             JOptionPane.showMessageDialog(this,
                 Localization.get("message.delete.bookcase.has.books") + "\n" + String.join("\n", blocked),
                 Localization.get("dialog.delete.blocked.title"), JOptionPane.ERROR_MESSAGE);
             return;
         }

         int confirm = JOptionPane.showConfirmDialog(this,
             Localization.get("message.confirm.delete.selected"),
             Localization.get("dialog.confirm.delete.title"),
             JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         if (confirm == JOptionPane.YES_OPTION) {
             for (Bookcase bookcase : toDelete) {
                 dbBookcase.deleteBookcase(bookcase.getId());
                 bookcases.remove(bookcase);
             }
             loadAllBookcases();
         }
     }

     private void showShelvesForBookcase(Bookcase bookcase) {
         List<Shelf> allShelves = dbShelves.getAllShelves();
         List<Shelf> filtered = new ArrayList<>();
         for (Shelf shelf : allShelves) {
             if (shelf.getBookcaseId() == bookcase.getId()) {
                 filtered.add(shelf);
             }
         }

         ListShelvesDialog dialog = new ListShelvesDialog((JFrame) SwingUtilities.getWindowAncestor(this));
         dialog.setTitle(Localization.get("dialog.list.shelves.title") + " — " + bookcase.getName());
         dialog.setBookcases(bookcases.toArray(new Bookcase[0]));
         dialog.setShelves(filtered);
         dialog.showDialog();
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
         delete.setText(Localization.get("button.delete"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
