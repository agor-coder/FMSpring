package pl.gorzki.fmspring.users.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.users.application.port.UserRegistrationUseCase;
import pl.gorzki.fmspring.users.application.port.UserRegistrationUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.domain.UserEntity;



@RestController
@AllArgsConstructor
@RequestMapping("/users")

public class UsersController {

    private final UserRegistrationUseCase userRegistrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity registerUser(@RequestBody RestUserCommand command) {
        return userRegistrationService.register(command.toCreateCommand());

    }


    @Data
    private static class RestUserCommand {
        private String password;
        private String firstName;
        private String lastName;
        private String phone;
        private String emailUserName;
        private String role;

        CreateUserCommand toCreateCommand() {
            return new CreateUserCommand(password, firstName, lastName, phone, emailUserName, role);
        }
    }
}
