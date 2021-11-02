package pl.gorzki.fmspring.fault.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

public interface ManipulateFaultUseCase {

    Fault addFault(CreateFaultCommand command);

    UpdateResponse removeFaultById(Long id);

    UpdateResponse updateFault(UpdateFaultCommand command);

    UpdateResponse assignFault(AssignFaultCommand command);

    UpdateResponse changeStatus(Long id, FaultStatus status);


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