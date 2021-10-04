package pl.gorzki.fmspring.fmModel.domain;

import java.util.List;
import java.util.Optional;

public interface FaultRepository {
    List<Fault> findAll();
    Fault save (Fault fault);
    Optional<Fault> findById(Long id);
    void removeById(Long id);
}