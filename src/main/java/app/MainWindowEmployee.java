package app;

import app.panels.ToolBar;

import javax.swing.*;
import java.awt.*;

public class MainWindowEmployee extends JFrame implements LanguageChangeListener {

    private final ToolBar toolbar;
    private final JButton booksButton;
    private final JButton addBookButton;
    private final JButton editBookButton;
    private final JButton borrowersButton;
    private final JButton addBorrowerButton;
    private final JButton editBorrowerButton;
    private final JButton loansButton;
    private final JButton addLoanButton;
    private final JButton editLoanButton;
    private final JButton managementButton;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    private final JPanel managementPanel;

    public MainWindowEmployee() {
        Localization.addLanguageChangeListener(this);

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 50, 10));

        booksButton = new JButton(Localization.get("button.books"));
        booksButton.addActionListener(e -> {});
        booksButton.setPreferredSize(new Dimension(300, 50));
        addBookButton = new JButton(Localization.get("button.book.add"));
        addBookButton.addActionListener(e -> {});
        addBookButton.setPreferredSize(new Dimension(50, 20));
        addBookButton.setForeground(new Color(0,153,76));
        addBookButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        editBookButton = new JButton(Localization.get("button.book.edit"));
        editBookButton.addActionListener(e -> {});
        editBookButton.setPreferredSize(new Dimension(150, 20));

        borrowersButton = new JButton(Localization.get("button.borrowers"));
        borrowersButton.addActionListener(e -> {});
        borrowersButton.setPreferredSize(new Dimension(300, 50));
        addBorrowerButton = new JButton(Localization.get("button.borrower.add"));
        addBorrowerButton.addActionListener(e -> {});
        addBorrowerButton.setPreferredSize(new Dimension(50, 20));
        addBorrowerButton.setForeground(new Color(0,153,76));
        addBorrowerButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        editBorrowerButton = new JButton(Localization.get("button.borrower.edit"));
        editBorrowerButton.addActionListener(e -> {});
        editBorrowerButton.setPreferredSize(new Dimension(150, 20));

        loansButton = new JButton(Localization.get("button.loans"));
        loansButton.addActionListener(e -> {});
        loansButton.setPreferredSize(new Dimension(300, 50));
        addLoanButton = new JButton(Localization.get("button.loan.add"));
        addLoanButton.addActionListener(e -> {});
        addLoanButton.setPreferredSize(new Dimension(50, 20));
        addLoanButton.setForeground(new Color(0,153,76));
        addLoanButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        editLoanButton = new JButton(Localization.get("button.loan.edit"));
        editLoanButton.addActionListener(e -> {});
        editLoanButton.setPreferredSize(new Dimension(150, 20));

        managementButton = new JButton(Localization.get("button.management"));
        managementButton.addActionListener(e -> {
            ManagementWindow window = new ManagementWindow();
            window.setVisible(true);
        });
        managementButton.setPreferredSize(new Dimension(500, 50));

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
        southPanel.add(borrowersButton, BorderLayout.WEST);
        southPanel.add(addBorrowerButton, BorderLayout.CENTER);
        southPanel.add(editBorrowerButton, BorderLayout.EAST);

        managementPanel = new JPanel();
        managementPanel.setLayout(new BorderLayout());
        managementPanel.add(managementButton, BorderLayout.SOUTH);


        toolbar = new ToolBar(true);
        add(toolbar);
        add(northPanel);
        add(centerPanel);
        add(southPanel);
        add(managementPanel);


        setSize(600, 300);
        setLocationRelativeTo(null);
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        booksButton.setText(Localization.get("button.books"));
        loansButton.setText(Localization.get("button.loans"));
        borrowersButton.setText(Localization.get("button.borrowers"));
        managementButton.setText(Localization.get("button.management"));
        addBookButton.setText(Localization.get("button.book.add"));
        editBookButton.setText(Localization.get("button.book.edit"));
        addBorrowerButton.setText(Localization.get("button.borrower.add"));
        editBorrowerButton.setText(Localization.get("button.borrower.edit"));
        addLoanButton.setText(Localization.get("button.loan.add"));
        editLoanButton.setText(Localization.get("button.loan.edit"));
        revalidate();
        repaint();
    }
}

