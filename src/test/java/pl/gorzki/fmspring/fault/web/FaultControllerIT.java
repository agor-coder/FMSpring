package pl.gorzki.fmspring.fault.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.CreateAreaCommand;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.AssignFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.UpdateFaultCommand;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;


@SpringBootTest
@AutoConfigureTestDatabase// with h2
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class FaultControllerIT {

    @TestConfiguration
    static class testConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    ManipulateFaultUseCase manipulateFaultService;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserJpaRepository userRepository;
    @Autowired
    FaultController controller;


    @BeforeEach
    private void initData() {
        TechArea area1 = areaService.addArea(new CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = userRepository.save(new UserEntity(
                "123", "Peter", "Novak", "12345", "notif1@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = userRepository.save(new UserEntity(
                "123", "Peter", "Smith", "12345", "notif2@2.pl", "ROLE_NOTIFIER"
        ));

        UserEntity spec = userRepository.save(new UserEntity(
                "123", "John", "Spec", "12345", "spec@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity assigner = userRepository.save(new UserEntity(
                "123", "Mike", "Assign", "12345", "assigner@2.pl", "ROLE_ASSIGNER"
        ));


        manipulateFaultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        manipulateFaultService.addFault(new CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));

        manipulateFaultService.assignFault(new AssignFaultCommand(
                5L, spec.getId(), assigner.getId()));
    }


    @Test
    public void findAllFaultsByUser() {
//        given
        User notif2 = new User("notif2@2.pl", "123", Set.of(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_NOTIFIER";
            }
        }));
        User spec = new User("spec@2.pl", "123", Set.of());
        User assigner = new User("assigner@2.pl", "123", Set.of());

//        when
        List<Fault> byNotifier = controller.getAllMyFaults(notif2);
        List<Fault> bySpec = controller.getAllMyFaults(spec);
        List<Fault> byAssigner = controller.getAllMyFaults(assigner);
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