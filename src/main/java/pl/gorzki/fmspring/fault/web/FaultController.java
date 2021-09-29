package pl.gorzki.fmspring.fault.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.Assigner;
import pl.gorzki.fmspring.Notifier;
import pl.gorzki.fmspring.Specialist;
import pl.gorzki.fmspring.TechArea;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase.CreateFaultCommand;
import pl.gorzki.fmspring.fault.domain.Fault;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
//        return service.findByDesription(descr);
//    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return service
                .fidById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fault addFault(@Valid @RequestBody RestCreateFaultCommand command) {
        return service.addFault(command.toCommand());
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
    private static class RestCreateFaultCommand {
        @NotBlank(message = "podaj opis")
        private String faultDescribe;
        private TechArea area;
        private Specialist specialist;
        private Assigner whoAssigned;
        private Notifier whoNotify;

        CreateFaultCommand toCommand() {
            return new CreateFaultCommand(
                    faultDescribe,
                    area,
                    specialist,
                    whoAssigned,
                    whoNotify);
        }
    }
}
