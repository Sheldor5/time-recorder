package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import at.sheldor5.tr.web.BusinessLayer;
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
import java.util.*;
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
  private UserController userController;

  private UUID uuidFromRequest;
  private String username;
  private String forename;
  private String surname;
  private String newPassword = "";
  private String newPasswordRepeat = "";
  private Map<String, Project> availableProjects = new HashMap<>();
  private List<Project> selectedProjectsForAdding;
  private List<Project> selectedProjectsForRemoving;
  private Map<String, Project> assignedProjects = new HashMap<>();
  private boolean validUUID = true;
  private boolean editOk = true;
  private boolean passwordRepeatWrongMsg = false;
  private boolean changeSuccessfulMsg = false;

  public UserEditController() {
    // CDI
  }

  @Inject
  public UserEditController(final BusinessLayer businessLayer, final UserController userController) {
    this.businessLayer = businessLayer;
    this.userController = userController;
  }

  @PostConstruct
  private void init() {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    String uuid = request.getParameter("uuid");
    storeUuid(uuid);
    initProjectData();
  }

  private void storeUuid(String uuid) {
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

  private void initProjectData() {
    assignedProjects.clear();
    availableProjects.clear();
    UserMapping userMapping = getUserMappingFromUUID(uuidFromRequest);
    for(Project project : businessLayer.getProjects(userMapping)) {
      assignedProjects.put(project.getName(), project);
    }
    for(Project project : businessLayer.getAllProjects()) {
      if(!assignedProjects.containsKey(project.getName())){
        availableProjects.put(project.getName(), project);
      }
    }
  }

  private UserMapping getUserMappingFromUUID(UUID uuidFromRequest) {
    return new UserMappingProvider().get(uuidFromRequest);
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

  private void redirectIfNotAuthorized() throws IOException {
    UUID uuidFromUserToBeEdited = uuidFromRequest;
    UUID uuidFromAuthenticatedUser = businessLayer.getUser().getUuid();
    // admin can edit all users, a normal user can only edit himself
    if(!uuidFromUserToBeEdited.equals(uuidFromAuthenticatedUser) && !businessLayer.isAdmin()) {
      redirect();
    }
  }

  public void getUserData() {
    User user  = businessLayer.getUser(uuidFromRequest);
    username = user.getUsername();
    forename = user.getForename();
    surname = user.getSurname();
  }

  public void saveUser() {
    LOGGER.info("save user");
    if(!hasPermission()) {
      return;
    }
    User user = businessLayer.getUser(uuidFromRequest);
    handlePassword(user);
    handleUserDetails(user);
    handleProjects();
    if(editOk) {
      //dataProvider.save(user); //todo delete if not necessary
      changeSuccessfulMsg = true;
    }
    initProjectData();
  }

  private void handlePassword(User user) {
    // if the user hasn't entered any passwords, do nothing
    if(!newPassword.isEmpty() || !newPasswordRepeat.isEmpty()) {
      if(newPassword.equals(newPasswordRepeat)) {
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
      }
      else {
        passwordRepeatWrongMsg = true;
        editOk = false;
      }
    }
  }

  private void handleUserDetails(User user) {
    if(editOk) {
      user.setUsername(username);
      user.setForename(forename);
      user.setSurname(surname);
    }
  }

  private void handleProjects() {
    if(userController.getAdmin()) {
      UserMapping userMapping = getUserMappingFromUUID(uuidFromRequest);
      businessLayer.addUserProjectMappings(userMapping, selectedProjectsForAdding);
      businessLayer.removeUserProjectMappings(userMapping, selectedProjectsForRemoving);
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

  public Map<String, Project> getAvailableProjects() {
    return availableProjects;
  }

  public void setAvailableProjects(Map<String, Project> availableProjects) {
    this.availableProjects = availableProjects;
  }

  public List<Project> getSelectedProjectsForAdding() {
    return selectedProjectsForAdding;
  }

  public void setSelectedProjectsForAdding(List<Project> selectedProjectsForAdding) {
    this.selectedProjectsForAdding = selectedProjectsForAdding;
  }

  public Map<String, Project> getAssignedProjects() {
    return assignedProjects;
  }

  public void setAssignedProjects(Map<String, Project> assignedProjects) {
    this.assignedProjects = assignedProjects;
  }

  public List<Project> getSelectedProjectsForRemoving() {
    return selectedProjectsForRemoving;
  }

  public void setSelectedProjectsForRemoving(List<Project> selectedProjectsForRemoving) {
    this.selectedProjectsForRemoving = selectedProjectsForRemoving;
  }
}
