package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.DataProvider;
import at.sheldor5.tr.web.module.administration.ManageProject;
import at.sheldor5.tr.web.utils.SessionUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author constantin
 * @date 05.06.2017
 */
@Named(value = "projectList")
@RequestScoped
public class ProjectListController {

  @Inject
  private DataProvider dataProvider;
  @Inject
  private UserController userController;

  private Collection<Project> projects = new ArrayList<>();
  private static final Logger LOGGER = Logger.getLogger(ProjectListController.class.getName());

  @PostConstruct
  private void fillProjects() {
    projects = dataProvider.getProjects();
  }

  public Collection<Project> getProjects() {
    return projects;
  }

  public void setProjects(Collection<Project> projects) {
    this.projects = projects;
  }

  public void checkPermissions(ComponentSystemEvent event) {
    if(!userController.getAdmin()) {
      LOGGER.warning("Non privileged user (" + userController.getUsername() + ") tried to access projects.xhtml");
      try {
        SessionUtils.getResponse().sendRedirect(SessionUtils.getRequest().getContextPath() + "/index.xhtml");
      } catch (IOException e) {
        LOGGER.warning("Redirect to index.xhtml failed");
      }
    }
  }
}
