package com.bruce.convoy.dao;

import com.bruce.convoy.model.Driver;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(Driver.Mapper.class)
public interface DriverDao {
    @SqlUpdate("CREATE TABLE driver (id int primary key auto_increment, capacity int);")
    void createDriverTable();

    @SqlUpdate("INSERT INTO driver (capacity) values (:capacity)")
    @GetGeneratedKeys
    int insert(@Bind("capacity") long capacity);

    @SqlQuery("SELECT * " +
              " FROM driver d " +
              " WHERE d.capacity >= :capacity ")
    List<Driver> findDriversWithCapacity(@Bind("capacity") long capacity);
}
