package pl.gorzki.fmspring.fmModel.domain;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Specjalista")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Specialist extends Account {

    private String department;
}
