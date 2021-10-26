package pl.gorzki.fmspring.initialization.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.initialization.application.port.InitServiceUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.domain.UserEntity;

import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;

@Service
@AllArgsConstructor
public class InitService implements InitServiceUseCase {
    private final QueryFaultUseCase queryFaultService;
    private final ManipulateFaultUseCase manipulateFaultService;
    private final AreaUseCase areaService;
    private final UserUseCase registrationService;


    @Override
    public void initialize() {
        initData();
    }

    private void initData() {

        TechArea area1 = areaService.addArea(new AreaUseCase.CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new AreaUseCase.CreateAreaCommand("elektryczny"));
        TechArea area3 = areaService.addArea(new AreaUseCase.CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity assigner1 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Andy", "Gor", "12345", "andy@2.pl", "ROLE_ASSIGNER"
        ));
        UserEntity assigner2 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Tom", "Man", "12345", "aman@2.pl", "ROLE_ASSIGNER"
        ));
        UserEntity specialist1 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "John", "Baker", "12345", "john@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity specialist2 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Stan", "Pure", "12345", "stan@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity admin1 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Kay", "Lenz", "12345", "kay@2.pl", "ROLE_ADMIN"
        ));

        manipulateFaultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));

        queryFaultService.fidById(2L).ifPresent(fault -> {
            fault.setSpecialist(specialist2);
            fault.setWhoAssigned(assigner2);
            fault.setStatus(ASSIGNED);
        });


    }
}
