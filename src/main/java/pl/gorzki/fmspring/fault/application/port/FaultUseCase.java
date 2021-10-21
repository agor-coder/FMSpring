package pl.gorzki.fmspring.fault.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

import java.util.List;
import java.util.Optional;

import static pl.gorzki.fmspring.fault.domain.FaultStatus.END;

public interface FaultUseCase {
    List<Fault> findAll();

    Optional<Fault> fidById(Long id);

    List<Fault> findByNotifier(String notifier);

    List<Fault> findBySpecialist(String assigner);

    List<Fault> findByDescription(String text);

    List<Fault> findByStatus(String text);

    List<Fault> findByDescriptionAndStatus(String descr, String status);

    Optional<Fault> findOneByDescription(String text);

    Fault addFault(CreateFaultCommand command);

    void removeFaultById(Long id);

    UpdateResponse updateFault(UpdateFaultCommand command);

    UpdateResponse assignFault(AssignFaultCommand command);

    UpdateResponse endFault(Long id);


    @Value
    class CreateFaultCommand {
        String faultDescribe;
        Long areaId;
        Long whoNotifyId;

    }


    @Value
    @Builder
    @AllArgsConstructor
    class UpdateFaultCommand {
        Long id;
        String faultDescribe;
        FaultStatus status;
        Long areaId;
        Long specialistId;
        Long whoAssignedId;
        Long whoNotifyId;
    }

    @Value
    @AllArgsConstructor
    class AssignFaultCommand {
        Long id;
        Long specialistId;
        Long whoAssignedId;

    }
}
