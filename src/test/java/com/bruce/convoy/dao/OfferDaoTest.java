package com.bruce.convoy.dao;

import com.bruce.convoy.model.Offer;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfferDaoTest extends JdbiUnitTest{

    private DriverDao driverDao;
    private OfferDao offerDao;
    private int shipmentId = 3810;

    protected void setUpDataAccessObjects() {
        driverDao = dbi.onDemand(DriverDao.class);
        offerDao = dbi.onDemand(OfferDao.class);
        driverDao.createDriverTable();
        offerDao.createOfferTable();
    }

    @Test
    public void test() {
        int driverWithMoreOffers = 1;
        int driverWithLessOffers = 2;

        List<Integer> driverIds = Lists.newArrayList(driverWithMoreOffers, driverWithLessOffers);

        int moreOffers = 10;
        int lessOffers = moreOffers - 1;
        for (int i = 0; i < moreOffers; i++) {
            insertOffer(driverWithMoreOffers);
        }

        for (int i = 0; i < lessOffers; i++) {
            insertOffer(driverWithLessOffers);
        }

        List<Offer> offers = offerDao.getAcceptedOffersByDrivers(driverIds);
        assertThat(offers.size(), is(lessOffers + moreOffers));
    }


    @Test
    public void testDriversWithLessOfferHistoryComesFirst() {
        //long sameCapacity = 100;
        //int diverWithMoreOffers = insertDriver(sameCapacity);
        //int diverWithLessOffers = insertDriver(sameCapacity);
        //
        //int moreOffers = 10;
        //int lessOffers = moreOffers - 1;
        //for (int i = 0; i < moreOffers; i++) {
        //    insertOffer(diverWithMoreOffers);
        //}
        //
        //for (int i = 0; i < lessOffers; i++) {
        //    insertOffer(diverWithLessOffers);
        //}
        ////
        //List<Driver> actualDriverList = driverDao.findDriversWithCapacity(sameCapacity);
        //assertThat("Both drivers that meet the capacity need to be selected",
        //           actualDriverList.size(), is(2));
        //
        //Driver firstDriver = actualDriverList.get(0);
        //assertThat("driver with less previous offers should come first",
        //           firstDriver.getId(), is(diverWithLessOffers));
        //
        //Driver secondDriver = actualDriverList.get(1);

    }

    private int insertOffer(final int driverId) {
        return offerDao.insert(shipmentId, driverId, true);
    }
}