package com.bruce.convoy.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface ShipmentDao {
    @SqlUpdate("CREATE TABLE shipment (id int primary key auto_increment, capacity int)")
    void createShipmentTable();

    @SqlUpdate("INSERT INTO shipment (capacity) values (:capacity)")
    @GetGeneratedKeys
    int insert(@Bind("capacity") final int capacity);
}
