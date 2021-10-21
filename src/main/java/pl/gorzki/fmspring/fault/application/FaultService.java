package pl.gorzki.fmspring.fault.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gorzki.fmspring.area.db.AreaJpaRepository;
import pl.gorzki.fmspring.area.domain.TechArea;
import pl.gorzki.fmspring.commons.UpdateResponse;
import pl.gorzki.fmspring.fault.application.port.FaultUseCase;
import pl.gorzki.fmspring.fault.db.FaultJpaRepository;
import pl.gorzki.fmspring.fault.domain.Fault;
import pl.gorzki.fmspring.users.db.UserJpaRepository;
import pl.gorzki.fmspring.users.domain.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.gorzki.fmspring.fault.domain.FaultStatus.ASSIGNED;
import static pl.gorzki.fmspring.fault.domain.FaultStatus.END;

@Service
class FaultService implements FaultUseCase {
    private final FaultJpaRepository repository;
    private final AreaJpaRepository areaRepository;
    private final UserJpaRepository userRepository;
    private final Long limit;

    public FaultService(FaultJpaRepository repository,
                        AreaJpaRepository areaRepository,
                        UserJpaRepository userRepository,
                        @Value("${fmspring.faults.limit}") Long limit) {
        this.repository = repository;
        this.areaRepository = areaRepository;
        this.userRepository = userRepository;
        this.limit = limit;
    }

    @Override
    public List<Fault> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Fault> fidById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Fault> findByNotifier(String notifier) {
        return null;
    }

    @Override
    public List<Fault> findBySpecialist(String assigner) {
        return null;
    }


    @Override
    public List<Fault> findByDescription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Fault> findOneByDescription(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(text.toLowerCase()))
                .findFirst();
    }

    @Override
    public List<Fault> findByStatus(String text) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getStatus().getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Fault> findByDescriptionAndStatus(String descr, String status) {
        return repository.findAll()
                .stream()
                .filter(fault -> fault.getFaultDescribe().toLowerCase().contains(descr.toLowerCase()))
                .filter(fault -> fault.getStatus().getDescription().toLowerCase().contains(status.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Fault addFault(CreateFaultCommand command) {
        Fault fault = toFault(command);
        return repository.save(fault);
    }


    private Fault toFault(CreateFaultCommand command) {
        TechArea area = areaRepository.findById(command.getAreaId()).get();
        UserEntity notif = userRepository.findById(command.getWhoNotifyId()).get(); //TODO - getAuthority()
        return new Fault(command.getFaultDescribe(), area, notif);

    }

    @Override
    public void removeFaultById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public UpdateResponse updateFault(UpdateFaultCommand command) {
        return repository
                .findById(command.getId())
                .map(fault -> {
                    updateFields(command, fault);
//                    Fault updatedFault = command.updateFields(fault);  //bo @Transactional
//                    repository.save(updatedFault);
                    return UpdateResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateResponse(false, Collections.singletonList("Fault not found with id: " + command.getId())));

    }

    @Override
    @Transactional
    public UpdateResponse assignFault(AssignFaultCommand command) {
        return repository
                .findById(command.getId())
                .map(fault -> {
                    UserEntity spec = userRepository.findById(command.getSpecialistId()).get();
                    if (countOfSpecFaults(spec) >= limit) {
                        return new UpdateResponse(false, Collections.singletonList("Specialist - fault limit reached"));
                    }
                    if (checkSpec(spec, fault)) {
                        return new UpdateResponse(false, Collections.singletonList("Specialist - the same"));
                    }
                    UserEntity assign = userRepository.findById(command.getWhoAssignedId()).get();//TODO - getAuthority()
                    fault.setWhoAssigned(assign);
                    fault.setSpecialist(spec);
                    fault.setStatus(ASSIGNED);
                    return UpdateResponse.SUCCESS;
                })

                .orElseGet(() -> new UpdateResponse(false, Collections.singletonList("Fault not found with id: " + command.getId())));
    }

    @Override
    @Transactional
    public UpdateResponse endFault(Long id) {
        return repository
                .findById(id)
                .map(fault -> {
                    fault.setStatus(END);
                    return UpdateResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateResponse(false, Collections.singletonList("Fault not found with id: " + id)));
    }



    private boolean checkSpec(UserEntity spec, Fault fault) {
        return null != fault.getSpecialist() && fault.getSpecialist().getUuid().equals(spec.getUuid());
    }


    private int countOfSpecFaults(UserEntity spec) {
        return repository.countBySpecialistAndStatus(spec, ASSIGNED);

    }


    private Fault updateFields(UpdateFaultCommand command, Fault fault) {
        if (command.getFaultDescribe() != null) {
            fault.setFaultDescribe(command.getFaultDescribe());
        }
        if (command.getStatus() != null) {
            fault.setStatus(command.getStatus());
        }
        if (command.getAreaId() != null) {
            fault.setArea(areaRepository.findById(command.getAreaId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find such area ")));
        }
        if (command.getSpecialistId() != null) {
            fault.setSpecialist(userRepository.findById(command.getSpecialistId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find specialist ")));
        }
        if (command.getWhoAssignedId() != null) {
            fault.setWhoAssigned(userRepository.findById(command.getWhoAssignedId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find assigner ")));
        }
        if (command.getWhoNotifyId() != null) {
            fault.setWhoNotify(userRepository.findById(command.getWhoNotifyId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find notifier ")));
        }
        return fault;
    }


}