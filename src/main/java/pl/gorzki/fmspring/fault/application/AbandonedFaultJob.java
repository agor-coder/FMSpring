package pl.gorzki.fmspring.fault.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.clock.Clock;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.db.FaultJpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static pl.gorzki.fmspring.fault.domain.FaultStatus.ABANDONED;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.NOT_ASSIGNED;

@Component
@Slf4j
@AllArgsConstructor
public class AbandonedFaultJob {

    private final FaultJpaRepository repository;
    private final ManipulateFaultUseCase faultUseCase;
    private final FaultProperties properties;
    private final Clock clock;

    @Scheduled(cron = "${fmspring.faults.abandon-cron}")
    @Transactional
    public void run() {
        Duration period= properties.getAbandonPeriod();
        LocalDateTime olderThan = clock.now().minus(period);
        List<Fault> faults = repository.findByStatusAndCreatedAtLessThanEqual(NOT_ASSIGNED, olderThan);
        log.info( "******************** find orders to be abandoned " + faults.size());
        faults.forEach(fault -> faultUseCase.changeStatus(fault.getId(), ABANDONED));

    }
}
