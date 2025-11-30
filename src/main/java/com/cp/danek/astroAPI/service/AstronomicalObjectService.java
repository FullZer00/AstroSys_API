package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.model.entities.AstronomicalObject;
import com.cp.danek.astroAPI.model.enums.AstronomicalObjectType;
import com.cp.danek.astroAPI.model.repositories.AstronomicalObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AstronomicalObjectService {

    private final AstronomicalObjectRepository astronomicalObjectRepository;

    @Autowired
    public AstronomicalObjectService(AstronomicalObjectRepository astronomicalObjectRepository) {
        this.astronomicalObjectRepository = astronomicalObjectRepository;
    }

    // Основные CRUD операции
    public List<AstronomicalObject> getAllObjects() {
        return astronomicalObjectRepository.findAll();
    }

    public Optional<AstronomicalObject> getObjectById(Long id) {
        return astronomicalObjectRepository.findById(id);
    }

    public AstronomicalObject createObject(AstronomicalObject object) {
        return astronomicalObjectRepository.save(object);
    }

    public AstronomicalObject updateObject(Long id, AstronomicalObject objectDetails) {
        return astronomicalObjectRepository.findById(id)
                .map(object -> {
                    object.setName(objectDetails.getName());
                    object.setType(objectDetails.getType());
                    object.setConstellation(objectDetails.getConstellation());
                    object.setCoordinates(objectDetails.getCoordinates());
                    object.setMagnitude(objectDetails.getMagnitude());
                    object.setDistance(objectDetails.getDistance());
                    object.setDescription(objectDetails.getDescription());
                    return astronomicalObjectRepository.save(object);
                })
                .orElseThrow(() -> new RuntimeException("Астрономический объект не найден с id: " + id));
    }

    public void deleteObject(Long id) {
        astronomicalObjectRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<AstronomicalObject> searchObjectsByName(String name) {
        return astronomicalObjectRepository.findByNameContainingIgnoreCase(name);
    }

    public List<AstronomicalObject> getObjectsByType(AstronomicalObjectType type) {
        return astronomicalObjectRepository.findByType(type);
    }

    public List<AstronomicalObject> getObjectsByConstellation(String constellation) {
        return astronomicalObjectRepository.findByConstellationContainingIgnoreCase(constellation);
    }

    public List<AstronomicalObject> getBrightObjects(Double maxMagnitude) {
        return astronomicalObjectRepository.findByMagnitudeLessThanEqual(maxMagnitude);
    }

    public List<AstronomicalObject> getObjectsInDistanceRange(Double minDistance, Double maxDistance) {
        return astronomicalObjectRepository.findByDistanceBetween(minDistance, maxDistance);
    }

    public List<AstronomicalObject> getPopularObjects() {
        return astronomicalObjectRepository.findPopularObjects();
    }
}