package at.sheldor5.tr.web.init;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.web.DataAccessLayer;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestData {

  public void setup(final UserMapping userMapping, final Project project) {
    try (final DataAccessLayer dataAccessLayer = new DataAccessLayer(EntityManagerHelper.createEntityManager())) {
      final Session s1 = new Session(project, userMapping, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(12, 0));
      final Session s2 = new Session(project, userMapping, LocalDate.now(), LocalTime.of(12, 30), LocalTime.of(16, 30));
      final Session s3 = new Session(project, userMapping, LocalDate.now(), LocalTime.of(17, 0), LocalTime.of(21, 0));
      dataAccessLayer.save(s1);
      dataAccessLayer.save(s2);
      dataAccessLayer.save(s3);
    }
  }
}
