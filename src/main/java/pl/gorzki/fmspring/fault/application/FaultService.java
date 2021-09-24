package pl.gorzki.fmspring.fault.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class FaultService implements FaultUseCase {

    private final FaultRepository repository;

    @Override
    public List<Fault> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Fault> fidById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Fault> findByNotifier(String notifier) {
        return null;
    }

    @Override
    public List<Fault> findByAssigner(String assigner) {
        return null;
    }


    @Override
    public List<Fault> findByDesription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().startsWith(text))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Fault> findOneByDesription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().startsWith(text))
                .findFirst();
    }

    @Override
    public void addFault(CreateFaultCommand command) {
        Fault fault = new Fault(command.getFaultDescribe());
        repository.save(fault);

    }

    @Override
    public void removeFaultById(Long id) {
        repository.removeById(id);
    }

    @Override
    public UpdateFaultResponse updateFault(UpdateFaultCommand command) {
        return repository
                .findById(command.getId())
                .map(fault -> {
                    Fault updatedFault = command.updateFields(fault);
                    repository.save(updatedFault);
                    return UpdateFaultResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateFaultResponse(false, Collections.singletonList("Fault not found with id: " + command.getId())));

    }


}