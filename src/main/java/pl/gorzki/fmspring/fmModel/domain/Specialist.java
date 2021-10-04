package pl.gorzki.fmspring.fmModel.domain;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Specialist extends Account {

    private String department;
}
