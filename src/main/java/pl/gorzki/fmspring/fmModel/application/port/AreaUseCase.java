package pl.gorzki.fmspring.fmModel.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.gorzki.fmspring.fmModel.domain.TechArea;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface AreaUseCase {
    List<TechArea> findAll();

    Optional<TechArea> fidById(Long id);

    List<TechArea> findAreaByName(String name);

    TechArea addArea(CreateAreaCommand command);

    UpdateAreaResponse updateArea(UpdateAreaCommand command);

    void removeAreaById(Long id);



    @Value
    class CreateAreaCommand {
        String areaName;

        public TechArea toArea() {
            return new TechArea(areaName);
        }
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateAreaCommand {
        Long id;
        String areaName;
    }

    @Value
    class UpdateAreaResponse {
        public static UpdateAreaResponse SUCCESS = new UpdateAreaResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }

}
