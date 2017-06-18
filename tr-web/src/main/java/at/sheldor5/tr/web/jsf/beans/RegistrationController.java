package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.mappings.UserProjectMapping;
import at.sheldor5.tr.persistence.provider.AccountProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import at.sheldor5.tr.web.BusinessLayer;
import at.sheldor5.tr.web.DataAccessLayer;
import at.sheldor5.tr.web.module.authentication.AuthenticationManager;

import java.time.LocalDate;
import java.util.PropertyResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("registration")
public class RegistrationController {

  private final PropertyResourceBundle msg;
  private final DataAccessLayer dataAccessLayer;
  private final BusinessLayer businessLayer;

  private String username;
  private String password;
  private String forename;
  private String surname;
  private String password2;

  private boolean successful;

  public RegistrationController() {
    // CDI
    this.msg = null;
    this.dataAccessLayer = null;
    this.businessLayer = null;
  }

  @Inject
  public RegistrationController(final PropertyResourceBundle msg, final DataAccessLayer dataAccessLayer, final BusinessLayer businessLayer) {
    this.msg = msg;
    this.dataAccessLayer = dataAccessLayer;
    this.businessLayer = businessLayer;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public String getPassword2() {
    return password2;
  }

  public void setPassword2(String password2) {
    this.password2 = password2;
  }

  public void register() {
    if (!password.equals(password2)) {
      FacesContext.getCurrentInstance().addMessage("form:password2",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("register.password.unequal"), null));
      return;
    }

    try (final UserProvider userProvider = new UserProvider()) {
      if (userProvider.get(username) != null) {
        FacesContext.getCurrentInstance().addMessage("form:username",
            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("register.username.duplication"), null));
        return;
      }
    }

    final User user = new User(username, password, forename, surname);
    final UserMapping userMapping = AuthenticationManager.getInstance().saveUser(user);
    final Project project = dataAccessLayer.getProject("time-recorder");
    final UserProjectMapping userProjectMapping = new UserProjectMapping(userMapping, project);
    dataAccessLayer.save(userProjectMapping);
    createScheduleAccount(userMapping);
    successful = true;
  }

  private void createScheduleAccount(UserMapping userMapping) {
    Account accountOfYear = new Account(LocalDate.now());
    Account accountOfMonth = new Account(LocalDate.now());
    accountOfMonth.setUserMapping(userMapping);
    accountOfMonth.setTime(0);
    accountOfMonth.setTimeWorked(0);
    accountOfYear.setUserMapping(userMapping);
    accountOfYear.setTime(0);
    accountOfYear.setTimeWorked(0);
    businessLayer.save(accountOfMonth, false);
    businessLayer.save(accountOfYear, true);
  }

  public boolean isSuccessful() {
    return successful;
  }

}
