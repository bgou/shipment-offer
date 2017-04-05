package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreatedResponse {
    @JsonProperty
    private String id;

    public CreatedResponse(final int id) {
        this.id = String.valueOf(id);
    }
}
