package pl.gorzki.fmspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FMSpringApplication {


    public static void main(String[] args) {
        SpringApplication.run(FMSpringApplication.class, args);
    }

}
