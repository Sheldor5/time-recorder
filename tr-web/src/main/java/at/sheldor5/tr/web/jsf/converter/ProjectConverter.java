package at.sheldor5.tr.web.jsf.converter;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.BusinessLayer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Consti
 * @date 10.06.2017
 */
@Named(value = "projectConverter")
public class ProjectConverter implements Converter {

  private static final Logger LOGGER = Logger.getLogger(ProjectConverter.class.getName());

  private BusinessLayer businessLayer;

  public ProjectConverter() {
    // CDI
  }

  @Inject
  public ProjectConverter(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
  }

  @Override
  public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
    List<Project> projects = businessLayer.getProjects(s);
    for(Project project : projects) {
      if(project.getName().equals(s)) {
        return project;
      }
    }
    // some project name was selected that doesn't exist in db
    LOGGER.severe("User has selected the project '" + s + "', which is not present in the database");
    return null;
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
    return ((Project) o).getName();
  }
}
