package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.DataAccessLayer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author constantin
 * @date 05.06.2017
 */
@Named(value = "projectList")
@RequestScoped
public class ProjectListController implements Serializable {

  @Inject
  private DataAccessLayer dataAccessLayer;

  private Collection<Project> projects = new ArrayList<>();
  private static final Logger LOGGER = Logger.getLogger(ProjectListController.class.getName());

  @PostConstruct
  private void init() {
    projects = dataAccessLayer.getProjects();
  }

  public Collection<Project> getProjects() {
    return projects;
  }

  public void setProjects(Collection<Project> projects) {
    this.projects = projects;
  }
}
