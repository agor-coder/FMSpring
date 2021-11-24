package pl.gorzki.fmspring.fault.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.area.web.AreaController;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({AreaController.class, FaultController.class})
@ActiveProfiles("test")
@WithMockUser
class AreaFaultControllerWebTest {

    @MockBean
    QueryFaultUseCase queryService;
    @MockBean
    ManipulateFaultUseCase manipulateService;
    @MockBean
    UserUseCase userService;
    @MockBean
    AreaUseCase areaService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getAllAreas() throws Exception {

        //given
        TechArea area = new TechArea("maszynownia");
        TechArea area2 = new TechArea("maszynownia2");
        when(areaService.findAll()).thenReturn(List.of(area, area2));

        //        expect
        mockMvc.perform(get("/areas/getAllTest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));

    }
    @Test
    public void getAllFaults() throws Exception {

        //given
        TechArea area = new TechArea("maszynownia");
        UserEntity notifier = new UserEntity("12", "a", "b", "123", "aa@2.pl", "ROLE_NOTIFIER");
        Fault fault1 = new Fault("brak", area, notifier);
        Fault fault2 = new Fault("niski", area, notifier);
        when(queryService.findAll()).thenReturn(List.of(fault1, fault2));

        //        expect
        mockMvc.perform(get("/faults"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));

    }

}