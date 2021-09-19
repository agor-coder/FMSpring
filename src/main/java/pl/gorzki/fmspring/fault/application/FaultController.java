package pl.gorzki.fmspring.fault.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FaultController {
    private final FaultService service;


    public List<Fault> findAll() {
        return service.findAll();
    }

    public List<Fault> finByDesription(String text){
        return service.finByDesription(text);
    }
}
