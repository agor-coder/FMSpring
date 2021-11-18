package pl.gorzki.fmspring.fault.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class FaultControllerAPITest_List {

    @LocalServerPort
    private int port;
    @MockBean
    QueryFaultUseCase queryService;
    @MockBean
    ManipulateFaultUseCase manipulateService;
    @MockBean
    UserUseCase userUseCase;

    @Autowired
    TestRestTemplate restTemplate;


    @Test
    public void getAllFaults() {

        //given
        TechArea area = new TechArea("maszynownia");
        UserEntity notifier = new UserEntity("12", "a", "b", "123", "aa@2.pl", "ROLE_NOTIFIER");
        Fault fault1 = new Fault("brak", area, notifier);
        Fault fault2 = new Fault("niski", area, notifier);
        when(queryService.findAll()).thenReturn(List.of(fault1, fault2));
        ParameterizedTypeReference<List<Fault>> type = new ParameterizedTypeReference<>() {
        };

        // when
        String url = "http://localhost:" + port + "/faults";
        RequestEntity<Void> request = RequestEntity.get(URI.create(url)).build();
        ResponseEntity<List<Fault>> response = restTemplate.exchange(request, type);// List
        //then

        assertEquals(2, response.getBody().size());

    }

}