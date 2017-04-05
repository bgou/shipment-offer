package com.bruce.convoy.resources;

import com.bruce.convoy.dao.DaoFactory;
import com.bruce.convoy.dao.DriverDao;
import com.bruce.convoy.dao.OfferDao;
import com.bruce.convoy.model.ErrorResponse;
import com.bruce.convoy.model.Offer;
import com.codahale.metrics.annotation.Timed;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/offer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OfferResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferResource.class);

    @Getter(AccessLevel.PROTECTED)
    protected final DriverDao driverDao;

    @Getter(AccessLevel.PROTECTED)
    protected final OfferDao offerDao;

    public OfferResource(final DaoFactory daoFactory) {
        this.driverDao = daoFactory.getDriverDao();
        this.offerDao = daoFactory.getOfferDao();
    }

    @PUT
    @Timed
    @Path("/{id}")
    public Response acceptOffer(@PathParam("id") final int offerId) {
        Offer offer = getOfferDao().getByOfferId(offerId);
        if (offer == null)
            return badrequest("offerId " + offerId + " not found");

        LOGGER.info("Checking if this shipment has already been accepted");
        Offer acceptedOffer = getOfferDao().getAcceptedOfferByShipmentId(offer.getShipmentId());
        if (acceptedOffer != null) {
            if (acceptedOffer.getDriverId() != offer.getDriverId()) {
                return badrequest("shipmentId " + offer.getShipmentId() + " has been accepted by " + offer.getDriverId());
            }
        } else {
            getOfferDao().updateOffer(offerId, true);
        }

        return Response.ok().build();
    }

    private Response badrequest(final String error) {
        ErrorResponse r = new ErrorResponse();
        r.setError(error);
        return Response.status(Response.Status.BAD_REQUEST).entity(r).build();
    }
}
