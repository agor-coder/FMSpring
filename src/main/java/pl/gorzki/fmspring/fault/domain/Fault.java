package pl.gorzki.fmspring.fault.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.gorzki.fmspring.*;



@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Fault {

    private Long id;
    private String faultDescribe;
    private FaultStatus status;
    private TechArea area;
    private Specialist specialist;
    private Assigner whoAssigned;
    private Notifier whoNotify;

    public Fault(String faultDescribe) {
        this.faultDescribe = faultDescribe;

    }


}
