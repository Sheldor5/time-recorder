package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.web.DataProvider;
import at.sheldor5.tr.web.utils.SessionUtils;
import com.sun.javaws.exceptions.InvalidArgumentException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Consti
 * @date 06.06.2017
 */
@Named(value = "editUser")
@RequestScoped
public class UserEditController implements Serializable {

  @Inject
  private UserController userController;
  @Inject
  private DataProvider dataProvider;

  private static final Logger LOGGER = Logger.getLogger(UserEditController.class.getName());
  private UUID uuidFromRequest;
  private String username;
  private String forename;
  private String surname;
  private boolean validUUID = true;

  @PostConstruct
  private void init() {
    String uuid = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("uuid");
    if(uuid == null || uuid.isEmpty()){
      return;
    }
    try{
      uuidFromRequest = UUID.fromString(uuid);
    }
    catch (IllegalArgumentException e) {
      LOGGER.info("Invalid UUID String");
      validUUID = false;
    }
  }

  public boolean hasPermission() {
    if(!validUUID) {
      return false;
    }
    try {
      redirectIfNotAuthorized();
      getUserData();
      return true;
    } catch (IOException e) {
      LOGGER.warning("Redirect to index.xhtml failed");
      return false;
    }
  }

  private void getUserData() {
    User user  = dataProvider.getUser(uuidFromRequest);
    username = user.getUsername();
    forename = user.getForename();
    surname = user.getSurname();
  }

  private void redirectIfNotAuthorized() throws IOException {
    UUID uuidFromUserToBeEdited = uuidFromRequest;
    UUID uuidFromAuthenticatedUser = userController.getUserMapping().getUuid();
    // admin can edit all users, a normal user can only edit himself
    if(!uuidFromUserToBeEdited.equals(uuidFromAuthenticatedUser) && !userController.getAdmin()) {
      redirect();
    }
  }

  private void redirect() throws IOException {
    SessionUtils.getResponse().sendRedirect(SessionUtils.getRequest().getContextPath() + "/index.xhtml");
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(String forename) {
    this.forename = forename;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }
}
