package pl.gorzki.fmspring.fmModel.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase.UpdateFaultCommand;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase.UpdateFaultResponse;
import pl.gorzki.fmspring.fmModel.domain.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faults")
@AllArgsConstructor
public class FaultController {
    private final FaultUseCase service;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAll(
            @RequestParam Optional<String> descr,
            @RequestParam Optional<String> stat) {
        if (descr.isPresent() && stat.isPresent()) {
            return service.findByDescriptionAndStatus(descr.get(), stat.get());
        } else if (descr.isPresent()) {
            return service.findByDescription(descr.get());
        } else if (stat.isPresent()) {
            return service.findByStatus(stat.get());
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fault addFault(@Valid @RequestBody RestFaultCommand command) {
        return service.addFault(command.toCreateCommand());

    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateFault(@PathVariable Long id, @RequestBody RestFaultCommand command) {
        UpdateFaultResponse response = service.updateFault(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (service.fidById(id).isPresent()) {
            service.removeFaultById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @Data
    private static class RestFaultCommand {
        @NotBlank(message = "podaj opis")
        private String faultDescribe;
        private FaultStatus status;
        private TechArea area;
        private Specialist specialist;
        private Assigner whoAssigned;
        private Notifier whoNotify;

        CreateFaultCommand toCreateCommand() {
            return new CreateFaultCommand(
                    faultDescribe,
                    area,
                    specialist,
                    whoAssigned,
                    whoNotify);
        }

        UpdateFaultCommand toUpdateCommand(Long id) {
            return new UpdateFaultCommand(
                    id,
                    faultDescribe,
                    status,
                    area,
                    specialist,
                    whoAssigned,
                    whoNotify
            );
        }
    }
}
