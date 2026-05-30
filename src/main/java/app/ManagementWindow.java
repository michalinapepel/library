package app;

import app.panels.ToolBar;

import javax.swing.*;
import java.awt.*;

public class ManagementWindow extends JFrame implements LanguageChangeListener{
    private final ToolBar toolbar;
    private final JButton sectionsButton;
    private final JButton addSectionButton;
    private final JButton editSectionButton;
    private final JButton bookcasesButton;
    private final JButton addBookcaseButton;
    private final JButton editBookcaseButton;
    private final JButton shelvesButton;
    private final JButton addShelfButton;
    private final JButton editShelfButton;
    private final JButton backButton;
    private final JPanel menu;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    public ManagementWindow() {
        Localization.addLanguageChangeListener(this);

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 50, 10));

        sectionsButton = new JButton(Localization.get("button.sections"));
        sectionsButton.addActionListener(e -> {});
        sectionsButton.setPreferredSize(new Dimension(300, 50));
        addSectionButton = new JButton(Localization.get("button.section.add"));
        addSectionButton.addActionListener(e -> {});
        addSectionButton.setPreferredSize(new Dimension(50, 20));
        addSectionButton.setForeground(new Color(0,153,76));
        addSectionButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        editSectionButton = new JButton(Localization.get("button.section.edit"));
        editSectionButton.addActionListener(e -> {});
        editSectionButton.setPreferredSize(new Dimension(150, 20));

        bookcasesButton = new JButton(Localization.get("button.bookcases"));
        bookcasesButton.addActionListener(e -> {});
        bookcasesButton.setPreferredSize(new Dimension(300, 50));
        addBookcaseButton = new JButton(Localization.get("button.bookcase.add"));
        addBookcaseButton.addActionListener(e -> {});
        addBookcaseButton.setPreferredSize(new Dimension(50, 20));
        addBookcaseButton.setForeground(new Color(0,153,76));
        addBookcaseButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        editBookcaseButton = new JButton(Localization.get("button.bookcase.edit"));
        editBookcaseButton.addActionListener(e -> {});
        editBookcaseButton.setPreferredSize(new Dimension(150, 20));

        shelvesButton = new JButton(Localization.get("button.shelves"));
        shelvesButton.addActionListener(e -> {});
        shelvesButton.setPreferredSize(new Dimension(300, 50));
        addShelfButton = new JButton(Localization.get("button.shelf.add"));
        addShelfButton.addActionListener(e -> {});
        addShelfButton.setPreferredSize(new Dimension(50, 20));
        addShelfButton.setForeground(new Color(0,153,76));
        addShelfButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        editShelfButton = new JButton(Localization.get("button.shelf.edit"));
        editShelfButton.addActionListener(e -> {});
        editShelfButton.setPreferredSize(new Dimension(150, 20));

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
        northPanel.add(editSectionButton, BorderLayout.EAST);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(bookcasesButton, BorderLayout.WEST);
        centerPanel.add(addBookcaseButton, BorderLayout.CENTER);
        centerPanel.add(editBookcaseButton, BorderLayout.EAST);

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(shelvesButton, BorderLayout.WEST);
        southPanel.add(addShelfButton, BorderLayout.CENTER);
        southPanel.add(editShelfButton, BorderLayout.EAST);


        add(menu);
        add(northPanel);
        add(centerPanel);
        add(southPanel);

        setSize(600, 300);
        setLocationRelativeTo(null);

    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        sectionsButton.setText(Localization.get("button.sections"));
        bookcasesButton.setText(Localization.get("button.bookcases"));
        shelvesButton.setText(Localization.get("button.shelves"));
        addSectionButton.setText(Localization.get("button.section.add"));
        editSectionButton.setText(Localization.get("button.section.edit"));
        addBookcaseButton.setText(Localization.get("button.bookcase.add"));
        editBookcaseButton.setText(Localization.get("button.bookcase.edit"));
        addShelfButton.setText(Localization.get("button.shelf.add"));
        editShelfButton.setText(Localization.get("button.shelf.edit"));
        backButton.setText(Localization.get("button.back"));

        revalidate();
        repaint();
    }
}
