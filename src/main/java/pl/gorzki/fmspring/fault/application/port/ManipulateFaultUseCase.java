package pl.gorzki.fmspring.fault.application.port;

import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

public interface ManipulateFaultUseCase {

    Fault addFault(CreateFaultCommand command);

    UpdateResponse removeFaultById(Long id);

    UpdateResponse updateFault(UpdateFaultCommand command);

    UpdateResponse assignFault(AssignFaultCommand command);

    UpdateResponse changeStatus(Long id, FaultStatus status);



    record CreateFaultCommand(String faultDescribe,
                              Long areaId,
                              Long whoNotifyId) {
    }




    record UpdateFaultCommand(Long id,
                              String faultDescribe,
                              FaultStatus status,
                              Long areaId,
                              Long specialistId,
                              Long whoAssignedId,
                              Long whoNotifyId) {
    }


    record AssignFaultCommand(Long id,
                              Long specialistId,
                              Long whoAssignedId) {
    }
}