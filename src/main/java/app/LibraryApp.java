package app;

import app.dialogs.BorrowerDialog;
import app.dialogs.EmployeeDialog;
import app.dialogs.StartDialog;
import domain.Borrower;
import management.DataBaseBorrowers;

import javax.swing.*;

/**
 * Główna klasa uruchamiająca aplikację biblioteczną.
 */
public class LibraryApp {

    public void startApp(){
        SwingUtilities.invokeLater(() -> {
            StartDialog start = new StartDialog(null);
            UserType result = start.showDialog();

            if (result == UserType.BORROWER) {
                BorrowerDialog b = new BorrowerDialog(null);
                String cardStr = b.showDialog();
                if (cardStr == null || cardStr.isEmpty()) return;
                try {
                    int cardNumber = Integer.parseInt(cardStr);
                    DataBaseBorrowers dbBorrowers = new DataBaseBorrowers();
                    Borrower borrower = dbBorrowers.getBorrowerByCardNumber(cardNumber);
                    if (borrower == null) {
                        JOptionPane.showMessageDialog(null,
                            Localization.get("error.login.card.notFound") + " " + cardNumber,
                            Localization.get("error.login.title"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    MainWindowBorrower window = new MainWindowBorrower(borrower);
                    window.setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        Localization.get("error.login.card.notNumber"),
                        Localization.get("error.login.title"), JOptionPane.ERROR_MESSAGE);
                }
            }

            if (result == UserType.EMPLOYEE) {
                EmployeeDialog e = new EmployeeDialog(null);
                String code = e.showDialog();
                if (code == null) return;
                else if (code.equals("")) {
                    MainWindowEmployee window = new MainWindowEmployee();
                    window.setVisible(true);
                }
                else return;
            }

            if (result == UserType.CANCEL) {
                return;
            }


        });
    }

    public static void main(String[] args) {
        new LibraryApp().startApp();
    }
}
