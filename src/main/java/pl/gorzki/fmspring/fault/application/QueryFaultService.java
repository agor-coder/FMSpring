package pl.gorzki.fmspring.fault.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.fault.application.port.QueryFaultUseCase;
import pl.gorzki.fmspring.fault.db.FaultJpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class QueryFaultService implements QueryFaultUseCase {
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
    public List<Fault> findAllByUser(UserEntity user) {
        return switch (user.getRole()) {
            case "ROLE_SPECIALIST" -> repository.findAllBySpecialist(user);
            case "ROLE_ASSIGNER" -> repository.findAllByWhoAssigned(user);
            case "ROLE_NOTIFIER" -> repository.findAllByWhoNotify(user);
            default -> Collections.emptyList();
        };
    }


    @Override
    public List<Fault> findByDescription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Fault> findOneByDescription(String text) {
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



}