package org.egov.receipt.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptReq {

    @NotNull
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @NotNull
    @JsonProperty("Receipt")
    @Valid
    private List<Receipt> receipt = null;


}
