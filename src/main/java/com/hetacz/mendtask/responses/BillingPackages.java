package com.hetacz.mendtask.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BillingPackages {

    @PositiveOrZero(message = "total_gigabytes_bandwidth_used should be non negative")
    @JsonProperty("total_gigabytes_bandwidth_used")
    Integer freeGbUsed;
    @PositiveOrZero(message = "total_paid_gigabytes_bandwidth_used should be non negative")
    @JsonProperty("total_paid_gigabytes_bandwidth_used")
    Integer paidGbUsed;
    @Positive(message = "included_gigabytes_bandwidth should be positive")
    @JsonProperty("included_gigabytes_bandwidth")
    Double gbBandwidth;
}
