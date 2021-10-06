package pl.gorzki.fmspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.gorzki.fmspring.fmModel.application.port.AreaUseCase;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase;
import pl.gorzki.fmspring.fmModel.domain.Fault;
import pl.gorzki.fmspring.fmModel.domain.FaultStatus;

import java.util.List;

import static pl.gorzki.fmspring.fmModel.application.port.AreaUseCase.*;
import static pl.gorzki.fmspring.fmModel.application.port.FaultUseCase.*;

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
        initData();
        findAllFaults();
        findByDescr();
        System.out.println("update_start");
        findAndUpdate();
        findByDescr();
    }


    private void initData() {
        faultService.addFault(new CreateFaultCommand("zwarcie",null,null,null,null));
        faultService.addFault(new CreateFaultCommand("brak",null,null,null,null));
        faultService.addFault(new CreateFaultCommand("nie ma",null,null,null,null));
        faultService.addFault(new CreateFaultCommand("spalony",null,null,null,null));
        faultService.addFault(new CreateFaultCommand("NOWA",null,null,null,null));

        areaService.addArea(new CreateAreaCommand("maszynownia"));
        areaService.addArea(new CreateAreaCommand("elektr"));
        areaService.addArea(new CreateAreaCommand("kotlownia"));

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
