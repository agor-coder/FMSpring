package pl.gorzki.fmspring.fault.infrastructure;

import org.springframework.stereotype.Repository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultRepository;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Repository
class MemoryFaultRepository implements FaultRepository {

    private final Map<Long, Fault> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXTVALUE = new AtomicLong((0L));



    @Override
    public List<Fault> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Fault fault) {
        nextId();
        fault.setId(nextId());
        storage.put(nextId(), fault);

    }

    private long nextId() {
        return ID_NEXTVALUE.getAndIncrement();
    }
}


