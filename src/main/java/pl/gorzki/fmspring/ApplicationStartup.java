package pl.gorzki.fmspring;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;

import static pl.gorzki.fmspring.fault.application.port.FaultUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final FaultUseCase faults;

    public ApplicationStartup(FaultUseCase faults) {
        this.faults = faults;
    }

    @Override
    public void run(String... args)  {
        initData();
        findAllFaults();
        findByDescr();
    }

    private void initData() {
        faults.addFault(new CreateFaultCommand("zwarcie"));
        faults.addFault(new CreateFaultCommand("brak"));
        faults.addFault(new CreateFaultCommand("nie ma"));
        faults.addFault(new CreateFaultCommand("spalony"));

    }

    private void findByDescr() {
        List<Fault> faultByDesc = faults.finByDesription("z");
        faultByDesc.forEach(System.out::println);
    }

    private void findAllFaults() {
        List<Fault> allFaults = faults.findAll();
        allFaults.forEach(System.out::println);
        System.out.println();
    }
}
