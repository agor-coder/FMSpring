package pl.gorzki.fmspring.fault.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.CreateAreaCommand;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.UpdateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;


@SpringBootTest
@AutoConfigureTestDatabase// with h2
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FaultServiceTest {
    @Autowired
    ManipulateFaultUseCase manipulateFaultService;
    @Autowired
    QueryFaultUseCase queryFaultService;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserUseCase userService;


    @Test
    public void findAllFaultsByUser() {
//        given
        TechArea area1 = areaService.addArea(new CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = userService.register(new CreateUserCommand(
                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = userService.register(new CreateUserCommand(
                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
        ));

        UserEntity spec = userService.register(new CreateUserCommand(
                "123", "John", "Spec", "12345", "spec@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity assigner = userService.register(new CreateUserCommand(
                "123", "Mike", "Assign", "12345", "assigner@2.pl", "ROLE_ASSIGNER"
        ));


        Fault fault1 = manipulateFaultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));

        manipulateFaultService.updateFault(new UpdateFaultCommand(
                fault1.getId(), null, ASSIGNED, null, spec.getId(), assigner.getId(), null));

//        when
        List<Fault> byNotifier = queryFaultService.findAllByUser(notifier2);
        List<Fault> bySpec = queryFaultService.findAllByUser(spec);
        List<Fault> byAssigner = queryFaultService.findAllByUser(assigner);
//        then
        assertEquals(3, byNotifier.size());
        assertEquals(1, bySpec.size());
        assertEquals(1, byAssigner.size());
    }

    @Test
    public void findAllFaults() {
//        given
        TechArea area1 = areaService.addArea(new CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = userService.register(new CreateUserCommand(
                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = userService.register(new CreateUserCommand(
                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
        ));

        UserEntity spec = userService.register(new CreateUserCommand(
                "123", "John", "Spec", "12345", "spec@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity assigner = userService.register(new CreateUserCommand(
                "123", "Mike", "Assign", "12345", "assigner@2.pl", "ROLE_ASSIGNER"
        ));


        Fault fault1 = manipulateFaultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));

        manipulateFaultService.updateFault(new UpdateFaultCommand(
                fault1.getId(), null, ASSIGNED, null, spec.getId(), assigner.getId(), null));
//        when
        List<Fault> list1 = queryFaultService.findAll();
//        then
        System.out.println(list1);
        assertEquals(5, list1.size());
    }
}