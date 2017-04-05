package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OfferUpdateResponse {
    @JsonProperty
    private String status;
}
