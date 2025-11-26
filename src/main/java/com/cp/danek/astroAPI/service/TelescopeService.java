package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.enums.TelescopeStatus;
import com.cp.danek.astroAPI.model.repositories.TelescopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TelescopeService {

    private final TelescopeRepository telescopeRepository;

    @Autowired
    public TelescopeService(TelescopeRepository telescopeRepository) {
        this.telescopeRepository = telescopeRepository;
    }

    // Основные CRUD операции
    public List<Telescope> getAllTelescopes() {
        return telescopeRepository.findAll();
    }

    public Optional<Telescope> getTelescopeById(Long id) {
        return telescopeRepository.findById(id);
    }

    public Telescope createTelescope(Telescope telescope) {
        return telescopeRepository.save(telescope);
    }

    public Telescope updateTelescope(Long id, Telescope telescopeDetails) {
        return telescopeRepository.findById(id)
                .map(telescope -> {
                    telescope.setName(telescopeDetails.getName());
                    telescope.setType(telescopeDetails.getType());
                    telescope.setAperture(telescopeDetails.getAperture());
                    telescope.setFocalLength(telescopeDetails.getFocalLength());
                    telescope.setLocation(telescopeDetails.getLocation());
                    telescope.setStatus(telescopeDetails.getStatus());
                    telescope.setMaxResolution(telescopeDetails.getMaxResolution());
                    return telescopeRepository.save(telescope);
                })
                .orElseThrow(() -> new RuntimeException("Телескоп не найден с id: " + id));
    }

    public void deleteTelescope(Long id) {
        telescopeRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<Telescope> getAvailableTelescopes() {
        return telescopeRepository.findByStatus(TelescopeStatus.AVAILABLE);
    }

    public List<Telescope> getTelescopesByType(String type) {
        return telescopeRepository.findByType(com.cp.danek.astroAPI.model.enums.TelescopeType.valueOf(type));
    }

    public void setTelescopeStatus(Long id, TelescopeStatus status) {
        telescopeRepository.findById(id).ifPresent(telescope -> {
            telescope.setStatus(status);
            telescopeRepository.save(telescope);
        });
    }

    public void updateMaintenanceDate(Long id) {
        telescopeRepository.findById(id).ifPresent(telescope -> {
            telescope.setLastMaintenanceDate(LocalDate.now());
            telescopeRepository.save(telescope);
        });
    }

    // Сложная бизнес-логика (то что не получилось в репозитории)
    public List<Telescope> getTelescopesNeedingMaintenance() {
        LocalDate thresholdDate = LocalDate.now().minusDays(30);
        List<Telescope> neverMaintained = telescopeRepository.findByLastMaintenanceDateIsNull();
        List<Telescope> oldMaintenance = telescopeRepository.findByLastMaintenanceDateBefore(thresholdDate);

        neverMaintained.addAll(oldMaintenance);
        return neverMaintained;
    }
}