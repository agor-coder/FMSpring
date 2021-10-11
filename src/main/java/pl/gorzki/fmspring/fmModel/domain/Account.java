package pl.gorzki.fmspring.fmModel.domain;

import lombok.*;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@NoArgsConstructor
public abstract class Account extends BaseEntity {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String type;
}
