package pl.gorzki.fmspring.users.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.users.application.port.AssignerUseCase;
import pl.gorzki.fmspring.users.db.AssignerJpaRepository;
import pl.gorzki.fmspring.users.domain.Assigner;

import java.util.List;

@Service
@AllArgsConstructor
public class AssignerService implements AssignerUseCase {

    private final AssignerJpaRepository repository;

    @Override
    public List<Assigner> findAll() {
        return repository.findAll();
    }
}
