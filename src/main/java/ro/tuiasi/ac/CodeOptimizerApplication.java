package ro.tuiasi.ac;

import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ro.tuiasi.ac.gui.MainWindow;

@SpringBootApplication
public class CodeOptimizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeOptimizerApplication.class, args);

		SwingUtilities.invokeLater(() -> {
			new MainWindow().setVisible(true);
		});
	}

}
