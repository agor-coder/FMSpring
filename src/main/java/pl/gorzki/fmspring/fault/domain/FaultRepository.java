package pl.gorzki.fmspring.fault.domain;

import java.util.List;
import java.util.Optional;

public interface FaultRepository {
    List<Fault> findAll();
    void save (Fault fault);
    Optional<Fault> findById(Long id);
    void removeById(Long id);
}
