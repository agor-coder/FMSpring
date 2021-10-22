package pl.gorzki.fmspring.fault.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase// with h2
class FaultServiceTest {
    @Autowired
    FaultUseCase faultService;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserUseCase registrationService;


    @Test
    public void findAllByUser() {
//        given
        TechArea area1 = areaService.addArea(new AreaUseCase.CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new AreaUseCase.CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new AreaUseCase.CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = registrationService.register(new UserUseCase.CreateUserCommand(
                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
        ));


        faultService.addFault(new FaultUseCase.CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
        faultService.addFault(new FaultUseCase.CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
        faultService.addFault(new FaultUseCase.CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
        faultService.addFault(new FaultUseCase.CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
        faultService.addFault(new FaultUseCase.CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));
//        when
        List<Fault> list1 = faultService.findAllByUser(userJpaRepository.findById(2L).get());
//        then
        System.out.println(list1);
        assertEquals(3,list1.size());
    }
}