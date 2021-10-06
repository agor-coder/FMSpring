package pl.gorzki.fmspring.fmModel.domain;


import lombok.*;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TechArea  extends BaseEntity {
    @Column(unique = true)
    String areaName;
}
