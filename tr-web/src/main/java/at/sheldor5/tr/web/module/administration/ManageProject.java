package at.sheldor5.tr.web.module.administration;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.DataProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author constantin
 * @date 05.06.2017
 */
@WebServlet(name = "mangeProject", urlPatterns = "/manageProject")
public class ManageProject extends HttpServlet {

  private static final Logger LOGGER = Logger.getLogger(ManageProject.class.getName());

  private DataProvider dataProvider;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final String command = req.getParameter("cmd");
    switch (command) {
      case "add":
        LOGGER.info("Received request for adding a project");
        addProject(req);
        break;
      case "rename":
        LOGGER.info("Received request for renaming a project");
        renameProject(req);
        break;
      default:
        LOGGER.info("Unknown command " + command);
    }
    resp.sendRedirect(req.getContextPath() + "/projects.xhtml");
  }

  private void addProject(HttpServletRequest req) {
    final String projectName = req.getParameter("name");
    Project project = new Project(projectName);
    dataProvider.save(project);
  }

  private void renameProject(HttpServletRequest req) {
    final int id;
    try {
      id = Integer.parseInt(req.getParameter("id"));
    }
    catch (NumberFormatException e) {
      LOGGER.warning("Given id is not an integer");
      return;
    }
    final String newName = req.getParameter("newName");
    final Project project = dataProvider.getProject(id);
    project.setName(newName);
    dataProvider.save(project);
  }

}
