package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.web.BusinessLayer;
import at.sheldor5.tr.web.DataAccessLayer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Consti
 * @date 06.06.2017
 */
@Named(value = "userList")
@RequestScoped
public class UserListController implements Serializable {

  private BusinessLayer businessLayer;

  private Collection<User> users;

  public UserListController() {
    // CDI
  }

  public UserListController(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
  }

  @PostConstruct
  private void init() {
    users = businessLayer.getUsers();
  }

  public Collection<User> getUsers() {
    return users;
  }

  public void setUsers(Collection<User> users) {
    this.users = users;
  }

  public boolean hasUsers() {
    return !users.isEmpty();
  }
}
