package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {
    @JsonProperty
    private int offerId;

    @JsonProperty
    private int driverId;

    @JsonIgnore
    private int shipmentId;

    @JsonIgnore
    private boolean accepted;

    public static class Mapper implements ResultSetMapper<Offer> {
        public Offer map(final int i, final ResultSet set, final StatementContext context) throws SQLException {
            Offer offer = new Offer();
            offer.setOfferId(set.getInt("id"));
            offer.setDriverId(set.getInt("driverId"));
            offer.setShipmentId(set.getInt("shipmentId"));
            offer.setAccepted(set.getBoolean("accepted"));
            return offer;
        }
    }
}
