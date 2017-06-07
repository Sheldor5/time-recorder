package at.sheldor5.tr.web.module.administration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Consti
 * @date 06.06.2017
 */
@WebServlet(name = "userManager", urlPatterns = "/userManager")
public class UserManager extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // todo persist changes
  }
}
