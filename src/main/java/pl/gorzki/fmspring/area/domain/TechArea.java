package pl.gorzki.fmspring.area.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TechArea  extends BaseEntity {
    @NotBlank
    @Column(unique = true)
    String areaName;
}
