package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import org.hibernate.cfg.Configuration;

public class PersistenceManager {

  private static Configuration configuration;

  public static Configuration getConfiguration() {
    if (configuration == null) {
      configuration = new Configuration();
      configuration.setProperty("hibernate.connection.driver_class", GlobalProperties.getProperty("db.jdbc.class"));
      configuration.setProperty("hibernate.connection.url", GlobalProperties.getProperty("db.jdbc.url"));
      configuration.setProperty("hibernate.connection.username", GlobalProperties.getProperty("db.username"));
      configuration.setProperty("hibernate.connection.password", GlobalProperties.getProperty("db.password"));
      configuration.setProperty("hibernate.connection.pool_size", GlobalProperties.getProperty("db.pool_size"));
      configuration.setProperty("hibernate.connection.autocommit", GlobalProperties.getProperty("db.autocommit"));
      //configuration.setProperty("hibernate.connection.datasource", "");
      configuration.setProperty("hibernate.current_session_context_class", "thread");
      configuration.setProperty("hibernate.hbm2ddl.auto", "update");
      configuration.addResource("UserMapping.hbm.xml");
      configuration.addResource("Record.hbm.xml");
    }
    return configuration;
  }

}
