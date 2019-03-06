package org.egov.encryption.models;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Attribute {

    private String jsonPath;
    private String maskingTechnique;

    public Attribute(String jsonPath) {
        this.jsonPath = jsonPath;
    }

}