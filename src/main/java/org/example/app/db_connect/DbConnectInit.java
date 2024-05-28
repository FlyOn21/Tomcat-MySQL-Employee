package org.example.app.db_connect;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.db_connect.interfaces.IConnection;
import org.example.app.entity.Employee;
import org.example.app.utils.logging.LoggingErrorsMsg;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbConnectInit implements IConnection {
//    private final Dotenv dotenv;
    private SessionFactory sessionFactory;
    private final String dbUrl;
    private boolean isConnected;
    @Getter
    private final List<String> connectErrors;
    private Properties props = new Properties();
    private static final Logger DB_CONNECT_LOGGER =
            LogManager.getLogger(DbConnectInit.class);

    public DbConnectInit() {
        getProps();
        this.dbUrl = String.format("%s%s:%s/%s", props.getProperty("HIBERNATE_JDBC_URL_PREFIX"), props.getProperty("MYSQL_HOST"), props.getProperty("MYSQL_HOST_PORT"), props.getProperty("MYSQL_DATABASE"));
        this.isConnected = false;
        this.connectErrors = new ArrayList<>();
        getSessionFactory();
    }

    private void getProps() {
        try {
            props.load(DbConnectInit.class.getResourceAsStream("/db/db.properties"));
        } catch (IOException e) {
            DB_CONNECT_LOGGER.error("Failed to load db.properties file", e);
            throw new RuntimeException("Failed to load db.properties file", e);
        }
    }

    public Configuration initEnvironment() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", props.getProperty("HIBERNATE_JDBC_DRIVER"));
        configuration.setProperty("hibernate.connection.url", this.dbUrl);
        configuration.setProperty("hibernate.connection.username", props.getProperty("MYSQL_USER"));
        configuration.setProperty("hibernate.connection.password", props.getProperty("MYSQL_PASSWORD"));
        configuration.setProperty("hibernate.dialect", props.getProperty("HIBERNATE_DIALECT"));
        configuration.setProperty("hibernate.show_sql", props.getProperty("HIBERNATE_SHOW_SQL"));
        configuration.setProperty("hibernate.format_sql", props.getProperty("HIBERNATE_FORMAT_SQL"));
        configuration.setProperty("hibernate.hbm2ddl.auto", props.getProperty("HIBERNATE_HBM2DDL_AUTO"));
        configuration.setProperty("hibernate.current_session_context_class", props.getProperty("HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS"));
        configuration.setProperty("hibernate.connection.pool_size", props.getProperty("HIBERNATE_CONNECTION_POOL_SIZE"));
        configuration.setProperty("hibernate.generate_statistics", props.getProperty("HIBERNATE_GENERATE_STATISTICS"));
        configuration.addAnnotatedClass(Employee.class);
        return configuration;
    }


    public boolean isConnected() {
        return this.isConnected;
    }

    private void setSessionFactory() {
        Configuration configuration = initEnvironment();
        ServiceRegistry serviceRegistry = null;
        try {
            serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            MetadataSources metadataSources = new MetadataSources(serviceRegistry);
            metadataSources.addAnnotatedClass(Employee.class);
            this.sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
            this.isConnected = true;
        } catch (HibernateException e) {
            DB_CONNECT_LOGGER.error(e.getMessage());
            this.isConnected = false;
            this.connectErrors.add(LoggingErrorsMsg.DB_CONNECTION_ERROR.getMsg());
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
            }
        }
    }

    public SessionFactory getSessionFactory() {
        if (this.sessionFactory == null) {
            setSessionFactory();
        }
        return this.sessionFactory;
    }
}


