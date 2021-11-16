package pl.gorzki.fmspring.fault.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.area.db.AreaJpaRepository;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.application.port.ManipulateFaultUseCase;
import pl.gorzki.fmspring.fault.db.FaultJpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.fault.domain.FaultStatus;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.Collections;

import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;

@Service
public class ManipulateFaultService implements ManipulateFaultUseCase {

    private final FaultJpaRepository repository;
    private final AreaJpaRepository areaRepository;
    private final UserJpaRepository userRepository;
    private final Long limit;

    public ManipulateFaultService(FaultJpaRepository repository,
                                  AreaJpaRepository areaRepository,
                                  UserJpaRepository userRepository,
                                  @Value("${fmspring.faults.limit}") Long limit) {
        this.repository = repository;
        this.areaRepository = areaRepository;
        this.userRepository = userRepository;
        this.limit = limit;
    }

    @Override
    @Transactional
    public Fault addFault(CreateFaultCommand command) {
        Fault fault = toFault(command);
        return repository.save(fault);
    }


    private Fault toFault(CreateFaultCommand command) {
        TechArea area = areaRepository.findById(command.areaId()).get();
        //TODO - get id from user
        UserEntity notif = userRepository.findById(command.whoNotifyId()).get();
        return new Fault(command.faultDescribe(), area, notif);

    }

    @Override
    public UpdateResponse removeFaultById(Long id) {
        return repository
                .findById(id)
                .map(this::remove)
                .orElseGet(() -> new UpdateResponse(false, Collections.singletonList("Fault not found with id: " + id)));

    }

    @Override
    @Transactional
    public UpdateResponse updateFault(UpdateFaultCommand command) {
        return repository
                .findById(command.id())
                .map(fault -> {
                    updateFields(command, fault);
//                    Fault updatedFault = command.updateFields(fault);  //bo @Transactional
//                    repository.save(updatedFault);
                    return UpdateResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateResponse(false, Collections.singletonList("Fault not found with id: " + command.id())));

    }

    @Override
    @Transactional
    public UpdateResponse assignFault(AssignFaultCommand command) {
        return repository
                .findById(command.id())
                .map(fault -> {
                    UserEntity spec = userRepository.findById(command.specialistId()).get();
                    if (checkSpec(spec, fault)) {
                        return new UpdateResponse(false, Collections.singletonList("Specialist - the same"));
                    }
                    if (countOfSpecFaults(spec) >= limit) {
                        return new UpdateResponse(false, Collections.singletonList("Specialist - fault limit reached"));
                    }

                    //TODO - get id from user
                    UserEntity assigner = userRepository.findById(command.whoAssignedId()).get();
                    fault.setWhoAssigned(assigner);
                    fault.setSpecialist(spec);
                    fault.setStatus(ASSIGNED);
                    return UpdateResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateResponse(
                        false, Collections.singletonList("Fault not found with id: " + command.id())));
    }

    @Override
    @Transactional
    public UpdateResponse changeStatus(Long id, FaultStatus status) {
        return repository
                .findById(id)
                .map(fault -> {
                    fault.setStatus(status);
                    return UpdateResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateResponse(
                        false, Collections.singletonList("Fault not found with id: " + id)));
    }


    private boolean checkSpec(UserEntity spec, Fault fault) {
        return null != fault.getSpecialist() && fault.getSpecialist().getUuid().equals(spec.getUuid());
    }


    private int countOfSpecFaults(UserEntity spec) {
        return repository.countBySpecialistAndStatus(spec, ASSIGNED);

    }


    private Fault updateFields(UpdateFaultCommand command, Fault fault) {
        if (command.faultDescribe() != null) {
            fault.setFaultDescribe(command.faultDescribe());
        }
        if (command.status() != null) {
            fault.setStatus(command.status());
        }
        if (command.areaId() != null) {
            fault.setArea(areaRepository.findById(command.areaId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find such area")));
        }
        if (command.specialistId() != null) {
            fault.setSpecialist(userRepository.findById(command.specialistId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find specialist")));
        }
        if (command.whoAssignedId() != null) {
            fault.setWhoAssigned(userRepository.findById(command.whoAssignedId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find assigner")));
        }
        if (command.whoNotifyId() != null) {
            fault.setWhoNotify(userRepository.findById(command.whoNotifyId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find notifier")));
        }
        return fault;
    }

    private UpdateResponse remove(Fault fault) {
        if (fault.getStatus() == ASSIGNED) {
            return new UpdateResponse(false, Collections.singletonList("Unable to remove assigned fault"));
        }
        repository.deleteById(fault.getId());
        return UpdateResponse.SUCCESS;
    }
}
