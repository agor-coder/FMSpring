package pl.gorzki.fmspring.users.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import javax.validation.constraints.Size;
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
    public ResponseEntity<?> getMyAccountData(@AuthenticationPrincipal UserDetails user) {
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
        AppResponse response = service.register(command.toCreateUserCommand());
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
    public void updateUser(@PathVariable Long id, @Valid @RequestBody RestUpdateUserCommand command) {
        AppResponse response = service.updateUser(command.toUpdateCommand(id));
        response.checkResponseSuccess();
    }

    @Data
    private static class RestUserCommand {
        @Size(min=3, max=50)
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

        CreateUserCommand toCreateUserCommand() {
            return new CreateUserCommand(password, firstName, lastName, phone, emailUserName, role);
        }

    }


    @Data
    private static class RestUpdateUserCommand {
        @Size(min=3, max=50)
        private String password;
        @NotBlank(message = "firstName cannot be empty")
        private String firstName;
        @NotBlank(message = "lastName cannot be empty")
        private String lastName;
        private String phone;
        @NotBlank(message = "role cannot be empty")
        private String role;

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
        @Size(min=3, max=50)
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
