package pl.gorzki.fmspring.fmModel.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.gorzki.fmspring.fmModel.domain.*;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface FaultUseCase {
    List<Fault> findAll();

    Optional<Fault> fidById(Long id);

    List<Fault> findByNotifier(String notifier);

    List<Fault> findByAssigner(String assigner);

    List<Fault> findByDescription(String text);

    List<Fault> findByStatus(String text);

    List<Fault> findByDescriptionAndStatus(String descr, String status);

    Optional<Fault> findOneByDescription(String text);

    Fault addFault(CreateFaultCommand command);

    void removeFaultById(Long id);

    UpdateFaultResponse updateFault(UpdateFaultCommand command);


    @Value
    class CreateFaultCommand {
        String faultDescribe;
        TechArea area;
        Specialist specialist;
        Assigner whoAssigned;
        Notifier whoNotify;

        public Fault toFault() {
            return new Fault(faultDescribe, area, specialist, whoAssigned, whoNotify);
        }
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateFaultCommand {
        Long id;
        String faultDescribe;
        FaultStatus status;
        TechArea area;
        Specialist specialist;
        Assigner whoAssigned;
        Notifier whoNotify;
    }

    @Value
    class UpdateFaultResponse {
        public static UpdateFaultResponse SUCCESS = new UpdateFaultResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }
}
