package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentGetResponse {
    @JsonProperty("accepted")
    private boolean accepted;

    @JsonProperty
    private List<Offer> offers = Lists.newArrayList();
}
