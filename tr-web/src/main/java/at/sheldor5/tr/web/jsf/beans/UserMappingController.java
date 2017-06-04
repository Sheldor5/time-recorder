package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.UserMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 * @author Michael Palata
 * @date 02.06.2017
 */
@ManagedBean(name = "userMapping")
@SessionScoped
public class UserMappingController extends UserMapping {

  protected boolean admin;

  public UserMappingController(final UserMapping userMapping) {
    this.id = userMapping.getId();
    this.uuid = userMapping.getUuid();
    this.role = userMapping.getRole();
    this.user = userMapping.getUser();
    this.admin = role == Role.ADMIN;
  }

  public boolean getAdmin() {
    return admin;
  }
}
