package pl.gorzki.fmspring.fault.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FaultStatus {
    NOT_ASSIGNED("Nie przydzielona"),
    ASSIGNED("Przydzielona"),
    END("Zakończona"),
    ABANDONED("Porzucona");

    private final String description;


}
