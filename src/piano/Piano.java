package piano;

import javax.swing.SwingUtilities;

public class Piano {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
        });

        //MainWindow mainWindow = new MainWindow();
        //System.out.println("AAA");
    }
}
