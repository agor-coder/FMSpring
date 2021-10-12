package pl.gorzki.fmspring.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    @Column(unique = true)
    private String emailUserName;
    private String role;

}
