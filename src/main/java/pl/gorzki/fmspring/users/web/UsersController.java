package pl.gorzki.fmspring.users.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.users.application.port.UserUseCase;
import pl.gorzki.fmspring.users.application.port.UserUseCase.CreateUserCommand;
import pl.gorzki.fmspring.users.application.port.UserUseCase.UpdateUserCommand;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/users")

public class UsersController {

    private final UserUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> getAll() {
        return service.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return service
                //TODO - get id from user
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/spec")
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> getSpecialists() {
        return service.findSpecialists();
    }


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

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable Long id, @RequestBody RestUserCommand command) {
        UpdateResponse response = service.updateUser(command.toUpdateCommand(id));
        if (!response.success()) {
            String message = String.join(", ", response.errors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
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
