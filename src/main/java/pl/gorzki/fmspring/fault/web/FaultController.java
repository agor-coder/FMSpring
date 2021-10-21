package pl.gorzki.fmspring.fault.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase.AssignFaultCommand;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase.UpdateFaultCommand;
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

@RestController
@RequestMapping("/faults")
@AllArgsConstructor
public class FaultController {
    private final FaultUseCase service;
    private final UserUseCase userService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAll(
            @RequestParam Optional<String> descr,
            @RequestParam Optional<String> stat) {
        if (descr.isPresent() && stat.isPresent()) {
            List<Fault> byDescAndStat = service.findByDescriptionAndStatus(descr.get(), stat.get());
            if (byDescAndStat.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such element");
            }
            return byDescAndStat;

        } else if (descr.isPresent()) {
            List<Fault> byDesc = service.findByDescription(descr.get());
            if (byDesc.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such element");
            }
            return byDesc;

        } else if (stat.isPresent()) {
            List<Fault> byStat = service.findByStatus(stat.get());
            if (byStat.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such element");
            }
            return byStat;
        }
        return service.findAll();
    }


//    @GetMapping(params = {"descr"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<Fault> getAllFiltered(@RequestParam String descr) {
//        return service.findByDescription(descr);
//    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot - sorry");
        }
        return service
                .fidById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAllByUser(@PathVariable Long id) {
        UserEntity user = userService.findById(id)//TODO - getAuthority()
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Spec not found with id: "+ id));
        return service.findAllByUser(user);
    }

    @PostMapping
    public ResponseEntity<?> addFault(@Valid @RequestBody RestFaultCommand command) {
        Fault fault = service.addFault(command.toCreateCommand());
        return ResponseEntity.created(createdFaultUri(fault)).build();
    }

    private URI createdFaultUri(Fault fault) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + fault.getId().toString()).build().toUri();
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateFault(@PathVariable Long id, @RequestBody RestFaultCommand command) {
        UpdateResponse response = service.updateFault(command.toUpdateCommand(id));
        checkResponseSuccess(response);
    }

    @PatchMapping("/assign/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void assignFault(@PathVariable Long id, @Valid @RequestBody RestAssignFaultCommand command) {
        UpdateResponse response = service.assignFault(command.toAssignCommand(id));
        checkResponseSuccess(response);
    }


    @PatchMapping("/end/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void endFault(@PathVariable Long id) {
        UpdateResponse response = service.endFault(id);
        checkResponseSuccess(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (service.fidById(id).isPresent()) {
            service.removeFaultById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    private void checkResponseSuccess(UpdateResponse response) {
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
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

        CreateFaultCommand toCreateCommand() {
            return new CreateFaultCommand(
                    faultDescribe,
                    areaId,
                    whoNotifyId);
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
