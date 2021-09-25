package pl.gorzki.fmspring.fault.domain;

public enum FaultStatus {
    NOT_ASSIGNED("Nie przydzielona"),
    ASSIGNED("Przydzielona"),
    END("Zakończona");

    private final String description;

    FaultStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
