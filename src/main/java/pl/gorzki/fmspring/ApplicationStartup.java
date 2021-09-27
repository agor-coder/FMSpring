package pl.gorzki.fmspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

import java.util.List;

import static pl.gorzki.fmspring.fault.application.port.FaultUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final FaultUseCase faultService;

    public ApplicationStartup(FaultUseCase faultService) {
        this.faultService = faultService;
    }

    @Override
    public void run(String... args) {
        initData();
        findAllFaults();
        findByDescr();
        System.out.println("update_start");
        findAndUpdate();
        findByDescr();
    }


    private void initData() {
        faultService.addFault(new CreateFaultCommand("zwarcie"));
        faultService.addFault(new CreateFaultCommand("brak"));
        faultService.addFault(new CreateFaultCommand("nie ma"));
        faultService.addFault(new CreateFaultCommand("spalony"));

    }

    private void findByDescr() {
        List<Fault> faultByDesc = faultService.findByDescription("z");
        faultByDesc.forEach(System.out::println);
    }

    private void findAllFaults() {
        List<Fault> allFaults = faultService.findAll();
        allFaults.forEach(System.out::println);
        System.out.println();
    }

    private void findAndUpdate() {
        faultService.findOneByDesription("z")
                .ifPresent(fault -> {
                    UpdateFaultCommand command = UpdateFaultCommand.builder()
                            .id(fault.getId())
                            .faultDescribe("zwarcie_update")
                            .status(FaultStatus.ASSIGNED)
                            .build();
                    UpdateFaultResponse response = faultService.updateFault(command);
                    System.out.println("Update result: " + response.isSuccess());
                    System.out.println(response.getErrors());

                });
    }
}
