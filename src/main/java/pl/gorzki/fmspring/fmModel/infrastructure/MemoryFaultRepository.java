package pl.gorzki.fmspring.fmModel.infrastructure;

import org.springframework.stereotype.Repository;
import pl.gorzki.fmspring.fmModel.domain.Fault;
import pl.gorzki.fmspring.fmModel.domain.FaultRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
    public Fault save(Fault fault) {
        if (null != fault.getId()) {
            storage.put(fault.getId(), fault);
        } else {
            long nextId = nextId();
            fault.setId(nextId);
            storage.put(nextId, fault);
        }
        return fault;
    }

    @Override
    public Optional<Fault> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }

    private long nextId() {
        return ID_NEXTVALUE.getAndIncrement();
    }
}


