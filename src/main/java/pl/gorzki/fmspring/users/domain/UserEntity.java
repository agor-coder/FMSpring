package pl.gorzki.fmspring.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    private String password;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String phone;
    @Getter
    @Column(unique = true)
    private String emailUserName;
    @Getter
    private String role;

}
