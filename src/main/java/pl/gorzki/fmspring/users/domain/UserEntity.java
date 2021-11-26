package pl.gorzki.fmspring.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @NotBlank
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private String phone;

    @Column(unique = true)
    @NotBlank
    private String emailUserName;
    @NotBlank
    private String role;

}
