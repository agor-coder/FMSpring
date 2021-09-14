package pl.gorzki.fmspring.fault.domain;

import java.util.List;

public interface FaultRepository {
    List<Fault> findAll();

}
