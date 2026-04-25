package ro.tuiasi.ac;

import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ro.tuiasi.ac.gui.MainWindow;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CodeOptimizerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CodeOptimizerApplication.class);
		
		app.setHeadless(false);
		
		ConfigurableApplicationContext context = app.run(args);

		SwingUtilities.invokeLater(() -> {
			MainWindow window = context.getBean(MainWindow.class);
			window.setVisible(true);
		});
	}

}
