package pl.gorzki.fmspring.fault.db;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface FaultJpaRepository extends JpaRepository<Fault, Long> {


    @Query(
            "  SELECT COUNT (i) FROM Fault i "
                    + " WHERE " +
                    " i.specialist = :specialist " +
                    " AND i.status=:status "
    )
    int countOfSpecialist(@Param("specialist") UserEntity specialist, @Param("status") FaultStatus status);

    int countBySpecialistAndStatus(UserEntity spec, FaultStatus status);


    List<Fault> findAllBySpecialist(UserEntity specialist);

    List<Fault> findAllByWhoAssigned(UserEntity assigner);

    List<Fault> findAllByWhoNotify(UserEntity notifier);

    List<Fault>findByStatusAndCreatedAtLessThanEqual(FaultStatus status, LocalDateTime timestamp);

    @Query(" SELECT DISTINCT b FROM Fault b JOIN FETCH b.area  JOIN FETCH b.whoNotify")
    List<Fault>findAllEager();
}
