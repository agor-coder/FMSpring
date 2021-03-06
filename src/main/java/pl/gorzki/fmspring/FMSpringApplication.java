package pl.gorzki.fmspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.gorzki.fmspring.fault.application.FaultProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(FaultProperties.class)
public class FMSpringApplication {


    public static void main(String[] args) {
        SpringApplication.run(FMSpringApplication.class, args);
    }

}
