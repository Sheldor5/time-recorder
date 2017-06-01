package at.sheldor5.tr.web.utils;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionUtils {

  public static HttpSession getSession() {
    return (HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(false);
  }

  public static HttpServletRequest getRequest() {
    return (HttpServletRequest) FacesContext.getCurrentInstance()
        .getExternalContext().getRequest();
  }

  public static HttpServletResponse getResponse() {
    return (HttpServletResponse) FacesContext.getCurrentInstance()
        .getExternalContext().getResponse();
  }

  public static UserMapping getUserMapping() {
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    Object object = session.getAttribute("usermapping");
    if (object == null) {
      return null;
    }
    return (UserMapping) object;
  }

  public static User getUser() {
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    Object object = session.getAttribute("user");
    if (object == null) {
      return null;
    }
    return (User) object;
  }
}