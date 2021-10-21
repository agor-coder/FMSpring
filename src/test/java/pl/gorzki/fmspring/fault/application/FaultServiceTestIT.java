package pl.gorzki.fmspring.fault.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.db.UserJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



//////////////na aktywnej bazie
@SpringBootTest
class FaultServiceTestIT {

    @Autowired
    FaultUseCase service;
    @Autowired
    UserJpaRepository userJpaRepository;


    @Test
    public void findAllByUser() {
//        given

//        when
        List<Fault> list1=service.findAllByUser(userJpaRepository.findById(4L).get());
//        then
        System.out.println(list1);
        assertEquals(2,list1.size());
    }

}