package pl.gorzki.fmspring.fmModel.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.fmModel.application.port.AreaUseCase;
import pl.gorzki.fmspring.fmModel.db.AreaJpaRepository;
import pl.gorzki.fmspring.fmModel.domain.TechArea;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AreaService implements AreaUseCase {

    private final AreaJpaRepository repository;


    @Override
    public List<TechArea> findAll() {
        return null;
    }

    @Override
    public Optional<TechArea> fidById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TechArea> findAreaByName(String name) {
        return null;
    }

    @Override
    public TechArea addArea(CreateAreaCommand command) {
        TechArea area = command.toArea();
        return repository.save(area);
    }
}
