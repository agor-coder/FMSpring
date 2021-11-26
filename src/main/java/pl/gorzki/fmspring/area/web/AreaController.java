package pl.gorzki.fmspring.area.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.CreateAreaCommand;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.UpdateAreaCommand;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.AppResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/areas")
@AllArgsConstructor
public class AreaController {

    private final AreaUseCase service;

    @GetMapping("/getAllTest")
    public List<TechArea> getAllTest() {
        return service.findAll();
    }

    @Secured({"ROLE_ADMIN", "ROLE_NOTIFIER"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TechArea> getAll(
            @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            return service.findAreaByName(name.get());
        }
        return service.findAll();
    }

    @Secured({"ROLE_ADMIN", "ROLE_NOTIFIER"})
    @GetMapping("/{id}")
    public ResponseEntity<?> geById(@PathVariable Long id) {
        return service
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TechArea addArea(@Valid @RequestBody RestAreaCommand command) {
        return service.addArea(command.toCreateCommand());

    }


    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateArea(@PathVariable Long id, @RequestBody RestAreaCommand command) {
        AppResponse response = service.updateArea(command.toUpdateCommand(id));
        response.checkResponseSuccess();
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.removeAreaById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Data
    private static class RestAreaCommand {
        @NotBlank(message = "podaj nazwe")
        private String areaName;

        CreateAreaCommand toCreateCommand() {
            return new CreateAreaCommand(areaName);
        }

        UpdateAreaCommand toUpdateCommand(Long id) {
            return new UpdateAreaCommand(id, areaName);
        }
    }
}

