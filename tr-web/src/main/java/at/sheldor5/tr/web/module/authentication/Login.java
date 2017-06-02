package at.sheldor5.tr.web.module.authentication;

import at.sheldor5.tr.api.user.UserMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = "/login")
public class Login extends HttpServlet {

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    final HttpSession session = request.getSession();
    if (session != null) {
      final String username = request.getUserPrincipal().getName();
      final UserMapping userMapping = LoginModuleImpl.getAuthenticatedUserMapping(username);
      if (userMapping != null) {
        session.setAttribute("user", userMapping.getUser());
        session.setAttribute("userMapping", userMapping);
      }
    }
    response.sendRedirect(request.getContextPath() + "/time.xhtml");
  }
}
