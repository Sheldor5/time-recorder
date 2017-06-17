package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.web.BusinessLayer;
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

  private BusinessLayer businessLayer;
  private UserController userController;

  private AdminPermissionsController() {

  }

  @Inject
  public AdminPermissionsController(final BusinessLayer businessLayer, final UserController userController) {
    this.businessLayer = businessLayer;
    this.userController = userController;
  }

  private static final Logger LOGGER = Logger.getLogger(AdminPermissionsController.class.getName());

  public void check(ComponentSystemEvent event) {
    if(!businessLayer.isAdmin()) {
      LOGGER.warning("Non privileged user (" + businessLayer.getUser().getUsername() + ") tried to access a forbidden site");
      try {
        SessionUtils.getResponse().sendRedirect(SessionUtils.getRequest().getContextPath() + "/index.xhtml");
      } catch (IOException e) {
        LOGGER.warning("Redirect to index.xhtml failed");
      }
    }
  }

  public boolean hasPermissions() {
    return userController.getAdmin();
  }
}
