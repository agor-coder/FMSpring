package pl.gorzki.fmspring.initialization.application;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.initialization.application.port.InitServiceUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@AllArgsConstructor
public class InitService implements InitServiceUseCase {
    private final QueryFaultUseCase queryFaultService;
    private final ManipulateFaultUseCase manipulateFaultService;
    private final AreaUseCase areaService;
    private final UserUseCase registrationService;


    @Override
    @Transactional
    public void initialize() {
        initData();
    }

    private void initData() {
        ClassPathResource classPathResource = new ClassPathResource("users.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))) {
            CsvToBean<CsvUser> build = new CsvToBeanBuilder<CsvUser>(reader)
                    .withType(CsvUser.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initUser);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initUser(CsvUser csvUser) {
        CreateUserCommand command = new CreateUserCommand(
                csvUser.password,
                csvUser.firstName,
                csvUser.lastName,
                csvUser.phone,
                csvUser.emailUserName,
                csvUser.role
        );
        registrationService.register(command);
    }

//    private void initData() {
//
//        TechArea area1 = areaService.addArea(new AreaUseCase.CreateAreaCommand("maszynownia"));
//        TechArea area2 = areaService.addArea(new AreaUseCase.CreateAreaCommand("elektryczny"));
//        TechArea area3 = areaService.addArea(new AreaUseCase.CreateAreaCommand("kotlownia"));
//
//        UserEntity notifier1 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "Peter", "Novak", "12345", "peter@2.pl", "ROLE_NOTIFIER"
//        ));
//        UserEntity notifier2 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "Peter", "Smith", "12345", "peter2@2.pl", "ROLE_NOTIFIER"
//        ));
//        UserEntity assigner1 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "Andy", "Gor", "12345", "andy@2.pl", "ROLE_ASSIGNER"
//        ));
//        UserEntity assigner2 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "Tom", "Man", "12345", "aman@2.pl", "ROLE_ASSIGNER"
//        ));
//        UserEntity specialist1 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "John", "Baker", "12345", "john@2.pl", "ROLE_SPECIALIST"
//        ));
//        UserEntity specialist2 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "Stan", "Pure", "12345", "stan@2.pl", "ROLE_SPECIALIST"
//        ));
//        UserEntity admin1 = registrationService.register(new UserUseCase.CreateUserCommand(
//                "123", "Kay", "Lenz", "12345", "kay@2.pl", "ROLE_ADMIN"
//        ));
//
//        manipulateFaultService.addFault(new CreateFaultCommand("zwarcie", area1.getId(), notifier1.getId()));
//        manipulateFaultService.addFault(new CreateFaultCommand("brak", area2.getId(), notifier2.getId()));
//        manipulateFaultService.addFault(new CreateFaultCommand("nie ma", area1.getId(), notifier1.getId()));
//        manipulateFaultService.addFault(new CreateFaultCommand("spalony", area2.getId(), notifier2.getId()));
//        manipulateFaultService.addFault(new CreateFaultCommand("NOWA", area3.getId(), notifier2.getId()));
//
//        queryFaultService.fidById(2L).ifPresent(fault -> {
//            fault.setSpecialist(specialist2);
//            fault.setWhoAssigned(assigner2);
//            fault.setStatus(ASSIGNED);
//        });
//
//
//    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvUser {
        @CsvBindByName
        String password;
        @CsvBindByName
        String firstName;
        @CsvBindByName
        String lastName;
        @CsvBindByName
        String phone;
        @CsvBindByName
        String emailUserName;
        @CsvBindByName
        String role;
    }
}
