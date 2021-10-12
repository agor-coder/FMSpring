package pl.gorzki.fmspring.users.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String email_userName;

    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();
}
