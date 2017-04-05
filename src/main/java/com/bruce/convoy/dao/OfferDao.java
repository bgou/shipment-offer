package com.bruce.convoy.dao;

import com.bruce.convoy.model.DriverOffer;
import com.bruce.convoy.model.Offer;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(Offer.Mapper.class)
public interface OfferDao {
    @SqlUpdate("CREATE TABLE offer (" +
               " id int primary key auto_increment, " +
               " shipmentId int NOT NULL, " +
               " driverId varchar(255) NOT NULL, " +
               " accepted tinyint DEFAULT 0" +
               ")")
    void createOfferTable();

    @SqlUpdate("INSERT INTO offer (shipmentId, driverId, accepted) values (:shipmentId, :driverId, :accepted)")
    @GetGeneratedKeys
    int insert(@Bind("shipmentId") int shipmentId, @Bind("driverId") final int driverId, @Bind("accepted") final boolean accepted);

    @SqlQuery("SELECT * " +
              " FROM offer o " +
              " WHERE o.id = :offerId")
    Offer getByOfferId(@Bind("offerId") final int offerId);

    @SqlQuery("SELECT * " +
              " FROM offer o " +
              " WHERE o.driverId IN (<driverIds>)" +
              "   AND o.accepted = 1 ")
    List<Offer> getAcceptedOffersByDrivers(@BindIn("driverIds") final List<Integer> driverIds);

    @SqlQuery("SELECT * " +
              " FROM offer o " +
              " WHERE o.driverId = :driverId" +
              " AND o.accepted = 0 " +
              " AND o.shipmentId NOT IN" +
              " ( " +
              "  SELECT a.shipmentId " +
              "   FROM offer a " +
              "   WHERE a.accepted = 1" +
              " ) ")
    @RegisterMapper(DriverOffer.Mapper.class)
    List<DriverOffer> getDriverOffers(@Bind("driverId") final int driverId);

    @SqlQuery("SELECT * " +
              " FROM offer o " +
              " WHERE o.shipmentId = :shipmentId " +
              "   AND o.accepted = 1 ")
    Offer getAcceptedOfferByShipmentId(@Bind("shipmentId") int shipmentId);


    @SqlQuery("SELECT * " +
              " FROM offer o " +
              " WHERE o.shipmentId = :shipmentId ")
    List<Offer> getAllOfferByShipmentId(@Bind("shipmentId") int shipmentId);

    @SqlUpdate("UPDATE offer SET accepted = :accept WHERE id = :offerId")
    void updateOffer(@Bind("offerId") int offerId, @Bind("accept") boolean accept);
}
