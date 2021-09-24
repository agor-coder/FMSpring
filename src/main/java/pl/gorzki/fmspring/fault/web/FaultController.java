package pl.gorzki.fmspring.fault.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;

@RestController
@RequestMapping("/faults")
@AllArgsConstructor
public class FaultController {
    private final FaultUseCase service;

    @GetMapping
    public List<Fault> getAll() {
        return service.findAll();
    }
}
