package pl.gorzki.fmspring.users.application.port;

import pl.gorzki.fmspring.users.domain.Assigner;

import java.util.List;

public interface AssignerUseCase {

    List<Assigner> findAll();
}
