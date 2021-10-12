package pl.gorzki.fmspring.users.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.users.application.port.UserRegistrationUseCase;
import pl.gorzki.fmspring.users.db.UserEntityRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;


@Service
@AllArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {

    private final UserEntityRepository repository;


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
}
