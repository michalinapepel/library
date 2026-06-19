package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Author;
import management.DataBaseAuthors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListAuthorsDialog extends JDialog implements LanguageChangeListener {

    private final List<Author> authors = new ArrayList<>();
    private final List<Author> filteredAuthors = new ArrayList<>();
    private final DataBaseAuthors dbAuthors = new DataBaseAuthors();
    private JTable authorsTable;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton search;
    private JButton close;
    private JButton edit;
    private JButton delete;

    public ListAuthorsDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.authors.title"), true);
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
        searchTypeCombo = new JComboBox<>(new String[]{
                Localization.get("label.firstName"),
                Localization.get("label.lastName"),
                Localization.get("label.pseudonym"),
                Localization.get("label.nationality")
        });
        searchField = new JTextField(20);
        search = new JButton(Localization.get("button.search"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(search);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
                Localization.get("label.firstName"),
                Localization.get("label.lastName"),
                Localization.get("label.pseudonym"),
                Localization.get("label.nationality"),
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        authorsTable = new JTable(tableModel);
        authorsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(authorsTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         delete = new JButton(Localization.get("button.delete"));
         delete.setForeground(Color.RED);
         edit.addActionListener(e -> editSelectedAuthor());
         delete.addActionListener(e -> deleteSelectedAuthors());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(delete);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

         search.addActionListener(e -> searchAuthors());
         loadAllAuthors();
     }

     private void editSelectedAuthor() {
         int selectedRow = authorsTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.author"));
             return;
         }

         Author selectedAuthor = filteredAuthors.get(selectedRow);
          EditAuthorDialog dialog = new EditAuthorDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedAuthor);
          Author editedAuthor = dialog.showDialog();

          if (editedAuthor != null) {
              refreshTable();
          }
      }

     private void deleteSelectedAuthors() {
         int[] selectedRows = authorsTable.getSelectedRows();
         if (selectedRows.length == 0) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.author"));
             return;
         }
         int confirm = JOptionPane.showConfirmDialog(this,
             Localization.get("message.confirm.delete.selected"),
             Localization.get("dialog.confirm.delete.title"),
             JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         if (confirm == JOptionPane.YES_OPTION) {
             List<Author> toDelete = new ArrayList<>();
             for (int row : selectedRows) toDelete.add(filteredAuthors.get(row));
             for (Author author : toDelete) {
                 dbAuthors.deleteAuthor(author.getId());
                 authors.remove(author);
             }
             loadAllAuthors();
         }
     }

     private void loadAllAuthors() {
         filteredAuthors.clear();
         filteredAuthors.addAll(authors);
         refreshTable();
     }

     private void searchAuthors() {
        String searchText = searchField.getText().trim().toLowerCase();
        String searchType = (String) searchTypeCombo.getSelectedItem();

        filteredAuthors.clear();

        for (Author author : authors) {
            boolean matches = false;

            if (searchText.isEmpty()) {
                matches = true;
            } else if (Localization.get("label.firstName").equals(searchType)) {
                matches = author.getFirstName() != null && author.getFirstName().toLowerCase().contains(searchText);
            } else if (Localization.get("label.lastName").equals(searchType)) {
                matches = author.getLastName() != null && author.getLastName().toLowerCase().contains(searchText);
            } else if (Localization.get("label.pseudonym").equals(searchType)) {
                matches = author.getPseudonym() != null && author.getPseudonym().toLowerCase().contains(searchText);
            } else if (Localization.get("label.nationality").equals(searchType)) {
                matches = author.getNationality() != null && author.getNationality().toLowerCase().contains(searchText);
            }

            if (matches) {
                filteredAuthors.add(author);
            }
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) authorsTable.getModel();
        model.setRowCount(0);

        for (Author author : filteredAuthors) {
            Object[] row = {
                    author.getFirstName(),
                    author.getLastName(),
                    author.getPseudonym(),
                    author.getNationality(),
            };
            model.addRow(row);
        }
    }

    public void setAuthors(List<Author> authors) {
        this.authors.clear();
        this.authors.addAll(authors);
        loadAllAuthors();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.authors.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         delete.setText(Localization.get("button.delete"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
