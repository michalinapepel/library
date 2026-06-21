package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Bookcase;
import domain.Shelf;
import management.DataBaseShelfs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego wypisywania półek
 */
public class ListShelvesDialog extends JDialog implements LanguageChangeListener {

     private final List<Shelf> shelves = new ArrayList<>();
     private final List<Shelf> filteredShelves = new ArrayList<>();
     private Bookcase[] bookcases = new Bookcase[0];
     private final DataBaseShelfs dbShelves = new DataBaseShelfs();
     private JTable shelvesTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;
     private JButton delete;

    public ListShelvesDialog(Window parent) {
        super(parent, Localization.get("dialog.list.shelves.title"), ModalityType.APPLICATION_MODAL);
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
            Localization.get("label.name"),
            Localization.get("label.bookcaseName")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        shelvesTable = new JTable(tableModel);
        shelvesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(shelvesTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         delete = new JButton(Localization.get("button.delete"));
         delete.setForeground(Color.RED);
         edit.addActionListener(e -> editSelectedShelf());
         delete.addActionListener(e -> deleteSelectedShelves());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(delete);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchShelves());
         loadAllShelves();
     }

     private void editSelectedShelf() {
         int selectedRow = shelvesTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.shelf"));
             return;
         }

         Shelf selectedShelf = filteredShelves.get(selectedRow);
          EditShelfDialog dialog = new EditShelfDialog(SwingUtilities.getWindowAncestor(this), selectedShelf, bookcases);
          Shelf editedShelf = dialog.showDialog();

          if (editedShelf != null) {
              refreshTable();
          }
      }

     private void deleteSelectedShelves() {
         int[] selectedRows = shelvesTable.getSelectedRows();
         if (selectedRows.length == 0) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.shelf"));
             return;
         }

         List<String> blocked = new ArrayList<>();
         List<Shelf> toDelete = new ArrayList<>();
         for (int row : selectedRows) {
             Shelf shelf = filteredShelves.get(row);
             if (dbShelves.hasBooksAssigned(shelf.getId())) {
                 blocked.add(shelf.getName());
             } else {
                 toDelete.add(shelf);
             }
         }

         if (!blocked.isEmpty()) {
             JOptionPane.showMessageDialog(this,
                 Localization.get("message.delete.shelf.has.books") + "\n" + String.join("\n", blocked),
                 Localization.get("dialog.delete.blocked.title"), JOptionPane.ERROR_MESSAGE);
             return;
         }

         int confirm = JOptionPane.showConfirmDialog(this,
             Localization.get("message.confirm.delete.selected"),
             Localization.get("dialog.confirm.delete.title"),
             JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         if (confirm == JOptionPane.YES_OPTION) {
             for (Shelf shelf : toDelete) {
                 dbShelves.deleteShelf(shelf.getId());
                 shelves.remove(shelf);
             }
             loadAllShelves();
         }
     }

     private void loadAllShelves() {
         filteredShelves.clear();
         filteredShelves.addAll(shelves);
         refreshTable();
     }

     private void searchShelves() {
        String searchText = searchField.getText().trim().toLowerCase();
        filteredShelves.clear();

        for (Shelf shelf : shelves) {
            if (searchText.isEmpty() || 
                (shelf.getName() != null && shelf.getName().toLowerCase().contains(searchText))) {
                filteredShelves.add(shelf);
            }
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) shelvesTable.getModel();
        model.setRowCount(0);

        for (Shelf shelf : filteredShelves) {
            Object[] row = {
                shelf.getName(),
                shelf.getBookcaseName()
            };
            model.addRow(row);
        }
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves.clear();
        this.shelves.addAll(shelves);
        loadAllShelves();
    }

    public void setBookcases(Bookcase[] bookcases) {
        this.bookcases = bookcases;
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.shelves.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         delete.setText(Localization.get("button.delete"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
