package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.web.utils.SessionUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @author Consti
 * @date 06.06.2017
 */
@Named(value = "adminPermissions")
@RequestScoped
public class AdminPermissionsController implements Serializable {

  @Inject
  private UserController userController;

  private static final Logger LOGGER = Logger.getLogger(AdminPermissionsController.class.getName());

  public void check(ComponentSystemEvent event) {
    if(!userController.getAdmin()) {
      LOGGER.warning("Non privileged user (" + userController.getUsername() + ") tried to access a forbidden site");
      try {
        SessionUtils.getResponse().sendRedirect(SessionUtils.getRequest().getContextPath() + "/index.xhtml");
      } catch (IOException e) {
        LOGGER.warning("Redirect to index.xhtml failed");
      }
    }
  }
}
