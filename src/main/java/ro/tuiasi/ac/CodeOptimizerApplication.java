package ro.tuiasi.ac;

import javax.swing.SwingUtilities;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import ro.tuiasi.ac.gui.MainWindow;

/**
 * The main entry point for the Code Optimizer application.
 * <p>
 * This class bootstraps the Spring Boot application context and subsequently launches 
 * the Swing-based graphical user interface. It explicitly disables Spring Boot's default 
 * headless mode to allow the rendering and display of Java Swing UI components.
 * </p>
 */
@SpringBootApplication
public class CodeOptimizerApplication {

    /**
     * The main method that starts the Spring Boot application and initializes the GUI.
     * <p>
     * It configures a {@link SpringApplicationBuilder} with {@code headless(false)} to ensure
     * desktop environment compatibility. After the Spring context is successfully started, 
     * it schedules the initialization and rendering of the {@link MainWindow} on the 
     * Swing Event Dispatch Thread (EDT) to ensure thread safety for UI operations.
     * </p>
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CodeOptimizerApplication.class).headless(false);

        builder.run(args);

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}