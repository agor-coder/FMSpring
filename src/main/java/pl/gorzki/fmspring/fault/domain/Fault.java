package pl.gorzki.fmspring.fault.domain;


import lombok.*;
import pl.gorzki.fmspring.*;
import java.util.StringJoiner;



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
