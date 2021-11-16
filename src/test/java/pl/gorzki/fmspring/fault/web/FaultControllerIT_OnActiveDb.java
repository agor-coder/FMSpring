package pl.gorzki.fmspring.fault.web;

import org.springframework.beans.factory.annotation.Autowired;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



//////////////na aktywnej bazie
//@SpringBootTest
class FaultControllerIT_OnActiveDb {

    @Autowired
    FaultController controller;


//  @Test
    public void findAllByUser() {
//        given

//        when
        List<Fault> list1 = controller.getAllByUser(1L);
//        then
        System.out.println(list1);
        assertEquals(3,list1.size());
    }

}