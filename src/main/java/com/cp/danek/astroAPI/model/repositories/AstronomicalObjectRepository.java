package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.AstronomicalObject;
import com.cp.danek.astroAPI.model.enums.AstronomicalObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AstronomicalObjectRepository extends JpaRepository<AstronomicalObject, Long> {

    // Поиск по названию
    List<AstronomicalObject> findByNameContainingIgnoreCase(String name);

    // Поиск по типу объекта
    List<AstronomicalObject> findByType(AstronomicalObjectType type);

    // Поиск по созвездию
    List<AstronomicalObject> findByConstellationContainingIgnoreCase(String constellation);

    // Поиск объектов с видимой величиной меньше (более яркие)
    List<AstronomicalObject> findByMagnitudeLessThanEqual(Double maxMagnitude);

    // Поиск объектов в диапазоне расстояний
    List<AstronomicalObject> findByDistanceBetween(Double minDistance, Double maxDistance);

    // Поиск по координатам (приблизительный)
    @Query("SELECT ao FROM AstronomicalObject ao WHERE ao.coordinates LIKE %:coords%")
    List<AstronomicalObject> findByCoordinatesContaining(@Param("coords") String coordinates);

    // Поиск популярных объектов (по количеству наблюдений)
    @Query("SELECT ao FROM AstronomicalObject ao ORDER BY size(ao.observations) DESC")
    List<AstronomicalObject> findPopularObjects();
}