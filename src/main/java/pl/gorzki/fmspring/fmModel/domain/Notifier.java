package pl.gorzki.fmspring.fmModel.domain;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Zglaszajacy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notifier extends Account{

    private String emplacement;
}
