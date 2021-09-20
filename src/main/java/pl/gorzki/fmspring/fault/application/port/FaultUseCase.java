package pl.gorzki.fmspring.fault.application.port;

import lombok.Value;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.List;
import java.util.Optional;

public interface FaultUseCase {
    List<Fault> findAll();

    List<Fault> findByNotifier(String notifier);

    List<Fault> findByAssigner(String assigner);

    List<Fault> finByDesription(String text);

    Optional<Fault> finOneByDesription(String text);

    void addFault(CreateFaultCommand command);

    void removeFaultById(Long id);

    void updateFault();


    @Value
    class CreateFaultCommand {
        String faultDescribe;
    }
}
