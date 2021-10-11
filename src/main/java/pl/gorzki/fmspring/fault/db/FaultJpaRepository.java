package pl.gorzki.fmspring.fault.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;

public interface FaultJpaRepository extends JpaRepository<Fault, Long> {

}
