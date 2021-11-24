package pl.gorzki.fmspring.fault.web;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.domain.TechArea;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AreaControllerAPITest_LinkedHashMAp {

    @LocalServerPort
    private int port;
    @MockBean
    AreaUseCase areaService;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllAreas() {

        //given
        TechArea area = new TechArea("maszynownia");
        TechArea area2 = new TechArea("maszynownia2");

        when(areaService.findAll()).thenReturn(List.of(area, area2));

        // when
        String url = "http://localhost:" + port + "/areas/getAllTest";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);// LinkedHashMAp
        //then

        assertEquals(2, response.getBody().size());

    }

}