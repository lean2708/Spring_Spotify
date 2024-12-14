package spotify.spring_spotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringSpotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSpotifyApplication.class, args);
	}

}
