package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

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
}
