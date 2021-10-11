package pl.gorzki.fmspring.area.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.CreateAreaCommand;
import pl.gorzki.fmspring.area.application.port.AreaUseCase.UpdateAreaResponse;
import pl.gorzki.fmspring.area.domain.TechArea;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/areas")
@AllArgsConstructor
public class AreaController {

    private final AreaUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TechArea> getAll(
            @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            return service.findAreaByName(name.get());
        }
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> geById(@PathVariable Long id) {
        return service
                .fidById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TechArea addArea(@Valid @RequestBody RestAreaCommand command) {
        return service.addArea(command.toCreateCommand());
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateArea(@PathVariable Long id, @RequestBody RestAreaCommand command) {
        UpdateAreaResponse response = service.updateArea(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

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

