package pl.gorzki.fmspring.users.application.port;


import lombok.Value;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserUseCase {

    UserEntity register(CreateUserCommand command);

    Optional<UserEntity> findById(Long id);

    List<UserEntity> findSpecialists();


    @Value

    class CreateUserCommand {
        String password;
        String firstName;
        String lastName;
        String phone;
        String emailUserName;
        String role;

        public UserEntity toUser() {
            return new UserEntity(password, firstName, lastName, phone, emailUserName, role);
        }
    }
}
