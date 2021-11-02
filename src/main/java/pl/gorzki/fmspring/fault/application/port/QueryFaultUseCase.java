package pl.gorzki.fmspring.fault.application.port;

import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface QueryFaultUseCase {
    List<Fault> findAll();
    List<Fault> findAllEager();

    Optional<Fault> fidById(Long id);

    List<Fault> findAllByUser(UserEntity specialist);

    List<Fault> findByDescription(String text);

    List<Fault> findByStatus(String text);

    List<Fault> findByDescriptionAndStatus(String descr, String status);

    Optional<Fault> findOneByDescription(String text);


}
