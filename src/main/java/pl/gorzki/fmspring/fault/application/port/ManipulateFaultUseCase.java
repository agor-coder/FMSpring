package pl.gorzki.fmspring.fault.application.port;

import pl.gorzki.fmspring.commons.AppResponse;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

public interface ManipulateFaultUseCase {

    Fault addFault(CreateFaultCommand command);

    AppResponse removeFaultById(Long id);

    AppResponse updateFault(UpdateFaultCommand command);

    AppResponse assignFault(AssignFaultCommand command);

    AppResponse changeStatus(Long id, FaultStatus status);



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