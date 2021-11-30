package pl.gorzki.fmspring.fault.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.gorzki.fmspring.commons.AppResponse;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.AssignFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase.UpdateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.domain.UserEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.END;

@RestController
@RequestMapping("/faults")
@AllArgsConstructor
public class FaultController {
    private final ManipulateFaultUseCase manipulateFaultService;
    private final QueryFaultUseCase queryFaultService;
    private final UserUseCase userService;


    @Secured({"ROLE_ASSIGNER"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAll(
            @RequestParam Optional<String> descr,
            @RequestParam Optional<String> stat) {
        if (descr.isPresent() && stat.isPresent()) {
            List<Fault> byDescAndStat = queryFaultService.findByDescriptionAndStatus(descr.get(), stat.get());
            if (byDescAndStat.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such element");
            }
            return byDescAndStat;

        } else if (descr.isPresent()) {
            List<Fault> byDesc = queryFaultService.findByDescription(descr.get());
            if (byDesc.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such element");
            }
            return byDesc;

        } else if (stat.isPresent()) {
            List<Fault> byStat = queryFaultService.findByStatus(stat.get());
            if (byStat.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such element");
            }
            return byStat;
        }
        return queryFaultService.findAll();
    }


//    @GetMapping(params = {"descr"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<Fault> getAllFiltered(@RequestParam String descr) {
//        return service.findByDescription(descr);
//    }


    @Secured({"ROLE_ASSIGNER"})
    @GetMapping("/eager")
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAllEager() {
        return queryFaultService.findAllEager();
    }


    @Secured({"ROLE_ASSIGNER"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot - sorry");
        }
        return queryFaultService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Secured({"ROLE_ASSIGNER", "ROLE_SPECIALIST","ROLE_NOTIFIER"})
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAllMyFaults(@AuthenticationPrincipal User user) {
        UserEntity userEntity = userService.findByUserName(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "User not found"));
        return queryFaultService.findAllByUser(userEntity);
    }


    @Secured({"ROLE_NOTIFIER"})
    @PostMapping
    public ResponseEntity<?> addFault(@Valid @RequestBody RestFaultCommand command, @AuthenticationPrincipal User user) {
        Long id = userService.findByUserName(user.getUsername()).get().getId();
        Fault fault = manipulateFaultService.addFault(command.toCreateCommand(id));
        return ResponseEntity.created(createdFaultUri(fault)).build();
    }

    private URI createdFaultUri(Fault fault) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + fault.getId().toString()).build().toUri();
    }

    @Secured({"ROLE_NOTIFIER"})
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateFault(@PathVariable Long id, @RequestBody RestFaultCommand command) {
        AppResponse response = manipulateFaultService.updateFault(command.toUpdateCommand(id));
        response.checkResponseSuccess();
    }

    @Secured({"ROLE_ASSIGNER"})
    @PatchMapping("/assign/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void assignFault(@PathVariable Long id, @Valid @RequestBody RestAssignFaultCommand command) {
        AppResponse response = manipulateFaultService.assignFault(command.toAssignCommand(id));
        response.checkResponseSuccess();
    }


    @Secured({"ROLE_ASSIGNER", "ROLE_SPECIALIST"})
    @PatchMapping("/end/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    //TODO - get id from user
    public void endFault(@PathVariable Long id) {
        AppResponse response = manipulateFaultService.changeStatus(id, END);
        response.checkResponseSuccess();
    }

    @Secured({"ROLE_ASSIGNER"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        AppResponse response = manipulateFaultService.removeFaultById(id);
        response.checkResponseSuccess();
    }


    @Data
    private static class RestFaultCommand {
        @NotBlank(message = "podaj opis")
        private String faultDescribe;
        private FaultStatus status;
        private Long areaId;
        private Long specialistId;
        private Long whoAssignedId;
        private Long whoNotifyId;

        CreateFaultCommand toCreateCommand(Long id) {
            return new CreateFaultCommand(
                    faultDescribe,
                    areaId,
                    id);
        }

        UpdateFaultCommand toUpdateCommand(Long id) {
            return new UpdateFaultCommand(
                    id,
                    faultDescribe,
                    status,
                    areaId,
                    specialistId,
                    whoAssignedId,
                    whoNotifyId);
        }
    }

    @Data
    private static class RestAssignFaultCommand {
        @NotNull
        private Long specialistId;
        @NotNull
        private Long whoAssignedId;

        AssignFaultCommand toAssignCommand(Long id) {
            return new AssignFaultCommand(
                    id,
                    specialistId,
                    whoAssignedId);
        }
    }
}
