package at.sheldor5.tr.web.module.administration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "removeProject", urlPatterns = "/removeProject")
public class RemoveProject extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final int projectId = Integer.parseInt(req.getParameter("id"));

    // todo checkout every user from this project if he is currently working on it
    // todo delete project from db

    System.out.println("Project with id " + projectId + " removed"); // todo remove
    resp.sendRedirect(req.getContextPath() + "/projects.xhtml");
  }
}
