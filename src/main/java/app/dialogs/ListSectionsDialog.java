package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Section;
import management.DataBaseSections;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego wypisywania działów
 */
public class ListSectionsDialog extends JDialog implements LanguageChangeListener {

     private final List<Section> sections = new ArrayList<>();
     private final List<Section> filteredSections = new ArrayList<>();
     private final DataBaseSections dbSections = new DataBaseSections();
     private JTable sectionsTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;
     private JButton delete;

    public ListSectionsDialog(Window parent) {
        super(parent, Localization.get("dialog.list.sections.title"), ModalityType.APPLICATION_MODAL);
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
                Localization.get("label.description")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sectionsTable = new JTable(tableModel);
        sectionsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(sectionsTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         delete = new JButton(Localization.get("button.delete"));
         delete.setForeground(Color.RED);
         edit.addActionListener(e -> editSelectedSection());
         delete.addActionListener(e -> deleteSelectedSections());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(delete);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchSections());
         loadAllSections();
     }

     private void editSelectedSection() {
         int selectedRow = sectionsTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.section"));
             return;
         }

         Section selectedSection = filteredSections.get(selectedRow);
          EditSectionDialog dialog = new EditSectionDialog(SwingUtilities.getWindowAncestor(this), selectedSection);
          Section editedSection = dialog.showDialog();

          if (editedSection != null) {
              refreshTable();
          }
      }

     private void deleteSelectedSections() {
         int[] selectedRows = sectionsTable.getSelectedRows();
         if (selectedRows.length == 0) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.section"));
             return;
         }
         int confirm = JOptionPane.showConfirmDialog(this,
             Localization.get("message.confirm.delete.selected"),
             Localization.get("dialog.confirm.delete.title"),
             JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         if (confirm == JOptionPane.YES_OPTION) {
             List<Section> toDelete = new ArrayList<>();
             for (int row : selectedRows) toDelete.add(filteredSections.get(row));
             for (Section section : toDelete) {
                 dbSections.deleteSection(section.getId());
                 sections.remove(section);
             }
             loadAllSections();
         }
     }

     private void loadAllSections() {
         filteredSections.clear();
         filteredSections.addAll(sections);
         refreshTable();
     }

      private void searchSections() {
         String searchText = searchField.getText().trim().toLowerCase();
         filteredSections.clear();

         for (Section section : sections) {
             if (searchText.isEmpty() || (section.getName() != null && section.getName().toLowerCase().contains(searchText))) {
                 filteredSections.add(section);
             }
         }

         refreshTable();
     }

     private void refreshTable() {
         DefaultTableModel model = (DefaultTableModel) sectionsTable.getModel();
         model.setRowCount(0);

         for (Section section : filteredSections) {
             Object[] row = {
                 section.getName(),
                     section.getDescription()
             };
             model.addRow(row);
         }
     }



    public void setSections(List<Section> sections) {
        this.sections.clear();
        this.sections.addAll(sections);
        loadAllSections();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
     public void onLanguageChanged() {
         setTitle(Localization.get("dialog.list.sections.title"));
         search.setText(Localization.get("button.search"));
         edit.setText(Localization.get("button.edit"));
         delete.setText(Localization.get("button.delete"));
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
