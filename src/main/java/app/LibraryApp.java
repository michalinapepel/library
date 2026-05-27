package app;

import app.dialogs.BorrowerDialog;
import app.dialogs.EmployeeDialog;
import app.dialogs.StartDialog;

import javax.swing.*;

public class LibraryApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartDialog start = new StartDialog(null);
            UserType result = start.showDialog();

            if (result == UserType.BORROWER) {
                BorrowerDialog b = new BorrowerDialog(null);
                String id = b.showDialog();
                if (id == null) return; // anulowano
            }

            if (result == UserType.EMPLOYEE) {
                EmployeeDialog e = new EmployeeDialog(null);
                String code = e.showDialog();
                if (code == null) return; // anulowano
                MainWindowEmployee window = new MainWindowEmployee();
                window.setVisible(true);
            }

            if (result == UserType.CANCEL) {
                return;
            }


        });
    }
}
