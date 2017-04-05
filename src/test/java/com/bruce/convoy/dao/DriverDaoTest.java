package com.bruce.convoy.dao;

import com.bruce.convoy.model.Driver;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DriverDaoTest extends JdbiUnitTest{
    private DriverDao driverDao;
    private OfferDao offerDao;

    @Override
    protected void setUpDataAccessObjects() {
        driverDao = dbi.onDemand(DriverDao.class);
        offerDao = dbi.onDemand(OfferDao.class);
        driverDao.createDriverTable();
        offerDao.createOfferTable();
    }

    @Test
    public void testGetDriverMeetsCapacity() {
        long capacity = 20;
        int idMeetsCapacity = insertDriver(capacity);
        insertDriver(capacity - 1);

        List<Driver> drivers = driverDao.findDriversWithCapacity(capacity);

        assertThat(drivers.size(), is(1));
        final Driver actualDriver = drivers.get(0);
        assertThat("There should only be 1 driver that meets the capacity",
                   actualDriver.getCapacity(), is(capacity));
        assertThat("The driver id that meets the criteria is not right",actualDriver.getId(), is(idMeetsCapacity));
    }

    private int insertDriver(final long capacity) {
        return driverDao.insert(capacity);
    }

}