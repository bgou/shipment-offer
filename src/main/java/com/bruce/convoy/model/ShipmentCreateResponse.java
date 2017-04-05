package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentCreateResponse {
    @JsonProperty
    private int id;

    @JsonProperty
    private List<Offer> offers = Lists.newArrayList();
}
