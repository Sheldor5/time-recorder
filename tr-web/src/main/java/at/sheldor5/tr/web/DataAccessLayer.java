package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Michael Palata
 * @date 04.06.2017
 */
@Named()
@ApplicationScoped
public class DataAccessLayer {

  public static Project getProject(int id) {
    return null;
  }

  public static Collection<Project> getProjects() {
    // mock
    Collection<Project> projects = new ArrayList<>();
    projects.add(new Project("Project A"));
    projects.add(new Project("Project B"));
    projects.add(new Project("Project C"));
    projects.add(new Project("Project D"));
    return projects;
  }

  public static void addProject(String name) {

  }

  public static void renameProject(int projectId, String newName) {

  }
}
