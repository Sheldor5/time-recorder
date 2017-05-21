package at.sheldor5.tr.web.export;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "export", urlPatterns = "/export")
public class Exporter extends HttpServlet {

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
    final HttpSession session = request.getSession();
    final Object object = session.getAttribute("userMapping");

    if (object == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    response.setContentType("text/html");

    final PrintWriter writer = response.getWriter();
    writer.println("<html>");
    writer.println("<head>");
    writer.println("<title>Sample Application Servlet Page</title>");
    writer.println("</head>");
    writer.println("<body bgcolor=white>");
    writer.println("<p>under construction</p>");
    writer.println("</body>");
    writer.println("</html>");
  }
}