package pl.gorzki.fmspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.gorzki.fmspring.fault.application.FaultController;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final FaultController faultController;

    public ApplicationStartup(FaultController faultController) {
        this.faultController = faultController;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Fault> faults = faultController.findAll();
        faults.forEach(System.out::println);
        System.out.println();

        List<Fault> faultByDesc = faultController.finByDesription("z");
        faultByDesc.forEach(System.out::println);

    }
}
