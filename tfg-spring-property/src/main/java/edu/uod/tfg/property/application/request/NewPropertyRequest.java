package edu.uod.tfg.property.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.uod.tfg.property.domain.model.PropertyType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NewPropertyRequest {

    @Getter
    @NotNull
    private final PropertyType type;

    @Getter
    @NotNull
    private final String description;

    @Getter
    @NotNull
    private final Integer rooms;

    @Getter
    @NotNull
    private final Integer baths;

    @Getter
    @NotNull
    private final Float surface;

    @Getter
    @NotNull
    private final String registryDocumentUrl;

    @Getter
    @NotNull
    private final String catastralReference;

    @Getter
    @NotNull
    private final Double latitude;

    @Getter
    @NotNull
    private final Double longitude;

    @Getter
    @NotNull
    private final Long userId;

    @Getter
    @NotNull
    private final String address;

    @Getter
    private final List<String> imageUrls;

    @Getter
    @NotNull
    private final String postalCode;

    @Getter
    @NotNull
    private final String city;

    @Getter
    @NotNull
    private final String region;

    @Getter
    @NotNull
    private final String country;

    @JsonCreator
    public NewPropertyRequest(
            @JsonProperty("type") @NotNull final String type,
            @JsonProperty("description") @NotNull final String description,
            @JsonProperty("rooms") @NotNull final Integer rooms,
            @JsonProperty("baths") @NotNull final Integer baths,
            @JsonProperty("surface") @NotNull final Float surface,
            @JsonProperty("registryDocumentUrl") @NotNull final String registryDocumentUrl,
            @JsonProperty("catastralReference") @NotNull final String catastralReference,
            @JsonProperty("latitude") @NotNull final Double latitude,
            @JsonProperty("longitude") @NotNull final Double longitude,
            @JsonProperty("userId") @NotNull final Long userId,
            @JsonProperty("address") @NotNull final String address,
            @JsonProperty("imageUrls") final List<String> imageUrls,
            @JsonProperty("postalCode") @NotNull final String postalCode,
            @JsonProperty("city") @NotNull final String city,
            @JsonProperty("region") @NotNull final String region,
            @JsonProperty("country") @NotNull final String country) {

        this.type = PropertyType.valueOf(type);
        this.description = description;
        this.rooms = rooms;
        this.baths = baths;
        this.surface = surface;
        this.registryDocumentUrl = registryDocumentUrl;
        this.catastralReference = catastralReference;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.address = address;
        this.imageUrls = imageUrls;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
        this.country = country;
    }
}
