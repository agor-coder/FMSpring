package pl.gorzki.fmspring.fault.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;
import pl.gorzki.fmspring.users.domain.UserEntity;

public interface FaultJpaRepository extends JpaRepository<Fault, Long> {


    @Query(
            "  SELECT COUNT (i) FROM Fault i "
                    + " WHERE " +
                    " i.specialist = :specialist " +
                    " AND i.status=:status "
    )
    int countOfSpecialist(@Param("specialist") UserEntity specialist, @Param("status") FaultStatus status);

    int countBySpecialistAndStatus(UserEntity spec, FaultStatus status);
}
