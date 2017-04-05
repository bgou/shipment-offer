package com.bruce.convoy;

import com.bruce.convoy.dao.DaoFactory;
import com.bruce.convoy.health.ShipmentOfferHealthCheck;
import com.bruce.convoy.resources.DriverResource;
import com.bruce.convoy.resources.OfferResource;
import com.bruce.convoy.resources.ShipmentResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class ShipmentOfferApplication extends Application<ShipmentOfferConfiguration> {
    public static void main(String[] args) throws Exception {
        new ShipmentOfferApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<ShipmentOfferConfiguration> bootstrap) {
    }

    public void run(final ShipmentOfferConfiguration configuration, final Environment environment) {

        ShipmentOfferHealthCheck check = new ShipmentOfferHealthCheck();
        environment.healthChecks().register("health", check);

        /*
         * NOTE: since I'm using h2 in memory database, each resource will create their own database on startup
         * that means the data will only live in the application life cycle
         */
        DaoFactory daoFactory = createDaoFactory(environment, configuration.getDatabase());
        environment.jersey().register(new DriverResource(daoFactory));
        environment.jersey().register(new ShipmentResource(daoFactory));
        environment.jersey().register(new OfferResource(daoFactory));
    }

    private DaoFactory createDaoFactory(final Environment environment, final DataSourceFactory database) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, database, "daofactory");
        return new DaoFactory(jdbi);
    }
}
