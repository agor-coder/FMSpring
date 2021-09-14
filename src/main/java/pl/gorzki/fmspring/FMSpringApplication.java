package pl.gorzki.fmspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.gorzki.fmspring.fault.application.FaultController;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;

@SpringBootApplication
public class FMSpringApplication {


    public static void main(String[] args) {
        SpringApplication.run(FMSpringApplication.class, args);
    }

}
