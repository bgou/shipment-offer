package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data

public class DriverOffer {
    @JsonProperty
    private String offerId;
    @JsonProperty
    private String shipmentId;

    public static class Mapper implements ResultSetMapper<DriverOffer> {
        public DriverOffer map(final int i, final ResultSet set, final StatementContext context) throws SQLException {
            DriverOffer driverOffer = new DriverOffer();
            driverOffer.setOfferId(set.getString("id"));
            driverOffer.setShipmentId(set.getString("shipmentId"));
            return driverOffer;
        }
    }
}
