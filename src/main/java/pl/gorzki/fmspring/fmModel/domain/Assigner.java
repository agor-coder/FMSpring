package pl.gorzki.fmspring.fmModel.domain;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Przydzielajacy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Assigner extends Account {

    private String department;
}
