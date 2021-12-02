package pl.gorzki.fmspring.fault.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.AppResponse;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.AssignFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.UpdateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import static org.junit.jupiter.api.Assertions.*;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ManipulateFaultServiceTest {


    @Autowired
    ManipulateFaultUseCase manipulateFaultService;
    @Autowired
    QueryFaultUseCase queryFaultService;
    @Autowired
    AreaUseCase areaService;
    @Autowired
    UserJpaRepository userRepository;


    @BeforeEach
    private void initData() {
        TechArea area1 = areaService.addArea(new AreaUseCase.CreateAreaCommand("maszynownia"));
        TechArea area2 = areaService.addArea(new AreaUseCase.CreateAreaCommand("elektr"));
        TechArea area3 = areaService.addArea(new AreaUseCase.CreateAreaCommand("kotlownia"));

        UserEntity notifier1 = userRepository.save(new UserEntity(
                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
        ));
        UserEntity notifier2 = userRepository.save(new UserEntity(
                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
        ));

        UserEntity spec = userRepository.save(new UserEntity(
                "123", "John", "Spec", "12345", "spec@2.pl", "ROLE_SPECIALIST"
        ));
        UserEntity spec2 = userRepository.save(new UserEntity(
                "123", "Jake", "Spec2", "12345", "spec2@2.pl", "ROLE_SPECIALIST"
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
        manipulateFaultService.assignFault(new AssignFaultCommand(
                4L, spec.getId(), assigner.getId()));
    }

    @Test
    public void removeFault() {
        AppResponse response = manipulateFaultService.removeFaultById(1L);
        assertTrue(response.isSuccess());
        assertEquals(4, queryFaultService.findAll().size());
    }

    @Test
    public void removeNotFoundFault() {
        //given

        //when
        AppResponse response = manipulateFaultService.removeFaultById(7L);
        //then
        assertTrue(response.getErrors().get(0).contains("Fault not found"));
        assertFalse(response.isSuccess());
    }

    @Test
    public void CannotRemoveAssignedFault() {
        //given

        //when
        AppResponse response = manipulateFaultService.removeFaultById(5L);
        //then
        assertTrue(response.getErrors().get(0).contains("Unable to remove assigned fault"));
        assertFalse(response.isSuccess());
    }

    @Test
    public void updateFault() {
        //when
        AppResponse response = manipulateFaultService.updateFault(new UpdateFaultCommand(
                2L, "update", null));
        //then
        assertTrue(response.isSuccess());
        assertTrue(queryFaultService.findOneByDescription("update").isPresent());

    }

    @Test
    public void updateFaultWithAreaNotFound() {
        //when
        UpdateFaultCommand command = new UpdateFaultCommand(
                2L, "update", 4L);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> manipulateFaultService.updateFault(command));
        //then
        assertEquals("Cannot find such area", exception.getMessage());
    }

    @Test
    public void assignFaultWithTheSameSpecialist() {
        //when
        AppResponse response = manipulateFaultService.assignFault(new AssignFaultCommand(5L, 3L, 5L));
        //then
        assertFalse(response.isSuccess());
        assertEquals("Specialist - the same", response.getErrors().get(0));

    }

    @Test
    public void assignFaultWithSpecLimit() {
        //when
        AppResponse response = manipulateFaultService.assignFault(new AssignFaultCommand(3L, 3L, 5L));
        //then
        assertFalse(response.isSuccess());
        assertEquals("Specialist - fault limit reached", response.getErrors().get(0));

    }

    @Test
    public void assignFault() {
        //when
        AppResponse response = manipulateFaultService.assignFault(new AssignFaultCommand(1L, 4L, 5L));
        Fault fault = queryFaultService.findById(1L).get();

        //then
        assertTrue(response.isSuccess());
        assertEquals("spec2@2.pl", fault.getSpecialist().getEmailUserName());
        assertEquals("assigner@2.pl", fault.getWhoAssigned().getEmailUserName());

    }


}