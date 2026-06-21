package app;

import java.awt.Window;

import javax.swing.JOptionPane;

public class Debug {
	public static final boolean ENABLED = true;

	public static void log(String message) {
		if (ENABLED) System.out.println("[DEBUG] " + message);
	}

	public static void error(String message, Exception e) {
		if (ENABLED) {
			System.out.println("[ERROR] " + message);
			e.printStackTrace();
		}
	}
	
	public static void showErrorWindow(String errorMessage) {
		 JOptionPane.showMessageDialog(
     	        null,
     	        errorMessage,
     	        "Błąd",
     	        JOptionPane.ERROR_MESSAGE
     	);
	}

}
