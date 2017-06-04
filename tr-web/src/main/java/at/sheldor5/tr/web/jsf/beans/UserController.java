package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.web.module.authentication.LoginModuleImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Michael Palata
 * @date 02.06.2017
 */
@Named(value = "user")
@SessionScoped
public class UserController implements Serializable {

  private UserMapping userMapping;
  private User user;
  private boolean admin;

  @PostConstruct
  public void init() {
    final String username = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
    this.userMapping = LoginModuleImpl.getAuthenticatedUserMapping(username);
    this.user = userMapping.getUser();
    this.admin = userMapping.getRole() == Role.ADMIN;
  }

  public UserMapping getUserMapping() {
    return userMapping;
  }

  public User getUser() {
    return user;
  }

  public int getId() {
    return userMapping.getId();
  }

  public String getUsername() {
    return user.getUsername();
  }

  public String getForename() {
    return user.getForename();
  }

  public String getSurname() {
    return user.getSurname();
  }

  public boolean getAdmin() {
    return admin;
  }

  public void logout() throws IOException {
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    FacesContext.getCurrentInstance().getExternalContext().redirect("/");
  }

}
