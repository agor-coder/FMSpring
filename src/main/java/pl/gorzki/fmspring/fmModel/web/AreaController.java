package pl.gorzki.fmspring.fmModel.web;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.fmModel.application.port.AreaUseCase;
import pl.gorzki.fmspring.fmModel.domain.TechArea;

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





}
