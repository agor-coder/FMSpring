package pl.gorzki.fmspring.fault.application.port;

import lombok.Builder;
import lombok.Value;
import pl.gorzki.fmspring.*;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface FaultUseCase {
    List<Fault> findAll();

    Optional<Fault> fidById(Long id);

    List<Fault> findByNotifier(String notifier);

    List<Fault> findByAssigner(String assigner);

    List<Fault> findByDesription(String text);

    Optional<Fault> findOneByDesription(String text);

    void addFault(CreateFaultCommand command);

    void removeFaultById(Long id);

    UpdateFaultResponse updateFault(UpdateFaultCommand command);


    @Value
    class CreateFaultCommand {
        String faultDescribe;
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
                fault.setStatus(fault.getStatus());
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
