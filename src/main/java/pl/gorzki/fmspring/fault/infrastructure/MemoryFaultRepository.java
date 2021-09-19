package pl.gorzki.fmspring.fault.infrastructure;

import org.springframework.stereotype.Repository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
 class MemoryFaultRepository implements FaultRepository {

    private final Map<Long, Fault> storage = new ConcurrentHashMap<>();

    public MemoryFaultRepository() {
        storage.put(1L, new Fault(1L, "zwarcie"));
        storage.put(2L, new Fault(2L, "brak"));
        storage.put(3L, new Fault(3L, "nie ma"));
        storage.put(4L, new Fault(4L, "spalony"));
    }

    @Override
    public List<Fault> findAll() {
        return new ArrayList<>(storage.values());
    }
}


