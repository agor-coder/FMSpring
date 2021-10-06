package pl.gorzki.fmspring.fmModel.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.fmModel.application.port.AreaUseCase;
import pl.gorzki.fmspring.fmModel.application.port.AreaUseCase.CreateAreaCommand;
import pl.gorzki.fmspring.fmModel.domain.TechArea;

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

    @Data
    private static class RestAreaCommand {
        @NotBlank(message = "podaj nazwe")
        private String areaName;

        private CreateAreaCommand toCreateCommand() {
            return new CreateAreaCommand(areaName);
        }
    }
}
