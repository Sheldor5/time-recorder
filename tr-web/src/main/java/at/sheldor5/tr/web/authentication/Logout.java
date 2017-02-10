package at.sheldor5.tr.web.authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class Logout extends HttpServlet {

  private static final String HTML_LOGIN = "/index.html";

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
    final HttpSession session = request.getSession();
    session.invalidate();
    response.sendRedirect(response.encodeRedirectURL(HTML_LOGIN));
  }
}