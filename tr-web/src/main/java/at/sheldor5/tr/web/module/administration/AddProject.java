package at.sheldor5.tr.web.module.administration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "addProject", urlPatterns = "/addProject")
public class AddProject extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final String projectName = req.getParameter("name");

    // todo store project in db

    System.out.println("Project " + projectName + " added"); // todo remove
    resp.sendRedirect(req.getContextPath() + "/projects.xhtml");
  }
}
