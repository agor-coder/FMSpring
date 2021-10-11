package pl.gorzki.fmspring.users.domain;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Specjalista")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Specialist extends UserEntity {

    private String department;
}
