package com.bruce.convoy.dao;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.junit.After;
import org.junit.Before;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.UUID;

public abstract class JdbiUnitTest {

    protected DBI dbi;

    private Handle handle;

    protected abstract void setUpDataAccessObjects();

    @Before
    public void setUpDatabase() throws Exception {

        Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null );
        dbi = new DBIFactory().build(environment, getDataSourceFactory(), "test" );
        handle = dbi.open();
        setUpDataAccessObjects();
    }

    @After
    public void tearDown() throws Exception {
        handle.close();
    }


    protected DataSourceFactory getDataSourceFactory()
    {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass( "org.h2.Driver" );
        dataSourceFactory.setUrl("jdbc:h2:mem:" + UUID.randomUUID().toString() + ";" +
                                 "INIT=CREATE SCHEMA IF NOT EXISTS offer\\;SET SCHEMA offer");
        dataSourceFactory.setUser( "sa" );
        dataSourceFactory.setPassword( "sa" );

        return dataSourceFactory;
    }
}