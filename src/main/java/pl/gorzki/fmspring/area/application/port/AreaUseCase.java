package pl.gorzki.fmspring.area.application.port;

import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.AppResponse;

import java.util.List;
import java.util.Optional;

public interface AreaUseCase {
    List<TechArea> findAll();

    Optional<TechArea> fidById(Long id);

    List<TechArea> findAreaByName(String name);

    TechArea addArea(CreateAreaCommand command);

    AppResponse updateArea(UpdateAreaCommand command);

    void removeAreaById(Long id);


    record CreateAreaCommand(String areaName) {
        public TechArea toArea() {
            return new TechArea(areaName);
        }
    }


    record UpdateAreaCommand(Long id, String areaName) {
    }

}
