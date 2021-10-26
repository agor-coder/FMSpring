package pl.gorzki.fmspring.fault.application;

import org.springframework.beans.factory.annotation.Autowired;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.db.UserJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



//////////////na aktywnej bazie
//@SpringBootTest
class FaultServiceTestIT_OnActiveDb {

    @Autowired
    QueryFaultUseCase service;
    @Autowired
    UserJpaRepository userJpaRepository;


//    @Test
    public void findAllByUser() {
//        given

//        when
        List<Fault> list1 = service.findAllByUser(userJpaRepository.findById(5L).get());
//        then
        System.out.println(list1);
        assertEquals(2,list1.size());
    }

}