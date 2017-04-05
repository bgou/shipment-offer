package com.bruce.convoy.resources;

import com.bruce.convoy.dao.DaoFactory;
import com.bruce.convoy.dao.DriverDao;
import com.bruce.convoy.dao.OfferDao;
import com.bruce.convoy.model.CapacityRequest;
import com.bruce.convoy.model.CreatedResponse;
import com.bruce.convoy.model.DriverOffer;
import com.codahale.metrics.annotation.Timed;
import lombok.AccessLevel;
import lombok.Getter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/driver")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DriverResource {

    @Getter(AccessLevel.PROTECTED)
    protected final DriverDao driverDao;

    @Getter(AccessLevel.PROTECTED)
    protected final OfferDao offerDao;

    public DriverResource(final DaoFactory daoFactory) {
        this.driverDao = daoFactory.getDriverDao();
        this.offerDao = daoFactory.getOfferDao();
    }

    @POST
    @Timed
    public CreatedResponse createDriver(final CapacityRequest request) {
        int id = getDriverDao().insert(request.getCapacity());
        return new CreatedResponse(id);
    }

    @GET
    @Timed
    @Path("/{id}")
    public List<DriverOffer> getOffers(@PathParam("id") final int driverId) {
        return getOfferDao().getDriverOffers(driverId);
    }
}
