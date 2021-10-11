package pl.gorzki.fmspring.area.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gorzki.fmspring.area.domain.TechArea;

public interface AreaJpaRepository extends JpaRepository<TechArea, Long> {

}
