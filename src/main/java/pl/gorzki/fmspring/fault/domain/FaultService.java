package pl.gorzki.fmspring.fault.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaultService {

    private final FaultRepository repository;

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
                .filter(fault -> fault.getFaultDescribe().startsWith(text))
                .collect(Collectors.toList());
    }
}