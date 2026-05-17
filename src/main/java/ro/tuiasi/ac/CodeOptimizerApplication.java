package ro.tuiasi.ac;

import javax.swing.SwingUtilities;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import ro.tuiasi.ac.gui.MainWindow;

@SpringBootApplication
public class CodeOptimizerApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CodeOptimizerApplication.class).headless(false);

        builder.run(args);

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}