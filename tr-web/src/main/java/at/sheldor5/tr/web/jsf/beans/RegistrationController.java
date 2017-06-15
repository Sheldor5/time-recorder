package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.persistence.provider.UserProvider;
import at.sheldor5.tr.web.DataAccessLayer;
import at.sheldor5.tr.web.module.authentication.AuthenticationManager;
import java.util.PropertyResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("registration")
public class RegistrationController {

  private PropertyResourceBundle msg;

  private DataAccessLayer dataAccessLayer;

  private String username;
  private String password;
  private String forename;
  private String surname;
  private String password2;

  private boolean successful;

  @Inject
  public void init(final PropertyResourceBundle msg) {
    this.msg = msg;
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
      }
    }

    final User user = new User(username, password, forename, surname);
    AuthenticationManager.getInstance().saveUser(user);
    successful = true;
  }

  public boolean isSuccessful() {
    return successful;
  }

}
