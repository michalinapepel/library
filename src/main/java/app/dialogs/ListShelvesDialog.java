package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Shelf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListShelvesDialog extends JDialog implements LanguageChangeListener {

     private final List<Shelf> shelves = new ArrayList<>();
     private final List<Shelf> filteredShelves = new ArrayList<>();
     private JTable shelvesTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;

    public ListShelvesDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.shelves.title"), true);
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
        shelvesTable = new JTable(tableModel);
        shelvesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(shelvesTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         edit.addActionListener(e -> editSelectedShelf());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
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
          EditShelfDialog dialog = new EditShelfDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedShelf);
          Shelf editedShelf = dialog.showDialog();

          if (dialog.isDeleted()) {
              shelves.remove(selectedShelf);
              loadAllShelves();
          } else if (editedShelf != null) {
              refreshTable();
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
                shelf.getId(),
                shelf.getName()
            };
            model.addRow(row);
        }
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves.clear();
        this.shelves.addAll(shelves);
        loadAllShelves();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.shelves.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
