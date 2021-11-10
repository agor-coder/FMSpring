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
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.initialization.application.port.InitServiceUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static pl.gorzki.fmspring.area.application.port.AreaUseCase.CreateAreaCommand;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;

@Service
@AllArgsConstructor
public class InitService implements InitServiceUseCase {
    private final QueryFaultUseCase queryFaultService;
    private final ManipulateFaultUseCase manipulateFaultService;
    private final AreaUseCase areaService;
    private final UserUseCase userService;


    @Override
    @Transactional
    public void initialize() {
        initUsers();
        initAreas();
        initFaults();
    }

    private void initUsers() {
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

    private void initAreas() {
        ClassPathResource classPathResource = new ClassPathResource("areas.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))) {
            CsvToBean<CsvArea> build = new CsvToBeanBuilder<CsvArea>(reader)
                    .withType(CsvArea.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initArea);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initFaults() {
        ClassPathResource classPathResource = new ClassPathResource("faults.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))) {
            CsvToBean<CsvFault> build = new CsvToBeanBuilder<CsvFault>(reader)
                    .withType(CsvFault.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initFault);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initFault(CsvFault csvFault) {
        Long a = Long.parseLong(csvFault.areaId);
        Long n = Long.parseLong(csvFault.whoNotifyId);
        CreateFaultCommand command = new CreateFaultCommand(csvFault.faultDescribe, a, n);
        manipulateFaultService.addFault(command);

        queryFaultService.fidById(2L).ifPresent(fault -> {
            fault.setSpecialist(userService.findById(6L).get());
            fault.setWhoAssigned(userService.findById(3L).get());
            fault.setStatus(ASSIGNED);
        });
    }

    private void initArea(CsvArea csvArea) {
        CreateAreaCommand command = new CreateAreaCommand(csvArea.areaName);
        areaService.addArea(command);
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
        userService.register(command);
    }


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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvArea {
        @CsvBindByName
        String areaName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvFault {
        @CsvBindByName
        String faultDescribe;
        @CsvBindByName
        String areaId;
        @CsvBindByName
        String whoNotifyId;
    }
}
