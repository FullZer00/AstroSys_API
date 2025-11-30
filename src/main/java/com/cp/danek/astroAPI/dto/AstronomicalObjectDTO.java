package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.AstronomicalObjectType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для представления астрономического объекта")
public class AstronomicalObjectDTO {
    @Schema(description = "Id астрономического объекта", example = "1")
    private Long id;

    @Schema(description = "Стандартный ответ API")
    private String name;

    @Schema(description = "Тип астрономического объекта")
    private AstronomicalObjectType type;

    @Schema(description = "Созвездие", example = "Большая медведица")
    private String constellation;

    @Schema(description = "Координаты астрономического объекта", example = "α 02h31m48.70s δ +89Â°15'51.0\"")
    private String coordinates;

    @Schema(description = "Видимая звездная величина астрономического объекта", example = "100.5")
    private Double magnitude;

    @Schema(description = "Расстояние в световых годах", example = "1000.3")
    private Double distance;

    @Schema(description = "Описание астрономического объекта", example = "Большая Медведица (лат. Ursa Major) — созвездие северного полушария неба")
    private String description;

    public AstronomicalObjectDTO() {}

    public AstronomicalObjectDTO(Long id, String name, AstronomicalObjectType type, String constellation, String coordinates, Double magnitude, Double distance, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.constellation = constellation;
        this.coordinates = coordinates;
        this.magnitude = magnitude;
        this.distance = distance;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AstronomicalObjectType getType() {
        return type;
    }

    public void setType(AstronomicalObjectType type) {
        this.type = type;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
