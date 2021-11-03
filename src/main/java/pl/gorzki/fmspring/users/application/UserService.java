package pl.gorzki.fmspring.users.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.Collections;
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
        UserEntity user = command.toUser();
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

    @Override
    @Transactional
    public UpdateResponse updateUser(UpdateUserCommand command) {
        return repository
                .findById(command.id())
                .map(user -> {
                    updateFields(command, user);
                    return UpdateResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateResponse(false, Collections.singletonList("User not found with id: " + command.id())));
    }

    private void updateFields(UpdateUserCommand command, UserEntity user) {
        if (command.password() != null) {
            user.setPassword(command.password());
        }
        if (command.firstName() != null) {
            user.setFirstName(command.firstName());
        }
        if (command.lastName() != null) {
            user.setLastName(command.lastName());
        }
        if (command.phone() != null) {
            user.setPhone(command.phone());
        }
        if (command.role() != null) {
            user.setRole(command.role());
        }
    }
}
