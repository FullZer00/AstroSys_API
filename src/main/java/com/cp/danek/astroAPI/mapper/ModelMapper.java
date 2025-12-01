package com.cp.danek.astroAPI.mapper;

import com.cp.danek.astroAPI.dto.*;
import com.cp.danek.astroAPI.model.entities.*;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    // User мапперы
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole() != null ? user.getRole().getName() : null,
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getIsActive()
        );
    }

    public User toUserEntity(RegistrationRequestDTO request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        return user;
    }

    public User toUserEntity(CreateUserDTO request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        // Роль устанавливается в сервисе/контроллере

        return user;
    }

    // Telescope мапперы
    public TelescopeDTO toTelescopeDTO(Telescope telescope) {
        if (telescope == null) {
            return null;
        }

        return new TelescopeDTO(
                telescope.getId(),
                telescope.getName(),
                telescope.getType(),
                telescope.getAperture(),
                telescope.getFocalLength(),
                telescope.getLocation(),
                telescope.getStatus(),
                telescope.getLastMaintenanceDate(),
                telescope.getMaxResolution()
        );
    }

    public Telescope toTelescopeEntity(TelescopeDTO telescopeDTO) {
        if (telescopeDTO == null) {
            return null;
        }

        Telescope telescope = new Telescope();
        telescope.setName(telescopeDTO.getName());
        telescope.setType(telescopeDTO.getType());
        telescope.setAperture(telescopeDTO.getAperture());
        telescope.setFocalLength(telescopeDTO.getFocalLength());
        telescope.setLocation(telescopeDTO.getLocation());
        telescope.setStatus(telescopeDTO.getStatus());
        telescope.setMaxResolution(telescopeDTO.getMaxResolution());

        return telescope;
    }

    // Методы для обновления сущностей (без создания новых)
    public void updateUserFromRequest(User user, CreateUserDTO request) {
        if (user == null || request == null) {
            return;
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        // Логин и пароль обычно не обновляются через этот метод
    }

    public void updateTelescopeFromDTO(Telescope telescope, TelescopeDTO telescopeDTO) {
        if (telescope == null || telescopeDTO == null) {
            return;
        }

        telescope.setName(telescopeDTO.getName());
        telescope.setType(telescopeDTO.getType());
        telescope.setAperture(telescopeDTO.getAperture());
        telescope.setFocalLength(telescopeDTO.getFocalLength());
        telescope.setLocation(telescopeDTO.getLocation());
        telescope.setStatus(telescopeDTO.getStatus());
        telescope.setMaxResolution(telescopeDTO.getMaxResolution());
    }

    // Astronomical object мапперы
    public AstronomicalObjectDTO toAstronomicalObjectDTO(AstronomicalObject astronomicalObject) {
        if (astronomicalObject == null) {
            return null;
        }

        AstronomicalObjectDTO dto = new AstronomicalObjectDTO();
        dto.setId(astronomicalObject.getId());
        dto.setName(astronomicalObject.getName());
        dto.setType(astronomicalObject.getType());
        dto.setDescription(astronomicalObject.getDescription());
        dto.setConstellation(astronomicalObject.getConstellation());
        dto.setCoordinates(astronomicalObject.getCoordinates());
        dto.setMagnitude(astronomicalObject.getMagnitude());
        dto.setType(astronomicalObject.getType());

        return dto;
    }

    public AstronomicalObject toAstronomicalObjectEntity(AstronomicalObjectDTO astronomicalObjectDTO) {
        if (astronomicalObjectDTO == null) {
            return null;
        }

        AstronomicalObject entity = new AstronomicalObject();
        entity.setId(astronomicalObjectDTO.getId());
        entity.setName(astronomicalObjectDTO.getName());
        entity.setType(astronomicalObjectDTO.getType());
        entity.setDescription(astronomicalObjectDTO.getDescription());
        entity.setConstellation(astronomicalObjectDTO.getConstellation());
        entity.setCoordinates(astronomicalObjectDTO.getCoordinates());
        entity.setMagnitude(astronomicalObjectDTO.getMagnitude());
        entity.setType(astronomicalObjectDTO.getType());

        return entity;
    }

    public void updateAstronomicalObjectFromDTO(AstronomicalObject object, AstronomicalObjectDTO objectDTO) {
        if (object == null || objectDTO == null) {
            return;
        }

        object.setName(objectDTO.getName());
        object.setType(objectDTO.getType());
        object.setConstellation(objectDTO.getConstellation());
        object.setCoordinates(objectDTO.getCoordinates());
        object.setMagnitude(objectDTO.getMagnitude());
        object.setDistance(objectDTO.getDistance());
        object.setDescription(objectDTO.getDescription());
    }


    // Observation мапперы
    public ObservationDTO toObservationDTO(Observation observation) {
        return new ObservationDTO(
                observation.getId(),
                observation.getTelescope() != null ? observation.getTelescope().getId() : null,
                observation.getTelescope() != null ? observation.getTelescope().getName() : null,
                observation.getAstronomer() != null ? observation.getAstronomer().getId() : null,
                observation.getAstronomer() != null ? observation.getAstronomer().getFullName() : null,
                observation.getTargetObject() != null ? observation.getTargetObject().getId() : null,
                observation.getTargetObject() != null ? observation.getTargetObject().getName() : null,
                observation.getStartTime(),
                observation.getEndTime(),
                observation.getWeatherConditions(),
                observation.getStatus(),
                observation.getNotes(),
                observation.getDataPath()
        );
    }

    // ObservationRequest мапперы
    public ObservationRequestDTO toObservationRequestDTO(ObservationRequest request) {
        return new ObservationRequestDTO(
                request.getId(),
                request.getUser() != null ? request.getUser().getId() : null,
                request.getUser() != null ? request.getUser().getFullName() : null,
                request.getTelescope() != null ? request.getTelescope().getId() : null,
                request.getTelescope() != null ? request.getTelescope().getName() : null,
                request.getTargetObject() != null ? request.getTargetObject().getId() : null,
                request.getTargetObject() != null ? request.getTargetObject().getName() : null,
                request.getRequestedTime(),
                request.getDurationHours(),
                request.getPriority(),
                request.getStatus(),
                request.getScientificJustification(),
                request.getCreatedAt(),
                request.getApprovedBy() != null ? request.getApprovedBy().getId() : null,
                request.getApprovedBy() != null ? request.getApprovedBy().getFullName() : null
        );
    }

    // Maintenance мапперы
    public MaintenanceDTO toMaintenanceDTO(Maintenance maintenance) {
        return new MaintenanceDTO(
                maintenance.getId(),
                maintenance.getEquipment() != null ? maintenance.getEquipment().getId() : null,
                maintenance.getEquipment() != null ? maintenance.getEquipment().getName() : null,
                maintenance.getEngineer() != null ? maintenance.getEngineer().getId() : null,
                maintenance.getEngineer() != null ? maintenance.getEngineer().getFullName() : null,
                maintenance.getMaintenanceType(),
                maintenance.getStartTime(),
                maintenance.getEndTime(),
                maintenance.getDescription(),
                maintenance.getStatus(),
                maintenance.getPartsUsed(),
                maintenance.getCost()
        );
    }

    // Observation Data мапперы
    public ObservationDataDTO toObservationDataDTO(ObservationData data) {
        if (data == null) {
            return null;
        }

        return new ObservationDataDTO(
                data.getId(),
                data.getObservation() != null ? data.getObservation().getId() : null,
                data.getFileName(),
                data.getFileSize(),
                data.getFileType(),
                data.getUploadTime(),
                data.getDataQuality(),
                data.getProcessingStatus(),
                data.getDownloadCount()
        );
    }

    public ObservationData toObservationDataEntity(ObservationDataDTO dataDTO) {
        if (dataDTO == null) {
            return null;
        }

        ObservationData data = new ObservationData();
        data.setFileName(dataDTO.getFileName());
        data.setFileSize(dataDTO.getFileSize());
        data.setFileType(dataDTO.getFileType());
        data.setDataQuality(dataDTO.getDataQuality());
        data.setProcessingStatus(dataDTO.getProcessingStatus());
        data.setDownloadCount(dataDTO.getDownloadCount());

        return data;
    }

    public ObservationData toObservationDataEntity(CreateObservationDataDTO dataDTO) {
        if (dataDTO == null) {
            return null;
        }

        ObservationData data = new ObservationData();
        data.setFileName(dataDTO.getFileName());
        data.setFileSize(dataDTO.getFileSize());
        data.setFileType(dataDTO.getFileType());

        return data;
    }

    public void updateObservationDataFromDTO(ObservationData data, ObservationDataDTO dataDTO) {
        if (data == null || dataDTO == null) {
            return;
        }

        data.setFileName(dataDTO.getFileName());
        data.setFileSize(dataDTO.getFileSize());
        data.setFileType(dataDTO.getFileType());
        data.setDataQuality(dataDTO.getDataQuality());
        data.setProcessingStatus(dataDTO.getProcessingStatus());
    }
}