package at.sheldor5.tr.web.controller.jsf;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.web.utils.SessionUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.security.SecureRandom;

@ManagedBean(name = "user")
@SessionScoped
public class UserController extends User {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private String plainPassword;
  private int i = SECURE_RANDOM.nextInt();

  public String getPlainPassword() {
    return plainPassword;
  }

  public void setPlainPassword(String plainPassword) {
    this.plainPassword = plainPassword;
  }

  public String authenticate() {
    if ("user".equals(username)) {

      SessionUtils.getSession().setAttribute("user", this);
      SessionUtils.getSession().setAttribute("usermapping", this);
      return "home";
    }
    return "login";
  }

}
