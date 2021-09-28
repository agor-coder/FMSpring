package pl.gorzki.fmspring.fault.application.port;

import lombok.Builder;
import lombok.Value;
import pl.gorzki.fmspring.*;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

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

    Optional<Fault> findOneByDesription(String text);

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
    class UpdateFaultCommand {
        Long id;
        String faultDescribe;
        FaultStatus status;
        TechArea area;
        Specialist specialist;
        Assigner whoAssigned;
        Notifier whoNotify;

        public Fault updateFields(Fault fault) {
            if (faultDescribe != null) {
                fault.setFaultDescribe(faultDescribe);
            }
            if (status != null) {
                fault.setStatus(status);
            }
            if (area != null) {
                fault.setArea(area);
            }
            if (specialist != null) {
                fault.setSpecialist(specialist);
            }
            if (whoAssigned != null) {
                fault.setWhoAssigned(whoAssigned);
            }
            if (whoNotify != null) {
                fault.setWhoNotify(whoNotify);
            }
            return fault;
        }
    }

    @Value
    class UpdateFaultResponse {
        public static UpdateFaultResponse SUCCESS = new UpdateFaultResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }
}
