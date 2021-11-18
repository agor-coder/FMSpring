package pl.gorzki.fmspring.fault.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.clock.Clock;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;


@SpringBootTest(properties = "fmspring.faults.abandon-period=1H")
@AutoConfigureTestDatabase
class AbandonedFaultJobTest {

    @TestConfiguration
    static class testConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }

    @Autowired
    AbandonedFaultJob faultJob;
    @Autowired
    ManipulateFaultUseCase manipulateFaultService;
    @Autowired
    QueryFaultService queryFaultService;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserUseCase userService;
    @Autowired
    Clock.Fake clock;

    @Test
    public void shouldMarkFaultAsAbandoned() {
        //given

        initData();
        //when
        clock.tick(Duration.ofHours(2));
        faultJob.run();
        //then
        assertEquals(4, queryFaultService.findByStatus("porzucona").size());


    }

    private void initData() {
        TechArea area1 = areaService.addArea(new AreaUseCase.CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new AreaUseCase.CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new AreaUseCase.CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = userService.register(new UserUseCase.CreateUserCommand(
                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = userService.register(new UserUseCase.CreateUserCommand(
                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
        ));

        UserEntity spec = userService.register(new UserUseCase.CreateUserCommand(
                "123", "John", "Spec", "12345", "spec@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity assigner = userService.register(new UserUseCase.CreateUserCommand(
                "123", "Mike", "Assign", "12345", "assigner@2.pl", "ROLE_ASSIGNER"
        ));


        manipulateFaultService.addFault(new ManipulateFaultUseCase.CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new ManipulateFaultUseCase.CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new ManipulateFaultUseCase.CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new ManipulateFaultUseCase.CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new ManipulateFaultUseCase.CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));

        manipulateFaultService.updateFault(new ManipulateFaultUseCase.UpdateFaultCommand(
                5L, null, ASSIGNED, null, spec.getId(), assigner.getId(), null));
    }

}