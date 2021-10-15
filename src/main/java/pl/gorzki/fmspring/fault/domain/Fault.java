package pl.gorzki.fmspring.fault.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.jpa.BaseEntity;
import pl.gorzki.fmspring.users.domain.UserEntity;

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
    private UserEntity specialist;
    @ManyToOne
    private UserEntity whoAssigned;
    @ManyToOne
    private UserEntity whoNotify;

    public Fault(String faultDescribe, TechArea area, UserEntity specialist, UserEntity whoAssigned, UserEntity whoNotify) {
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
        sb.append(", area=").append(area.getAreaName());
        sb.append(", specialist=").append(specialist);
        sb.append(", whoAssigned=").append(whoAssigned);
        sb.append(", whoNotify=").append(whoNotify.getLastName());
        sb.append('}');
        return sb.toString();
    }
}
