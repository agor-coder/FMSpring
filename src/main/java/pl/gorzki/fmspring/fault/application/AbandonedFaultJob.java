package pl.gorzki.fmspring.fault.application;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.fault.db.FaultJpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;

import java.time.LocalDateTime;
import java.util.List;

import static pl.gorzki.fmspring.fault.domain.FaultStatus.*;

@Component
@AllArgsConstructor
public class AbandonedFaultJob {

    private final FaultJpaRepository repository;
    private final ManipulateFaultService faultService;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void run() {
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(1);

        List<Fault> faults = repository.findByStatusAndCreatedAtLessThanEqual(NOT_ASSIGNED, timestamp);
        System.out.println("******************** find orders to be abandoned " + faults.size());
        faults.forEach(fault -> faultService.changeStatus(fault.getId(), ABANDONED));

    }
}
