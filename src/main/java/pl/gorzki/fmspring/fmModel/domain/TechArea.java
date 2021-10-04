package pl.gorzki.fmspring.fmModel.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.jpa.BaseEntity;

import javax.persistence.Entity;


@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class TechArea  extends BaseEntity {
    String areaName;
}
