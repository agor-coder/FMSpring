package pl.gorzki.fmspring.fault.domain;

import org.springframework.stereotype.Service;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FaultService {

    private FaultRepository repository;

    public FaultService(FaultRepository repository) {
        this.repository = repository;
    }

    public List<Fault> findAll() {
        return repository.findAll();
    }

    List<Fault> findByNotifier(String notifier) {
        return null;
    }

    List<Fault> findByAssigner(String assigner) {
        return null;
    }


    public List<Fault> finByDesription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.faultDescribe.startsWith(text))
                .collect(Collectors.toList());
    }
}