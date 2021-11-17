package pl.gorzki.fmspring.fault.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FaultController.class})
class FaultControllerTest_Mock {


    @MockBean
    QueryFaultUseCase queryService;
    @MockBean
    ManipulateFaultUseCase manipulateService;
    @MockBean
    UserUseCase userUseCase;
    @Autowired
    FaultController controller;

    @Test
    public void getAllFaults() {

        //given
        TechArea area = new TechArea("maszynownia");
        UserEntity notifier = new UserEntity("12", "a", "b", "123", "aa@2.pl", "ROLE_NOTIFIER");
        Fault fault1 = new Fault("brak", area, notifier);
        Fault fault2 = new Fault("niski", area, notifier);
        when(queryService.findAll()).thenReturn(List.of(fault1, fault2));

        // when
        List<Fault> faults = controller.getAll(Optional.empty(), Optional.empty());
        //then

        assertEquals(2, faults.size());

    }
}