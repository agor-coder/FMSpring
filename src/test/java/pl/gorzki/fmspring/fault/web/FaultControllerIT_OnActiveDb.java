package pl.gorzki.fmspring.fault.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


//////////////na aktywnej bazie
@SpringBootTest
@ActiveProfiles("test")
class FaultControllerIT_OnActiveDb {

    @TestConfiguration
    static class testConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    FaultController controller;


//    @Test
//    public void findAllByUser() {
////        given
//        User user = new User("notif1@2.pl", "123", Set.of());
////        when
//                List < Fault > list1 = controller.getAllMyFaults(user);
////        then
//        System.out.println(list1);
//        assertEquals(4, list1.size());
//    }

}