package pl.gorzki.fmspring.users.application.port;


import lombok.Value;
import pl.gorzki.fmspring.users.domain.UserEntity;


public interface UserRegistrationUseCase {

    UserEntity register(CreateUserCommand command);


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
