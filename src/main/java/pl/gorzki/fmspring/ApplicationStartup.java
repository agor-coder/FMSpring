package pl.gorzki.fmspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

import java.util.List;

import static pl.gorzki.fmspring.area.application.port.AreaUseCase.*;
import static pl.gorzki.fmspring.fault.application.port.FaultUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final FaultUseCase faultService;
    private final AreaUseCase areaService;

    public ApplicationStartup(FaultUseCase faultService, AreaUseCase areaService) {
        this.faultService = faultService;
        this.areaService = areaService;
    }

    @Override
    public void run(String... args) {
//     initData();
        findAllFaults();
        findByDescr();
        System.out.println("update_start");
        findAndUpdate();
        findByDescr();
    }


    private void initData() {

        TechArea area1 = areaService.addArea(new CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new CreateAreaCommand("kotlownia"));

        faultService.addFault(new CreateFaultCommand("zwarcie", area1, null, null, null));
        faultService.addFault(new CreateFaultCommand("brak", area2, null, null, null));
        faultService.addFault(new CreateFaultCommand("nie ma", area1, null, null, null));
        faultService.addFault(new CreateFaultCommand("spalony", area2, null, null, null));
        faultService.addFault(new CreateFaultCommand("NOWA", area3, null, null, null));


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
        faultService.findOneByDescription("z")
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
