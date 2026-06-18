package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Section;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListSectionsDialog extends JDialog implements LanguageChangeListener {

     private final List<Section> sections = new ArrayList<>();
     private JTable sectionsTable;
     private JTextField searchField;
     private JButton search;
     private JButton close;
     private JButton edit;
     private final List<Section> filteredSections = new ArrayList<>();

    public ListSectionsDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.sections.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel(Localization.get("label.search.by.id")));
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
        sectionsTable = new JTable(tableModel);
        sectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(sectionsTable);
        add(scrollPane, BorderLayout.CENTER);

         // Buttons panel
         JPanel buttonPanel = new JPanel();
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         edit.addActionListener(e -> editSelectedSection());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
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
          EditSectionDialog dialog = new EditSectionDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedSection);
          Section editedSection = dialog.showDialog();

          if (dialog.isDeleted()) {
              sections.remove(selectedSection);
              loadAllSections();
          } else if (editedSection != null) {
              refreshTable();
          }
      }

     private void loadAllSections() {
         filteredSections.clear();
         filteredSections.addAll(sections);
         refreshTable();
     }

      private void searchSections() {
         String searchText = searchField.getText().trim();
         filteredSections.clear();

         try {
             int searchId = Integer.parseInt(searchText);
             for (Section section : sections) {
                 if (section.getId() == searchId) {
                     filteredSections.add(section);
                 }
             }
         } catch (NumberFormatException e) {
             if (searchText.isEmpty()) {
                 filteredSections.addAll(sections);
             } else {
                 for (Section section : sections) {
                     if (section.getName() != null && section.getName().toLowerCase().contains(searchText.toLowerCase())) {
                         filteredSections.add(section);
                     }
                 }
             }
         }

         refreshTable();
     }

     private void refreshTable() {
         DefaultTableModel model = (DefaultTableModel) sectionsTable.getModel();
         model.setRowCount(0);

         for (Section section : filteredSections) {
             Object[] row = {
                 section.getId(),
                 section.getName()
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
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
