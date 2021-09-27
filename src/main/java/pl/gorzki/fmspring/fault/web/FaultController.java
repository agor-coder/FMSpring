package pl.gorzki.fmspring.fault.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faults")
@AllArgsConstructor
public class FaultController {
    private final FaultUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Fault> getAll() {
        return service.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
       return service
               .fidById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }
}
