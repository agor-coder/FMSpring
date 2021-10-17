package pl.gorzki.fmspring;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;

import static pl.gorzki.fmspring.area.application.port.AreaUseCase.*;
import static pl.gorzki.fmspring.fault.application.port.FaultUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final FaultUseCase faultService;
    private final AreaUseCase areaService;
    private final UserUseCase registrationService;



    @Override
    public void run(String... args) {
//   initData();
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

        UserEntity notifier1 = registrationService.register(new CreateUserCommand(
                "123","Peter", "Novak", "12345", "peter@2.pl","ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = registrationService.register(new CreateUserCommand(
                "123","Peter", "Smith", "12345", "peter2@2.pl","ROLE_NOTIFIER"
        ));
        UserEntity assigner1 = registrationService.register(new CreateUserCommand(
                "123","Andy", "Gor", "12345", "andy@2.pl","ROLE_ASSIGNER"
        ));
        UserEntity specialist1 = registrationService.register(new CreateUserCommand(
                "123","John", "Baker", "12345", "john@2.pl","ROLE_SPECIALIST"
        ));
        UserEntity specialist2 = registrationService.register(new CreateUserCommand(
                "123","Stan", "Pure", "12345", "stan@2.pl","ROLE_SPECIALIST"
        ));
        UserEntity admin1 = registrationService.register(new CreateUserCommand(
                "123","Kay", "Lenz", "12345", "kay@2.pl","ROLE_ADMIN"
        ));

        faultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(),notifier1.getId()));
        faultService.addFault(new CreateFaultCommand("brak", area2.getId(),  notifier2.getId()));
        faultService.addFault(new CreateFaultCommand("nie ma", area1.getId(),  notifier1.getId()));
        faultService.addFault(new CreateFaultCommand("spalony", area2.getId(),  notifier2.getId()));
        faultService.addFault(new CreateFaultCommand("NOWA", area3.getId(),  notifier2.getId()));


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
                            .status(FaultStatus.END)
                            .build();
                    UpdateFaultResponse response = faultService.updateFault(command);
                    System.out.println("Update result: " + response.isSuccess());
                    System.out.println(response.getErrors());

                });
    }
}
