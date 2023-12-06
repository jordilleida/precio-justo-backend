package edu.uoc.epcsd.communication.domain;

import lombok.*;


@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    private Long id;

    private String description;
    private Integer rooms;
    private Integer baths;
    private Float surface;
    private String registryDocumentUrl;
    private String catastralReference;

    private Double latitude;
    private Double longitude;
    private Long userId;
    private String contact;
    private String address;
}
