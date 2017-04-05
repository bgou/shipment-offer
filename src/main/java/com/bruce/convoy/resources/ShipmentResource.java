package com.bruce.convoy.resources;

import com.bruce.convoy.dao.DaoFactory;
import com.bruce.convoy.dao.DriverDao;
import com.bruce.convoy.dao.OfferDao;
import com.bruce.convoy.dao.ShipmentDao;
import com.bruce.convoy.model.CapacityRequest;
import com.bruce.convoy.model.Driver;
import com.bruce.convoy.model.ErrorResponse;
import com.bruce.convoy.model.Offer;
import com.bruce.convoy.model.ShipmentCreateResponse;
import com.bruce.convoy.model.ShipmentGetResponse;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/shipment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShipmentResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentResource.class);

    @Getter(AccessLevel.PROTECTED) private final DriverDao driverDao;
    @Getter(AccessLevel.PROTECTED) private final ShipmentDao shipmentDao;
    @Getter(AccessLevel.PROTECTED) private final OfferDao offerDao;

    public ShipmentResource(final DaoFactory daoFactory) {
        Preconditions.checkNotNull(daoFactory);
        this.shipmentDao = daoFactory.getShipmentDao();
        this.driverDao = daoFactory.getDriverDao();
        this.offerDao = daoFactory.getOfferDao();
    }

    /**
     * Test only constructor
     */
    ShipmentResource() {
        this.shipmentDao = null;
        this.driverDao = null;
        this.offerDao = null;
    }

    /**
     * Creates a new shipment and creates offers to the top 10 eligible drivers.
     * A driver is eligible if they have a truck that can carry the required capacity for the shipment.
     * To be fair to drivers, drivers should be sorted by how many offers that they have received in the past.
     * Drivers who have had less opportunity to accept jobs should be put to the top.
     */
    @POST
    public Response createShipment(final CapacityRequest request) {

        // find drivers
        int capacity = request.getCapacity();
        LOGGER.info("Getting drivers that has capacity {}", capacity);
        List<Driver> drivers = getDriverDao().findDriversWithCapacity(capacity);

        if (drivers == null || drivers.isEmpty()) {
            String msg = String.format("No driver found that can serve capacity %d, " +
                                       "not saving shipment into database", capacity);
            LOGGER.info(msg);
            return convertOffersToResponse(-1, Lists.newArrayList());
        }

        LOGGER.info("{} drivers found", drivers.size());

        int shipmentId = getShipmentDao().insert(capacity);
        LOGGER.info("Saved shipment id {}", shipmentId);

        LOGGER.info("Sorting drivers by their past offers");
        orderDriversByPreviousOffers(drivers);

        List<Offer> offers = writeOffersToDb(shipmentId, drivers);
        LOGGER.info("Saved {} offers", offers.size());

        return convertOffersToResponse(shipmentId, offers);
    }

    @GET
    @Path("/{shipmentId}")
    public Response getShipment(@PathParam("shipmentId") final int shipmentId) {
        // get accepted offer for the shipment
        List<Offer> allOffers = getOfferDao().getAllOfferByShipmentId(shipmentId);
        if (allOffers==null || allOffers.isEmpty()) {
            ErrorResponse e = new ErrorResponse();
            e.setError("no offer found for shipmentId " + shipmentId);
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
        ShipmentGetResponse r = new ShipmentGetResponse();

        for (Offer o : allOffers) {
            if (o.isAccepted()) {
                r.setAccepted(true);
                r.setOffers(Lists.newArrayList(o));
                return Response.ok(r).build();
            }
        }

        r.setAccepted(false);
        r.setOffers(allOffers);
        return Response.ok(r).build();
    }

    protected void orderDriversByPreviousOffers(final List<Driver> drivers) {
        List<Integer> driverIds = extractDriverIds(drivers);

        List<Offer> offerList = getOfferDao().getAcceptedOffersByDrivers(driverIds);

        if (offerList == null || offerList.isEmpty()) {
            return;
        }

        // TODO this can probably be cached
        final Map<Integer, Integer> driverIdToJobsMap = Maps.newHashMap();
        for(Offer o : offerList) {
            final int driverId = o.getDriverId();
            if (driverIdToJobsMap.containsKey(driverId)) {
                driverIdToJobsMap.put(driverId, driverIdToJobsMap.get(driverId)+1);
            } else {
                driverIdToJobsMap.put(driverId, 1);
            }
        }
        drivers.sort((o1, o2) -> {
            Integer driver1Offers = driverIdToJobsMap.get(o1.getId());
            Integer driver2Offers = driverIdToJobsMap.get(o2.getId());
            if (driver1Offers == null && driver2Offers == null) {
                return 0;
            }
            if (driver1Offers == null) {
                return -1;
            }
            if (driver2Offers == null) {
                return 1;
            }

            return driver1Offers - driver2Offers;
        });
    }

    private Response convertOffersToResponse(final int shipmentId, final List<Offer> offers) {
        ShipmentCreateResponse response = new ShipmentCreateResponse();
        response.setId(shipmentId);
        response.setOffers(offers);
        return Response.ok(response).build();
    }

    private List<Offer> writeOffersToDb(final int shipmentId, final List<Driver> drivers) {
        List<Offer> offerList = Lists.newArrayList();
        for(Driver d : drivers) {
            int offerId = getOfferDao().insert(shipmentId, d.getId(), false);
            Offer o = new Offer();
            o.setOfferId(offerId);
            o.setDriverId(d.getId());
            o.setAccepted(false);
            offerList.add(o);
        }
        return offerList;
    }

    private List<Integer> extractDriverIds(final List<Driver> drivers) {
        List<Integer> dids = Lists.newArrayList();
        for (Driver d : drivers) {
            dids.add(d.getId());
        }
        return dids;
    }
}
