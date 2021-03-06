package pl.gorzki.fmspring.users.application.port;


import pl.gorzki.fmspring.commons.AppResponse;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserUseCase {

    List<UserEntity> findAll();

    AppResponse register(CreateUserCommand command);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUserName(String userName);

    List<UserEntity> findAllSpecialists();

    AppResponse updateUser(UpdateUserCommand command);


    record CreateUserCommand(String password,
                             String firstName,
                             String lastName,
                             String phone,
                             String emailUserName,
                             String role) {
        public UserEntity toUser(String pass) {
            return new UserEntity(pass, firstName, lastName, phone, emailUserName, role);
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
