package pl.gorzki.fmspring.users.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements UserUseCase {

    private final UserJpaRepository repository;


    @Override
    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public UserEntity register(CreateUserCommand command) {
        UserEntity user = new UserEntity(
                command.getPassword(),
                command.getFirstName(),
                command.getLastName(),
                command.getPhone(),
                command.getEmailUserName(),
                command.getRole()
        );
        return repository.save(user);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<UserEntity> findSpecialists() {
        return repository.findByRole("ROLE_SPECIALIST");
    }
}
