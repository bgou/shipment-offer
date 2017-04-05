package com.bruce.convoy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Driver {
    @JsonProperty
    private int id;

    @JsonProperty
    private long capacity;

    public static class Mapper implements ResultSetMapper<Driver> {

        public Driver map(final int i, final ResultSet set, final StatementContext context) throws SQLException {
            Driver driver = new Driver();
            driver.setId(set.getInt("id"));
            driver.setCapacity(set.getLong("capacity"));
            return driver;
        }
    }
}
