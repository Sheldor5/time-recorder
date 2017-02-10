package at.sheldor5.tr.web.test;

import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.auth.AuthenticationManager;
import at.sheldor5.tr.core.persistence.DatabaseEngine;
import at.sheldor5.tr.web.init.ConnectionPool;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Test", urlPatterns = "/test")
public class Test extends HttpServlet {
  /**
   * Respond to a GET request for the content produced by
   * this servlet.
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are producing
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet error occurs
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

    response.setContentType("text/html");
    PrintWriter writer = response.getWriter();
    writer.println("<html>");
    writer.println("<head>");
    writer.println("<title>Sample Application Servlet Page</title>");
    writer.println("</head>");
    writer.println("<body bgcolor=white>");

    User user = AuthenticationManager.getInstance().getUser("time-recorder", "password");

    if (user == null) {
      AuthenticationManager.getInstance().addUser(user, "password");
    }

    user = AuthenticationManager.getInstance().getUser("time-recorder", "password");

    if (user == null) {
      writer.println("<p>401</p>");
      writer.println("</body>");
      writer.println("</html>");
      return;
    }

    final LocalDate date = LocalDate.now();
    final DatabaseEngine engine = new DatabaseEngine(ConnectionPool.getConnection());
    final Day day = engine.getDay(user, date.getYear(), date.getMonthValue(), date.getDayOfMonth());

    final List<Session> sessions = day.getItems();
    writer.println(date + " has " + sessions.size() + " sessions.<br>");
    for (final Session session : sessions) {
      writer.println(session);
      writer.println("<br>");
    }
    writer.println("</body>");
    writer.println("</html>");
  }
}