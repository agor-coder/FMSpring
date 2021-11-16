package pl.gorzki.fmspring.fault.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.db.AreaJpaRepository;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.UpdateFaultCommand;
import pl.gorzki.fmspring.fault.db.FaultJpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ManipulateFaultServiceTest {


    @Autowired
    AreaJpaRepository areaJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    FaultJpaRepository faultJpaRepository;
    @Autowired
    ManipulateFaultUseCase manipulateFaultService;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserUseCase userService;

    @BeforeEach
//    private void initData() {
//        TechArea area1 = areaJpaRepository.save(new TechArea("maszynownia"));
//        TechArea area2 = areaJpaRepository.save(new TechArea("elektr"));
//        TechArea area3 = areaJpaRepository.save(new TechArea("kotlownia"));
//
//        UserEntity notifier1 = userJpaRepository.save(new UserEntity(
//                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
//        ));
//        UserEntity notifier2 = userJpaRepository.save(new UserEntity(
//                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
//        ));
//
//        UserEntity spec = userJpaRepository.save(new UserEntity(
//                "123", "John", "Spec", "12345", "spec@2.pl", "ROLE_SPECIALIST"
//        ));
//        UserEntity assigner = userJpaRepository.save(new UserEntity(
//                "123", "Mike", "Assign", "12345", "assigner@2.pl", "ROLE_ASSIGNER"
//        ));
//
//
//        faultJpaRepository.save(new Fault("zwarcie", area1, notifier1));
//        faultJpaRepository.save(new Fault("brak", area2, notifier2));
//        faultJpaRepository.save(new Fault("nie ma", area1, notifier1));
//        faultJpaRepository.save(new Fault("spalony", area2, notifier2));
//        faultJpaRepository.save(new Fault("NOWA", area3, notifier2));
//
//        manipulateFaultService.updateFault(new UpdateFaultCommand(
//                5L, null, ASSIGNED, null, spec.getId(), assigner.getId(), null));
//    }
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

        manipulateFaultService.updateFault(new UpdateFaultCommand(
                5L, null, ASSIGNED, null, spec.getId(), assigner.getId(), null));
    }

    @Test
    void addFault() {
    }

    @Test
    void removeFaultById() {
        //given
        initData();
        //when
        UpdateResponse response = manipulateFaultService.removeFaultById(1L);
        //then
        assertTrue(response.success());
    }

    @Test
    void updateFault() {
    }

    @Test
    void assignFault() {
    }

    @Test
    void changeStatus() {
    }
}