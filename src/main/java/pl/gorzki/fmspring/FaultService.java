package pl.gorzki.fmspring;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FaultService {

    private final Map<Long, Fault> storage = new ConcurrentHashMap<>();

    public FaultService() {
        storage.put(1L,new Fault(1L,"zwarcie"));
        storage.put(2L,new Fault(2L,"brak"));
        storage.put(3L,new Fault(3L,"nie ma"));
    }

    List<Fault> findByNotifier(String notifier) {
        return null;
    }

    List<Fault> findByAssigner(String assigner) {
        return null;
    }

    List<Fault> findAll() {
        return new ArrayList<>(storage.values());
    }
}
