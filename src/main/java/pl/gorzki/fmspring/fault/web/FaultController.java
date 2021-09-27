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
}
