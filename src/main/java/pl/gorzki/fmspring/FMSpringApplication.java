package pl.gorzki.fmspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class FMSpringApplication implements CommandLineRunner {

	public FMSpringApplication(FaultService service) {
		this.service = service;
	}

	public static void main(String[] args) {
        SpringApplication.run(FMSpringApplication.class, args);
    }


	private  final  FaultService service;


    @Override
    public void run(String... args) throws Exception {
//        FaultService service = new FaultService();
        List<Fault> faults = service.findAll();
        faults.forEach(System.out::println);
    }
}
