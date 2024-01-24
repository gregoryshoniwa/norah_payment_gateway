package dev.grebles.norah;

import dev.grebles.norah.entities.User;
import dev.grebles.norah.enums.Role;
import dev.grebles.norah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
public class NorahApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(NorahApplication.class, args);
	}

	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findById(1L).orElse(null);
		if (adminAccount == null) {
			User user = new User();
			user.setFirstName("admin");
			user.setLastName("admin");
			user.setEmail("admin@norah.co.zw");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setPrimaryUser(0L);
			user.setCompanyName("Norah Payment Gateway");
			user.setActivated(true);
			userRepository.save(user);
			System.out.println("User Saved Successfully");
		}

	}

}
