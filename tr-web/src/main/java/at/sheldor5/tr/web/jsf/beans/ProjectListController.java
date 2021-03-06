package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.BusinessLayer;
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

  private static final Logger LOGGER = Logger.getLogger(ProjectListController.class.getName());

  private BusinessLayer businessLayer;

  private Collection<Project> projects = new ArrayList<>();

  public ProjectListController() {
    // CDI
  }

  @Inject
  public ProjectListController(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
  }

  @PostConstruct
  public void init() {
    projects = businessLayer.getAllProjects();
  }

  public Collection<Project> getProjects() {
    return projects;
  }

  public void setProjects(Collection<Project> projects) {
    this.projects = projects;
  }
}
