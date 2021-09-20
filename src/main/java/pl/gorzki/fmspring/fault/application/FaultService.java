package pl.gorzki.fmspring.fault.application;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultRepository;

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
    public List<Fault> findByNotifier(String notifier) {
        return null;
    }

    @Override
    public List<Fault> findByAssigner(String assigner) {
        return null;
    }


    @Override
    public List<Fault> finByDesription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().startsWith(text))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Fault> finOneByDesription(String text) {
        return Optional.empty();
    }

    @Override
    public void addFault(CreateFaultCommand command) {
        Fault fault = new Fault(command.getFaultDescribe());
        repository.save(fault);

    }

    @Override
    public void removeFaultById(Long id) {

    }

    @Override
    public void updateFault() {

    }

}