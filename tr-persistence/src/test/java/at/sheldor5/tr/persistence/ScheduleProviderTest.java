package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.provider.ScheduleProvider;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class ScheduleProviderTest extends TestFixture {

  private static final ScheduleProvider SCHEDULE_PROVIDER = new ScheduleProvider();
  private static final UserProvider USER_PROVIDER = new UserProvider();
  private static final UserMappingProvider USER_MAPPING_PROVIDER = new UserMappingProvider();

  private static User user;
  private static UserMapping userMapping;

  @Before
  public void setup() throws IOException, SQLException {
    final User user = new User("ScheduleProviderTest", "pASSw0rD");
    USER_PROVIDER.save(user);

    Assert.assertNotNull(user.getUuid());

    final UserMapping userMapping = new UserMapping(user.getUuid());
    USER_MAPPING_PROVIDER.save(userMapping);

    Assert.assertTrue(userMapping.getId() > 0);

    ScheduleProviderTest.user = user;
    ScheduleProviderTest.userMapping = userMapping;
  }

  @Test
  public void test_persist_schedule() {
    final Schedule schedule = new Schedule(userMapping);
    schedule.setTime(DayOfWeek.MONDAY, LocalTime.of(8, 0));
    schedule.setTime(DayOfWeek.TUESDAY, LocalTime.of(8, 0));
    schedule.setTime(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0));
    schedule.setTime(DayOfWeek.THURSDAY, LocalTime.of(8, 0));
    schedule.setTime(DayOfWeek.FRIDAY, LocalTime.of(6, 30));
    schedule.setTime(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
    schedule.setTime(DayOfWeek.SUNDAY, LocalTime.of(0, 0));

    SCHEDULE_PROVIDER.save(schedule);

    Assert.assertTrue(schedule.getId() > 0);

    final Schedule actual = SCHEDULE_PROVIDER.get(schedule.getId());
    Assert.assertNotNull(actual);

    Assert.assertEquals(LocalTime.of(8, 0), schedule.getMonday());
    Assert.assertEquals(LocalTime.of(8, 0), schedule.getTuesday());
    Assert.assertEquals(LocalTime.of(8, 0), schedule.getWednesday());
    Assert.assertEquals(LocalTime.of(8, 0), schedule.getThursday());
    Assert.assertEquals(LocalTime.of(6, 30), schedule.getFriday());
    Assert.assertEquals(LocalTime.of(0, 0), schedule.getSaturday());
    Assert.assertEquals(LocalTime.of(0, 0), schedule.getSunday());
  }

  @AfterClass
  public static void teardown() throws IOException, SQLException {
    SCHEDULE_PROVIDER.close();
  }
}
