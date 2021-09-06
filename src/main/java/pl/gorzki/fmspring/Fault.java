package pl.gorzki.fmspring;

import java.util.StringJoiner;

public class Fault {
    Long id;
    String faultDescribe;
    FaultStatus status;
    TechArea area;
    Specialist specialist;
    Assigner whoAssigned;
    Notifier whoNotify;

    public Fault(Long id, String faultDescribe, FaultStatus status, TechArea area, Specialist specialist, Assigner whoAssigned, Notifier whoNotify) {
        this.id = id;
        this.faultDescribe = faultDescribe;
        this.status = status;
        this.area = area;
        this.specialist = specialist;
        this.whoAssigned = whoAssigned;
        this.whoNotify = whoNotify;
    }

    public Fault(Long id, String faultDescribe) {
        this.id = id;
        this.faultDescribe = faultDescribe;

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Fault.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("faultDescribe='" + faultDescribe + "'")
                .add("status=" + status)
                .add("area=" + area)
                .add("specialist=" + specialist)
                .add("whoAssigned=" + whoAssigned)
                .add("whoNotify=" + whoNotify)
                .toString();
    }
}
