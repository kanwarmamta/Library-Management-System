package org.example;

import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import org.apache.commons.text.StringSubstitutor;
import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.example.resources.AuthorResource;
import org.example.resources.BookResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import org.jdbi.v3.core.Jdbi;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;


import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class LibraryApplication extends Application<LibraryConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LibraryApplication().run(args);
    }

    @Override
    public String getName() {
        return "Library Management System";
    }

    @Override
    public void initialize(final Bootstrap<LibraryConfiguration> bootstrap) {
        // Enable database migrations
        bootstrap.addBundle(new MigrationsBundle<LibraryConfiguration>() {
            @Override
            public io.dropwizard.db.DataSourceFactory getDataSourceFactory(LibraryConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false))
        );
    }

    @Override
    public void run(final LibraryConfiguration configuration,
                    final Environment environment) {

        // --- Configure CORS ---
        // This filter allows your local HTML page to communicate with the server.
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        // --- End CORS Configuration ---

        // Get the JDBI instance using JdbiFactory
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        // Register the DAOs
        final AuthorDAO authorDAO = jdbi.onDemand(AuthorDAO.class);
        final BookDAO bookDAO = jdbi.onDemand(BookDAO.class);

        // Initialize the services with their respective DAOs
        final AuthorService authorService = new AuthorService(authorDAO);
        final BookService bookService = new BookService(bookDAO);

        // Register the resources (API endpoints)
        environment.jersey().register(new AuthorResource(authorService));
        environment.jersey().register(new BookResource(bookService));
    }
}