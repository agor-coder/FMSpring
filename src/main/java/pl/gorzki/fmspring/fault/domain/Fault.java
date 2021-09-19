package pl.gorzki.fmspring.fault.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.gorzki.fmspring.*;



@RequiredArgsConstructor
@Getter
@ToString
public class Fault {

    private Long id;
    private String faultDescribe;
    private FaultStatus status;
    private TechArea area;
    private Specialist specialist;
    private Assigner whoAssigned;
    private Notifier whoNotify;

    public Fault(Long id, String faultDescribe) {
        this.id = id;
        this.faultDescribe = faultDescribe;

    }


}
