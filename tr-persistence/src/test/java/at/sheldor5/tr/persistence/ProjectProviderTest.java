package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.provider.ProjectProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class ProjectProviderTest {

  private static final String PROPERTIES = "test.properties";
  private static ProjectProvider projectProvider;

  @BeforeClass
  public static void setup() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    EntityManagerHelper.setupGlobalProperties();
    projectProvider = new ProjectProvider();
  }

  @Test
  public void test_persist_project() {
    String name;
    Project project;

    name = UUID.randomUUID().toString().replace("-", "");
    project = new Project(name);

    projectProvider.save(project);

    Assert.assertNotNull(project.getId());

    name = UUID.randomUUID().toString().replace("-", "");
    project = new Project(name, project);

    projectProvider.save(project);

    Assert.assertNotNull(project.getId());

    final Project actual = projectProvider.get(project.getId());
    final Project parent = actual.getParent();

    Assert.assertNotNull(actual);
    Assert.assertNotNull(parent);
    System.out.println(parent);
    Assert.assertEquals(project, actual);
  }

  @Test
  public void test_find_by_username() {
    final String name = UUID.randomUUID().toString().replace("-", "");
    final Project project = new Project(name);

    projectProvider.save(project);

    Assert.assertTrue(project.getId() > 0);

    final Project actual = projectProvider.get(name);

    Assert.assertNotNull(actual);
    Assert.assertEquals(project, actual);
  }

  @AfterClass
  public static void teardown() {
    projectProvider.close();
  }
}