package pl.gorzki.fmspring.jpa;

import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;
import java.util.UUID;


public class BaseEntity {
    @Id
    protected Long id;

    protected String uuid = UUID.randomUUID().toString();

    protected long version;

    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;
}
