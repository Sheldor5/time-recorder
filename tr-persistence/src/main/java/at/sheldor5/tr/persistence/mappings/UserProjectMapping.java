package at.sheldor5.tr.persistence.mappings;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.UserMapping;

public class UserProjectMapping {

  protected int id;
  protected UserMapping userMapping;
  protected Project project;

  public UserProjectMapping() {

  }

  public UserProjectMapping(final UserMapping userMapping, final Project project) {
    this.userMapping = userMapping;
    this.project = project;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UserMapping getUserMapping() {
    return userMapping;
  }

  public void setUserMapping(UserMapping userMapping) {
    this.userMapping = userMapping;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

}
