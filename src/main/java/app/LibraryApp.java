package app;

import app.dialogs.BorrowerDialog;
import app.dialogs.EmployeeDialog;
import app.dialogs.StartDialog;

import javax.swing.*;

public class LibraryApp {

    public void startApp(){
        SwingUtilities.invokeLater(() -> {
            StartDialog start = new StartDialog(null);
            UserType result = start.showDialog();

            if (result == UserType.BORROWER) {
                BorrowerDialog b = new BorrowerDialog(null);
                String id = b.showDialog();
                if (id == null) return;
                if (id.length() == 8) {
                    MainWindowBorrower window = new MainWindowBorrower();
                    window.setVisible(true);
                };
            }

            if (result == UserType.EMPLOYEE) {
                EmployeeDialog e = new EmployeeDialog(null);
                String code = e.showDialog();
                if (code == null) return;
                else if (code.equals("15071410")) {
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
