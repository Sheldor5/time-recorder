package at.sheldor5.tr.web.authentication;

import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.auth.AuthenticationManager;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "login", urlPatterns = "/login")
public class Login extends HttpServlet {

  private static final String HTML_LOGIN = "/index.html";
  private static final String HTML_HOME = "/home.jsp";

  @Override
  public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
    final HttpSession session = request.getSession();
    final Object object = session.getAttribute("user");

    if (object == null) {
      final String username = request.getParameter("username");
      final String password = request.getParameter("password");
      final User user = AuthenticationManager.getInstance().getUser(username, password);
      if (user == null) {
        response.sendRedirect(response.encodeRedirectURL(HTML_LOGIN));
        return;
      }
    }

    response.sendRedirect(response.encodeRedirectURL(HTML_HOME));
  }
}