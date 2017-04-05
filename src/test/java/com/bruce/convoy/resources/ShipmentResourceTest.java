package com.bruce.convoy.resources;

import com.bruce.convoy.dao.DaoFactory;
import com.bruce.convoy.dao.DriverDao;
import com.bruce.convoy.dao.OfferDao;
import com.bruce.convoy.dao.ShipmentDao;
import com.bruce.convoy.model.CapacityRequest;
import com.bruce.convoy.model.Driver;
import com.bruce.convoy.model.Offer;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShipmentResourceTest extends ShipmentResource{
    @Mock private DriverDao driverDao;
    @Mock private ShipmentDao shipmentDao;
    @Mock private OfferDao offerDao;
    @Captor ArgumentCaptor<List<Integer>> listCaptor;
    private DaoFactory daoFactory = new DaoFactory();
    private int testCapacity = 10;

    ShipmentResource shipmentResource;
    private List<Integer> driverIds;
    private List<Driver> drivers;
    private List<Offer> offers;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        daoFactory.setDriverDao(driverDao);
        daoFactory.setOfferDao(offerDao);
        daoFactory.setShipmentDao(shipmentDao);
        shipmentResource = new ShipmentResource(daoFactory);
        driverIds = Lists.newArrayList(10, 20);
        drivers = createDrivers(driverIds);
        offers = Lists.newArrayList();
    }

    @Test
    public void testRetrieveOffersByDriverId() {
        when(driverDao.findDriversWithCapacity(anyInt())).thenReturn(drivers);

        Response actualRes = createShipment(new CapacityRequest(testCapacity));
        assertThat(actualRes.getStatus(), is(Response.Status.OK.getStatusCode()));

        verify(offerDao).getAcceptedOffersByDrivers(listCaptor.capture());

        assertThat(listCaptor.getValue(), is(driverIds));
    }

    @Test
    public void testDriversAreSortedByLeastOffers() {
        driverIds = Lists.newArrayList(38, 20, 10);
        drivers = createDrivers(driverIds);
        Driver moreOfferDriver = drivers.get(0);
        Driver lessOfferDriver = drivers.get(1);
        Driver leastOfferDriver = drivers.get(2);
        int offerCount = 10;
        int lessOffers = offerCount - 1;
        int leastOffers = lessOffers - 1;
        setupOffers(moreOfferDriver, offerCount);
        setupOffers(lessOfferDriver, lessOffers);
        setupOffers(leastOfferDriver, leastOffers);
        when(offerDao.getAcceptedOffersByDrivers(anyListOf(Integer.class))).thenReturn(offers);

        orderDriversByPreviousOffers(drivers);

        Driver firstDriver = drivers.get(0);
        Driver secondDriver = drivers.get(1);
        Driver thirdDriver = drivers.get(2);
        assertThat(firstDriver, sameInstance(leastOfferDriver));
        assertThat(secondDriver, sameInstance(lessOfferDriver));
        assertThat(thirdDriver, sameInstance(moreOfferDriver));
    }

    private void setupOffers(final Driver driver, final int offerCount) {
        for (int i = 0; i < offerCount; i++) {
            Offer o = new Offer();
            o.setDriverId(driver.getId());
            offers.add(o);
        }
    }

    private Driver getDriver(int id) {
        Driver driver = new Driver();
        driver.setId(id);
        return driver;
    }

    private List<Driver> createDrivers(List<Integer> driverIds){
        List<Driver> drivers = Lists.newArrayList();
        for(int id: driverIds) {
            drivers.add(getDriver(id));
        }
        return drivers;
    }

    @Override
    protected DriverDao getDriverDao() {
        return driverDao;
    }

    @Override
    protected ShipmentDao getShipmentDao() {
        return shipmentDao;
    }

    @Override
    protected OfferDao getOfferDao() {
        return offerDao;
    }
}
