package com.cp.danek.astroAPI.mapper;

import com.cp.danek.astroAPI.dto.CreateUserDTO;
import com.cp.danek.astroAPI.dto.TelescopeDTO;
import com.cp.danek.astroAPI.dto.UserDTO;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.entities.User;
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

    public User toUserEntity(CreateUserDTO request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setPasswordHash(request.getPassword()); // В реальном приложении нужно хэшировать!
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
}