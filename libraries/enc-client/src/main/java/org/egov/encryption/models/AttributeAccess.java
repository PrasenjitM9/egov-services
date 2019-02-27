package org.egov.encryption.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeAccess {

    private Attribute attribute;
    private AccessType accessType;

}