package ch.csbe.vokabelapp;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.csbe.vokabelapp.ui.*;

/**
 * @author Julian
 *
 */

public class Main {

    public static void main(String[] args) {

        try {
            // sets the UIManager to the system default of the current operating system
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Initializes the first window and makes it visible.
        MainWindow mainWindow = MainWindow.getInstance();
        mainWindow.setVisible(true);
    }

}
