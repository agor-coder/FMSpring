package pl.gorzki.fmspring.users.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gorzki.fmspring.users.domain.Assigner;


public interface AssignerJpaRepository extends JpaRepository<Assigner, Long> {
}
