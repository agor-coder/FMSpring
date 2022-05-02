package pl.gorzki.fmspring.initialization.web;


import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gorzki.fmspring.initialization.application.port.InitServiceUseCase;

@RestController
@Secured({"ROLE_ADMIN"})
@AllArgsConstructor
@RequestMapping("/initialization")
public class InitController {

    private final InitServiceUseCase initService;


    @PostMapping
    public void initialize() {
        initService.initialize();
    }
}
