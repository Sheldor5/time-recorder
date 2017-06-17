package at.sheldor5.tr.web.module.administration;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.BusinessLayer;
import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author constantin
 * @date 05.06.2017
 */
@WebServlet(name = "mangeProject", urlPatterns = "/manageProject")
public class ProjectManager extends HttpServlet {

  private static final Logger LOGGER = Logger.getLogger(ProjectManager.class.getName());

  @Inject
  private BusinessLayer businessLayer;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if(!businessLayer.isAdmin()) {
      LOGGER.warning("Non privileged user (" + businessLayer.getUser().getUsername() + ") tried to access manageProject WebServlet");
      resp.sendRedirect(req.getContextPath() + "/index.xhtml");
      return;
    }
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
    if(projectName == null || projectName.isEmpty()) {
      return;
    }
    Project project = new Project(projectName);
    businessLayer.save(project);
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
    if(newName == null || newName.isEmpty()) {
      return;
    }
    final Project project = businessLayer.getProject(id);
    project.setName(newName);
    businessLayer.save(project);
  }

}
