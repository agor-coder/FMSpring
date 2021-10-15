package pl.gorzki.fmspring.users.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.domain.UserEntity;


@RestController
@AllArgsConstructor
@RequestMapping("/users")

public class UsersController {

    private final UserUseCase service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity registerUser(@RequestBody RestUserCommand command) {
        return service.register(command.toCreateCommand());

    }

    @PostMapping("/notifier")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity registerNotifier(@RequestBody RestNotifierCommand command) {
        return service.register(command.toCreateNotifierCommand());
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

    @Data
    private static class RestNotifierCommand {
        private String password;
        private String firstName;
        private String lastName;
        private String phone;
        private String emailUserName;

        CreateUserCommand toCreateNotifierCommand() {
            return new CreateUserCommand(password, firstName, lastName, phone, emailUserName, "ROLE_NOTIFIER");
        }
    }
}
