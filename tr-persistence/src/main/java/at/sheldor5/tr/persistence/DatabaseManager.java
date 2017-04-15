package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseManager {

  private static final Configuration configuration = new Configuration();
  private static boolean initialized = false;
  private static SessionFactory sessionFactory;

  public static void init() {
    if (!initialized) {
      configuration.setProperty("hibernate.connection.driver_class", GlobalProperties.getProperty("db.jdbc.class"));
      configuration.setProperty("hibernate.connection.url", GlobalProperties.getProperty("db.jdbc.url"));
      configuration.setProperty("hibernate.connection.username", GlobalProperties.getProperty("db.username"));
      configuration.setProperty("hibernate.connection.password", GlobalProperties.getProperty("db.password"));
      configuration.setProperty("hibernate.connection.pool_size", GlobalProperties.getProperty("db.pool_size"));
      configuration.setProperty("hibernate.connection.autocommit", GlobalProperties.getProperty("db.autocommit"));
      //configuration.setProperty("hibernate.connection.datasource", "");
      configuration.setProperty("hibernate.current_session_context_class", "thread");
      configuration.setProperty("hibernate.hbm2ddl.auto", "update");

      // object-to-table mapping files
      configuration.addResource("UserMapping.hbm.xml");
      configuration.addResource("Record.hbm.xml");
      configuration.addResource("Schedule.hbm.xml");

      sessionFactory = configuration.buildSessionFactory();
      initialized = true;
    }
  }

  public static final Configuration getConfiguration() {
    return configuration;
  }

  public static Session getSession() {
    return sessionFactory.getCurrentSession();
  }

}
