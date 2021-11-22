package pl.gorzki.fmspring.area.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.CreateAreaCommand;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.UpdateResponse;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/areas")
@AllArgsConstructor
public class AreaController {

    private final AreaUseCase service;

//    ADMIN, NOTIFIER
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TechArea> getAll(
            @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            return service.findAreaByName(name.get());
        }
        return service.findAll();
    }

    //    ADMIN, NOTIFIER
    @GetMapping("/{id}")
    public ResponseEntity<?> geById(@PathVariable Long id) {
        return service
                .fidById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //    ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TechArea addArea(@Valid @RequestBody RestAreaCommand command) {
        return service.addArea(command.toCreateCommand());
    }


    //    ADMIN
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateArea(@PathVariable Long id, @RequestBody RestAreaCommand command) {
        UpdateResponse response = service.updateArea(command.toUpdateCommand(id));
        if (!response.success()) {
            String message = String.join(", ", response.errors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    //    ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (service.fidById(id).isPresent()) {
            service.removeAreaById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Data
    private static class RestAreaCommand {
        @NotBlank(message = "podaj nazwe")
        private String areaName;

        private CreateAreaCommand toCreateCommand() {
            return new CreateAreaCommand(areaName);
        }

        private AreaUseCase.UpdateAreaCommand toUpdateCommand(Long id) {
            return new AreaUseCase.UpdateAreaCommand(id, areaName);
        }
    }
}

