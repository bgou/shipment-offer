package com.bruce.convoy.dao;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.skife.jdbi.v2.DBI;

@NoArgsConstructor
@Data
public class DaoFactory {
    private DriverDao driverDao;
    private ShipmentDao shipmentDao;
    private OfferDao offerDao;
    public DaoFactory(DBI jdbi) {
        Preconditions.checkNotNull(jdbi);
        this.driverDao = jdbi.onDemand(DriverDao.class);
        this.shipmentDao = jdbi.onDemand(ShipmentDao.class);
        this.offerDao = jdbi.onDemand(OfferDao.class);
    }
}
