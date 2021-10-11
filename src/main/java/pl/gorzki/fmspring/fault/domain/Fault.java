package pl.gorzki.fmspring.fault.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.jpa.BaseEntity;
import pl.gorzki.fmspring.users.domain.Assigner;
import pl.gorzki.fmspring.users.domain.Notifier;
import pl.gorzki.fmspring.users.domain.Specialist;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Fault extends BaseEntity {


    private String faultDescribe;

    @Enumerated(EnumType.STRING)
    private FaultStatus status = FaultStatus.NOT_ASSIGNED;
    @ManyToOne
    private TechArea area;
    @ManyToOne
    private Specialist specialist;
    @ManyToOne
    private Assigner whoAssigned;
    @ManyToOne
    private Notifier whoNotify;

    public Fault(String faultDescribe, TechArea area, Specialist specialist, Assigner whoAssigned, Notifier whoNotify) {
        this.faultDescribe = faultDescribe;
        this.area = area;
        this.specialist = specialist;
        this.whoAssigned = whoAssigned;
        this.whoNotify = whoNotify;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Fault{");
        sb.append("id=").append(id);
        sb.append(", faultDescribe='").append(faultDescribe).append('\'');
        sb.append(", status=").append(status.getDescription());
        sb.append(", area=").append(area);
        sb.append(", specialist=").append(specialist);
        sb.append(", whoAssigned=").append(whoAssigned);
        sb.append(", whoNotify=").append(whoNotify);
        sb.append('}');
        return sb.toString();
    }
}
