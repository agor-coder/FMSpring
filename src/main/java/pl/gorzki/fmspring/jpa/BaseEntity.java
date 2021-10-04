package pl.gorzki.fmspring.jpa;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;


@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of= "uuid")
public class BaseEntity {
    @Id
    @GeneratedValue
    protected Long id;

    protected String uuid = UUID.randomUUID().toString();

    protected long version;

    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;
}
