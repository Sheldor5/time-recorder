package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.DataProvider;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author constantin
 * @date 05.06.2017
 */
@Named(value = "projectList")
@RequestScoped
public class ProjectListController {

  @Inject
  private DataProvider dataProvider;

  private Collection<Project> projects = new ArrayList<>();

  public ProjectListController() {
    projects = dataProvider.getProjects();
  }

  public Collection<Project> getProjects() {
    return projects;
  }

  public void setProjects(Collection<Project> projects) {
    this.projects = projects;
  }
}
