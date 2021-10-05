package pl.gorzki.fmspring.fmModel.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gorzki.fmspring.fmModel.domain.Fault;

public interface FaultJpaRepository extends JpaRepository<Fault, Long> {

}
