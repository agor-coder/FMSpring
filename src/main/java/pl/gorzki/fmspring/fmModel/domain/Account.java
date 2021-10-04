package pl.gorzki.fmspring.fmModel.domain;

import lombok.*;
import pl.gorzki.fmspring.jpa.BaseEntity;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String type;
}
