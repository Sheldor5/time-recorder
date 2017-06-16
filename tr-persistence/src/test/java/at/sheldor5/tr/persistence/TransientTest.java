package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.provider.SessionProvider;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author Michael Palata
 * @date 16.06.2017
 */
public class TransientTest extends TestFixture {

  private static final UUID USER_MAPPING_UUID = UUID.fromString("0f0f9435-c43b-4939-9a42-559c6347c2a8");

  //@Test
  public void test() {
    // simulate registration process
    /*final EntityManager entityManager1 = EntityManagerHelper.createEntityManager();
    final UserMappingProvider userMappingProvider1 = new UserMappingProvider(entityManager1);
    final UserMapping registered = new UserMapping(USER_MAPPING_UUID);
    registered.setRole(Role.ADMIN);
    userMappingProvider1.save(registered);
    Assert.assertTrue(registered.getId() > 0);
    userMappingProvider1.close();*/

    // simulate login
    final EntityManager entityManager2 = EntityManagerHelper.createEntityManager();
    final UserMappingProvider userMappingProvider2 = new UserMappingProvider(entityManager2);
    final UserMapping login = userMappingProvider2.get(USER_MAPPING_UUID);
    Assert.assertNotNull(login);
    //Assert.assertEquals(registered.getUuid(), login.getUuid());
    //Assert.assertEquals(registered.getId(), login.getId());
    userMappingProvider2.close();

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // simulate creating child objects
    final EntityManager entityManager3 = EntityManagerHelper.createEntityManager();
    final SessionProvider sessionProvider = new SessionProvider(entityManager3);
    final Session session = new Session(null, login, LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1));
    sessionProvider.save(session);
    sessionProvider.close();
  }

}
