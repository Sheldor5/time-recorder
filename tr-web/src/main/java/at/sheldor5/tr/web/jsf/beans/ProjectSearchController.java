package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Palata
 * @date 04.06.2017
 */
@Named(value = "projectSearch")
@RequestScoped
public class ProjectSearchController implements Serializable {

  private String name;
  private List<Project> projects = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void search() {
    // simulate search (database access, ...)
    projects.add(new Project(name));
    projects.add(new Project("Project X"));
  }

  public boolean hasProjects() {
    return projects.size() > 0;
  }
}