package pl.gorzki.fmspring.fault.web;

import org.junit.jupiter.api.BeforeEach;
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
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;


@SpringBootTest
@AutoConfigureTestDatabase// with h2
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FaultControllerIT {
    @Autowired
    ManipulateFaultUseCase manipulateFaultService;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserUseCase userService;
    @Autowired
    FaultController controller;



    @BeforeEach
    private void initData() {
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


        manipulateFaultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));

        manipulateFaultService.updateFault(new UpdateFaultCommand(
                5L, null, ASSIGNED, null, spec.getId(), assigner.getId(), null));
    }



    @Test
    public void findAllFaultsByUser() {
//        given


//        when
        List<Fault> byNotifier = controller.getAllByUser(2L);
        List<Fault> bySpec = controller.getAllByUser(3L);
        List<Fault> byAssigner = controller.getAllByUser(4L);
//        then
        assertEquals(3, byNotifier.size());
        assertEquals(1, bySpec.size());
        assertEquals(1, byAssigner.size());
    }

    @Test
    public void findAllFaults() {
//        given

//        when
        List<Fault> list1 = controller.getAll(Optional.empty(), Optional.empty());
//        then
        assertEquals(5, list1.size());
    }
}