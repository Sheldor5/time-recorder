package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.web.DataProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Consti
 * @date 06.06.2017
 */
@Named(value = "userList")
@RequestScoped
public class UserListController implements Serializable {

  @Inject
  private DataProvider dataProvider;
  private Collection<User> users;

  @PostConstruct
  private void init() {
    users = dataProvider.getUsers();
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
