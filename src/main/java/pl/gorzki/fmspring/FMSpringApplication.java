package pl.gorzki.fmspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.gorzki.fmspring.fault.application.FaultProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(FaultProperties.class)
public class FMSpringApplication {


    public static void main(String[] args) {
        SpringApplication.run(FMSpringApplication.class, args);
    }

}
