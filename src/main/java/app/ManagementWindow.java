package app;

import app.dialogs.*;
import app.panels.ToolBar;
import domain.Bookcase;
import domain.Shelf;
import domain.Section;
import management.DataBaseBookcase;
import management.DataBaseSections;
import management.DataBaseShelfs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ManagementWindow extends JFrame implements LanguageChangeListener{
    private final ToolBar toolbar;
    private final DataBaseSections dbSections;
    private final DataBaseBookcase dbBookcases;
    private final DataBaseShelfs dbShelves;
    private final JButton sectionsButton;
    private final JButton addSectionButton;
    private final JButton bookcasesButton;
    private final JButton addBookcaseButton;
    private final JButton shelvesButton;
    private final JButton addShelfButton;
    private final JButton backButton;
    private final JPanel menu;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    public ManagementWindow() {
        Localization.addLanguageChangeListener(this);

        // Inicjalizacja klasy do obsługi bazy danych
        dbSections = new DataBaseSections();
        dbBookcases = new DataBaseBookcase();
        dbShelves = new DataBaseShelfs();

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 50, 10));

        sectionsButton = new JButton(Localization.get("button.sections"));
        sectionsButton.addActionListener(e -> showListSectionsDialog());
        sectionsButton.setPreferredSize(new Dimension(300, 50));
        addSectionButton = new JButton(Localization.get("button.section.add"));
        addSectionButton.addActionListener(e -> showAddSectionDialog());
        addSectionButton.setPreferredSize(new Dimension(50, 20));
        addSectionButton.setForeground(new Color(0,153,76));
        addSectionButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        bookcasesButton = new JButton(Localization.get("button.bookcases"));
        bookcasesButton.addActionListener(e -> showListBookcasesDialog());
        bookcasesButton.setPreferredSize(new Dimension(300, 50));
        addBookcaseButton = new JButton(Localization.get("button.bookcase.add"));
        addBookcaseButton.addActionListener(e -> showAddBookcaseDialog());
        addBookcaseButton.setPreferredSize(new Dimension(50, 20));
        addBookcaseButton.setForeground(new Color(0,153,76));
        addBookcaseButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        shelvesButton = new JButton(Localization.get("button.shelves"));
        shelvesButton.addActionListener(e -> showListShelvesDialog());
        shelvesButton.setPreferredSize(new Dimension(300, 50));
        addShelfButton = new JButton(Localization.get("button.shelf.add"));
        addShelfButton.addActionListener(e -> showAddShelfDialog());
        addShelfButton.setPreferredSize(new Dimension(50, 20));
        addShelfButton.setForeground(new Color(0,153,76));
        addShelfButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        toolbar = new ToolBar(false);
        backButton = new JButton(Localization.get("button.back"));
        backButton.addActionListener(e -> {
            dispose();
        });

        menu = new JPanel();
        menu.setLayout(new BorderLayout());
        menu.add(toolbar, BorderLayout.EAST);
        menu.add(backButton, BorderLayout.WEST);

        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(sectionsButton, BorderLayout.WEST);
        northPanel.add(addSectionButton, BorderLayout.CENTER);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(bookcasesButton, BorderLayout.WEST);
        centerPanel.add(addBookcaseButton, BorderLayout.CENTER);

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(shelvesButton, BorderLayout.WEST);
        southPanel.add(addShelfButton, BorderLayout.CENTER);


        add(menu);
        add(northPanel);
        add(centerPanel);
        add(southPanel);

        setSize(600, 300);
        setLocationRelativeTo(null);

    }

    private void showListSectionsDialog() {
        ListSectionsDialog dialog = new ListSectionsDialog(this);
        // Pobierz wszystkie sekcje z bazy danych
        dialog.setSections(dbSections.getAllSections());
        dialog.showDialog();
    }

    private void showAddSectionDialog() {
        AddSectionDialog dialog = new AddSectionDialog(this);
        Section newSection = dialog.showDialog();

        if (newSection != null) {
            dbSections.addSection(newSection);
            JOptionPane.showMessageDialog(this, "Sekcja dodana pomyślnie!");
        }
    }

    private void showListBookcasesDialog() {
        ListBookcasesDialog dialog = new ListBookcasesDialog(this);
        // Pobierz wszystkie regały z bazy danych
        dialog.setBookcases(dbBookcases.getAllBookcases());
        dialog.showDialog();
    }

    private void showAddBookcaseDialog() {
        AddBookcaseDialog dialog = new AddBookcaseDialog(this);
        Bookcase newBookcase = dialog.showDialog();

        if (newBookcase != null) {
            dbBookcases.addBookcase(newBookcase);
            JOptionPane.showMessageDialog(this, "Regał dodany pomyślnie!");
        }
    }

    private void showListShelvesDialog() {
        ListShelvesDialog dialog = new ListShelvesDialog(this);
        // Pobierz wszystkie półki z bazy danych
        dialog.setShelves(dbShelves.getAllShelves());
        dialog.showDialog();
    }

    private void showAddShelfDialog() {
        AddShelfDialog dialog = new AddShelfDialog(this);
        Shelf newShelf = dialog.showDialog();

        if (newShelf != null) {
            dbShelves.addShelf(newShelf);
            JOptionPane.showMessageDialog(this, "Półka dodana pomyślnie!");
        }
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        sectionsButton.setText(Localization.get("button.sections"));
        bookcasesButton.setText(Localization.get("button.bookcases"));
        shelvesButton.setText(Localization.get("button.shelves"));
        addSectionButton.setText(Localization.get("button.section.add"));
        addBookcaseButton.setText(Localization.get("button.bookcase.add"));
        addShelfButton.setText(Localization.get("button.shelf.add"));
        backButton.setText(Localization.get("button.back"));

        revalidate();
        repaint();
    }
}
