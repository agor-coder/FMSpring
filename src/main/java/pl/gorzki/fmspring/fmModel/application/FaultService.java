package pl.gorzki.fmspring.fmModel.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.fmModel.application.port.FaultUseCase;
import pl.gorzki.fmspring.fmModel.db.FaultJpaRepository;
import pl.gorzki.fmspring.fmModel.domain.Fault;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class FaultService implements FaultUseCase {

    private final FaultJpaRepository repository;

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
    public List<Fault> findByDescription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Fault> findOneByDesription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(text.toLowerCase()))
                .findFirst();
    }

    @Override
    public List<Fault> findByStatus(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getStatus().getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Fault> findByDescriptionAndStatus(String descr, String status) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(descr.toLowerCase()))
                .filter(fault -> fault.getStatus().getDescription().toLowerCase().contains(status.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Fault addFault(CreateFaultCommand command) {
        Fault fault = command.toFault();
        return repository.save(fault);
    }

    @Override
    public void removeFaultById(Long id) {
        repository.deleteById(id);
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