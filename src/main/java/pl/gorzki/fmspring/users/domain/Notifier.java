package pl.gorzki.fmspring.users.domain;


import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Zglaszajacy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notifier extends UserEntity {

    private String emplacement;
}
