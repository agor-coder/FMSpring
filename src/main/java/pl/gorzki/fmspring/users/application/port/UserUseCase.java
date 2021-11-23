package pl.gorzki.fmspring.users.application.port;


import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserUseCase {

    List<UserEntity> findAll();

    UserEntity register(CreateUserCommand command);

    Optional<UserEntity> findById(Long id);

    List<UserEntity> findAllSpecialists();

    UpdateResponse updateUser(UpdateUserCommand command);


    record CreateUserCommand(String password,
                             String firstName,
                             String lastName,
                             String phone,
                             String emailUserName,
                             String role) {
        public UserEntity toUser() {
            return new UserEntity(password, firstName, lastName, phone, emailUserName, role);
        }
    }


    record UpdateUserCommand(Long id,
                             String password,
                             String firstName,
                             String lastName,
                             String phone,
                             String role) {
    }
}
