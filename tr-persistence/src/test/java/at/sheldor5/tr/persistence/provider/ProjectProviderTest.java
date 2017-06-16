package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.persistence.TestFixture;
import at.sheldor5.tr.persistence.provider.ProjectProvider;
import org.junit.*;

import java.util.List;
import java.util.UUID;

public class ProjectProviderTest extends TestFixture {

  private static ProjectProvider projectProvider = new ProjectProvider();

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

    final List<Project> projects = projectProvider.get(name);

    Assert.assertNotNull(projects);
    Assert.assertTrue(projects.size() > 0);

    boolean found = false;
    for (final Project p : projects) {
      if (name.equals(p.getName())) {
        found = true;
        break;
      }
    }
    Assert.assertTrue(found);
  }

  @AfterClass
  public static void teardown() {
    projectProvider.close();
  }
}
