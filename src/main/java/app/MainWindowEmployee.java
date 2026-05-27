package app;

import app.panels.ToolBar;

import javax.swing.*;
import java.awt.*;

public class MainWindowEmployee extends JFrame implements LanguageChangeListener {

    private final ToolBar toolbar;
    private final JButton booksButton;
    private final JButton addBookButton;
    private final JButton editBookButton;
    private final JButton loansButton;
    private final JButton addLoanButton;
    private final JButton editLoanButton;
    private final JButton managementButton;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;

    public MainWindowEmployee() {
        Localization.addLanguageChangeListener(this);

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 50, 10));

        booksButton = new JButton(Localization.get("button.books"));
        booksButton.addActionListener(e -> {});
        booksButton.setPreferredSize(new Dimension(300, 50));
        loansButton = new JButton(Localization.get("button.loans"));
        loansButton.addActionListener(e -> {});
        loansButton.setPreferredSize(new Dimension(300, 50));
        managementButton = new JButton(Localization.get("button.management"));
        managementButton.addActionListener(e -> {});
        managementButton.setPreferredSize(new Dimension(500, 50));
        addBookButton = new JButton(Localization.get("button.book.add"));
        addBookButton.addActionListener(e -> {});
        addBookButton.setPreferredSize(new Dimension(50, 20));
        editBookButton = new JButton(Localization.get("button.book.edit"));
        editBookButton.addActionListener(e -> {});
        editBookButton.setPreferredSize(new Dimension(150, 20));
        addLoanButton = new JButton(Localization.get("button.loan.add"));
        addLoanButton.addActionListener(e -> {});
        addLoanButton.setPreferredSize(new Dimension(50, 20));
        editLoanButton = new JButton(Localization.get("button.loan.edit"));
        editLoanButton.addActionListener(e -> {});
        editLoanButton.setPreferredSize(new Dimension(150, 20));

        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(booksButton, BorderLayout.WEST);
        northPanel.add(addBookButton, BorderLayout.CENTER);
        northPanel.add(editBookButton, BorderLayout.EAST);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(loansButton, BorderLayout.WEST);
        centerPanel.add(addLoanButton, BorderLayout.CENTER);
        centerPanel.add(editLoanButton, BorderLayout.EAST);

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(managementButton, BorderLayout.SOUTH);


        toolbar = new ToolBar();
        add(toolbar);
        add(northPanel);
        add(centerPanel);
        add(southPanel);


        setSize(500, 300);
        setLocationRelativeTo(null);
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        booksButton.setText(Localization.get("button.books"));
        loansButton.setText(Localization.get("button.loans"));
        managementButton.setText(Localization.get("button.management"));
        addBookButton.setText(Localization.get("button.book.add"));
        editBookButton.setText(Localization.get("button.book.edit"));
        addLoanButton.setText(Localization.get("button.loan.add"));
        editLoanButton.setText(Localization.get("button.loan.edit"));
        revalidate();
        repaint();
    }
}

