package cz.upce.fei.bookdb_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BookdbBackendApplication {

	// TODO: Readme.md - napsat do něj i jak se projekt spouští
	// TODO: Jednotkové testy

	public static void main(String[] args) {
		SpringApplication.run(BookdbBackendApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
