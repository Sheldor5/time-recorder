package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.web.BusinessLayer;
import at.sheldor5.tr.web.DataAccessLayer;
import at.sheldor5.tr.web.utils.SessionUtils;
import org.mindrot.jbcrypt.BCrypt;

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

  private static final Logger LOGGER = Logger.getLogger(UserEditController.class.getName());

  private BusinessLayer businessLayer;

  private UUID uuidFromRequest;
  private String username;
  private String forename;
  private String surname;
  private String newPassword = "";
  private String newPasswordRepeat = "";
  private boolean validUUID = true;
  private boolean editOk = true;
  private boolean passwordRepeatWrongMsg = false;
  private boolean changeSuccessfulMsg = false;

  public UserEditController() {
    // CDI
  }

  @Inject
  public UserEditController(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
  }

  @PostConstruct
  private void init() {
    String uuid = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("uuid");
    if(uuid == null || uuid.isEmpty()){
      validUUID = false;
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
      return true;
    } catch (IOException e) {
      LOGGER.warning("Redirect to index.xhtml failed");
      return false;
    }
  }

  public void getUserData() {
    User user  = businessLayer.getUser(uuidFromRequest);
    username = user.getUsername();
    forename = user.getForename();
    surname = user.getSurname();
  }

  public void saveUser() {
    if(!hasPermission()) {
      return;
    }
    User user = businessLayer.getUser(uuidFromRequest);
    if(!username.isEmpty()) {
      user.setUsername(username);
    }
    if(!forename.isEmpty()) {
      user.setForename(forename);
    }
    if(!surname.isEmpty()) {
      user.setSurname(surname);
    }
    if(!newPassword.isEmpty() || !newPasswordRepeat.isEmpty()) {
      handlePassword(user);
    }
    if(editOk) {
      changeSuccessfulMsg = true;
      //dataProvider.save(user); // todo nicht notwendig?
    }
  }

  private void handlePassword(User user) {
    if(newPassword.equals(newPasswordRepeat)) {
      user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }
    else {
      passwordRepeatWrongMsg = true;
      editOk = false;
    }
  }

  private void redirectIfNotAuthorized() throws IOException {
    UUID uuidFromUserToBeEdited = uuidFromRequest;
    UUID uuidFromAuthenticatedUser = businessLayer.getUser().getUuid();
    // admin can edit all users, a normal user can only edit himself
    if(!uuidFromUserToBeEdited.equals(uuidFromAuthenticatedUser) && !businessLayer.isAdmin()) {
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

  public UUID getUuidFromRequest() {
    return uuidFromRequest;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getNewPasswordRepeat() {
    return newPasswordRepeat;
  }

  public void setNewPasswordRepeat(String newPasswordRepeat) {
    this.newPasswordRepeat = newPasswordRepeat;
  }

  public boolean isPasswordRepeatWrongMsg() {
    return passwordRepeatWrongMsg;
  }

  public void setPasswordRepeatWrongMsg(boolean passwordRepeatWrongMsg) {
    this.passwordRepeatWrongMsg = passwordRepeatWrongMsg;
  }

  public boolean isChangeSuccessfulMsg() {
    return changeSuccessfulMsg;
  }

  public void setChangeSuccessfulMsg(boolean changeSuccessfulMsg) {
    this.changeSuccessfulMsg = changeSuccessfulMsg;
  }
}
