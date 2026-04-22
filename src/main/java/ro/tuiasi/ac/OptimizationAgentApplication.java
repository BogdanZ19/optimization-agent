package ro.tuiasi.ac;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class OptimizationAgentApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(OptimizationAgentApplication.class, args);
//	}
//
//}


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OptimizationAgentApplication {
    public static void main(String[] args) {
      SpringApplication.run(OptimizationAgentApplication.class, args);
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }
}


