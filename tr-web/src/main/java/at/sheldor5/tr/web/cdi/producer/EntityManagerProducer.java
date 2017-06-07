package at.sheldor5.tr.web.cdi.producer;

import at.sheldor5.tr.persistence.EntityManagerHelper;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

public class EntityManagerProducer {

  @Produces
  @RequestScoped
  public EntityManager getEntityManager() {
    return EntityManagerHelper.createEntityManager();
  }
}
