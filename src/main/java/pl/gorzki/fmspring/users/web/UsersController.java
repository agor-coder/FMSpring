package pl.gorzki.fmspring.users.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.gorzki.fmspring.commons.AppResponse;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.application.port.UserUseCase.UpdateUserCommand;
import pl.gorzki.fmspring.users.domain.UserEntity;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/users")

public class UsersController {

    private final UserUseCase service;

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> getAll() {
        return service.findAll();
    }

    @Secured({"ROLE_ASSIGNER", "ROLE_SPECIALIST", "ROLE_NOTIFIER", "ROLE_ADMIN"})
    @GetMapping("/account")
    public ResponseEntity<?> getMyAccountData(@AuthenticationPrincipal User user) {
        String userName = user.getUsername();
        return service
                .findByUserName(userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_ASSIGNER", "ROLE_ADMIN"})
    @GetMapping("/spec")
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> getAllSpecialists() {
        return service.findAllSpecialists();
    }


    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RestUserCommand command) {
        AppResponse response = service.register(command.toCreateCommand());
        response.checkResponseSuccess();
    }


    @PostMapping("/notifier")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNotifier(@Valid @RequestBody RestNotifierCommand command) {
        AppResponse response = service.register(command.toCreateNotifierCommand());
        response.checkResponseSuccess();
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable Long id, @RequestBody RestUserCommand command) {
        AppResponse response = service.updateUser(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Data
    private static class RestUserCommand {
        @NotBlank(message = "password cannot be empty")
        private String password;
        @NotBlank(message = "firstName cannot be empty")
        private String firstName;
        @NotBlank(message = "lastName cannot be empty")
        private String lastName;
        private String phone;
        @NotBlank(message = "username cannot be empty")
        @Email
        private String emailUserName;
        @NotBlank(message = "role cannot be empty")
        private String role;

        CreateUserCommand toCreateCommand() {
            return new CreateUserCommand(password, firstName, lastName, phone, emailUserName, role);
        }

        UpdateUserCommand toUpdateCommand(Long id) {
            return new UpdateUserCommand(
                    id,
                    password,
                    firstName,
                    lastName,
                    phone,
                    role
            );
        }
    }

    @Data
    private static class RestNotifierCommand {
        @NotBlank(message = "password cannot be empty")
        private String password;
        @NotBlank(message = "firstName cannot be empty")
        private String firstName;
        @NotBlank(message = "lastName cannot be empty")
        private String lastName;
        private String phone;
        @NotBlank(message = "username cannot be empty")
        @Email
        private String emailUserName;

        CreateUserCommand toCreateNotifierCommand() {
            return new CreateUserCommand(password, firstName, lastName, phone, emailUserName, "ROLE_NOTIFIER");
        }
    }
}
