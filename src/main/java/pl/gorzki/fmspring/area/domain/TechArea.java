package pl.gorzki.fmspring.area.domain;


import lombok.*;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TechArea  extends BaseEntity {
    @NotNull
    @Column(unique = true)
    String areaName;
}
