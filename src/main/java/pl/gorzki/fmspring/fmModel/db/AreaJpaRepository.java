package pl.gorzki.fmspring.fmModel.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gorzki.fmspring.fmModel.domain.TechArea;

public interface AreaJpaRepository extends JpaRepository<TechArea, Long> {
}
