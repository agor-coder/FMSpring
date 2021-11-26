package pl.gorzki.fmspring.area.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.area.application.port.AreaUseCase;
import pl.gorzki.fmspring.area.db.AreaJpaRepository;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.AppResponse;

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
    public Optional<TechArea> findById(Long id) {
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
    public void removeAreaById(Long id) {
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public AppResponse updateArea(UpdateAreaCommand command) {
        return repository
                .findById(command.id())
                .map(area -> {
                    updateFields(command, area);
                    return AppResponse.SUCCESS;
                })
                .orElseGet(() -> new AppResponse(false, Collections.singletonList("Area not found with id: " + command.id())));
    }

    private TechArea updateFields(UpdateAreaCommand command, TechArea area) {
        if (command.areaName() != null) {
            area.setAreaName(command.areaName());
        }
        return area;
    }
}
