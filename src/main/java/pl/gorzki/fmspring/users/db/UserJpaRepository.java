package pl.gorzki.fmspring.users.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailUserNameIgnoreCase(String username);

}
