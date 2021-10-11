package pl.gorzki.fmspring.users.domain;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Przydzielajacy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Assigner extends UserEntity {

    private String department;
}
