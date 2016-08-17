package lfkimura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main class where start springboot application container
 * @author Luis Kimura
 *
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
