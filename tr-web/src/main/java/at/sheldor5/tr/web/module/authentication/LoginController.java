package at.sheldor5.tr.web.module.authentication;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean(name = "login")
public class LoginController {

  public void forward() throws IOException {
    FacesContext.getCurrentInstance().getExternalContext().redirect("/login");
  }
}
