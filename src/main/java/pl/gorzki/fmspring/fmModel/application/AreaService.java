package pl.gorzki.fmspring.fmModel.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.fmModel.application.port.AreaUseCase;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase;
import pl.gorzki.fmspring.fmModel.db.AreaJpaRepository;
import pl.gorzki.fmspring.fmModel.domain.TechArea;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AreaService implements AreaUseCase {

    private final AreaJpaRepository repository;


    @Override
    public List<TechArea> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TechArea> fidById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<TechArea> findAreaByName(String name) {
        return repository.findAll()
                .stream()
                .filter(area -> area.getAreaName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TechArea addArea(CreateAreaCommand command) {
        TechArea area = command.toArea();
        return repository.save(area);
    }


    @Override
    @Transactional
    public UpdateAreaResponse updateArea(UpdateAreaCommand command) {
        return repository
                .findById(command.getId())
                .map(area -> {
                    command.updateFields(area);
                    return UpdateAreaResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateAreaResponse(false, Collections.singletonList("Fault not found with id: " + command.getId())));
    }
}
