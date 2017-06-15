package at.sheldor5.tr.web.export;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.exporter.ExporterManager;
import at.sheldor5.tr.web.BusinessLayer;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "export", urlPatterns = "/export")
public class Exporter extends HttpServlet {

  private static final List<String> TYPES = new ArrayList<>();

  static {
    TYPES.add("month");
    TYPES.add("year");
  }

  @Inject
  private BusinessLayer businessLayer;

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {

    if (businessLayer == null || businessLayer.getUser() == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String pluginName = request.getParameter("exporter");
    String exportType = request.getParameter("type");

    if (!TYPES.contains(exportType)) {
      response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
      return;
    }

    String dateText = request.getParameter("date");
    String parts[] = dateText.split("-");

    if (parts.length != 2) {
      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      return;
    }

    LocalDate date;

    try {
      int year = Integer.parseInt(parts[0]);
      int month = Integer.parseInt(parts[1]);
      date = LocalDate.of(year, month, 1);
    } catch (final NumberFormatException | DateTimeException e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      return;
    }

    ExporterPlugin plugin = ExporterManager.getInstance().getPlugin(pluginName);

    if (plugin == null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    plugin.initialize(businessLayer.getUserCopy(), response.getOutputStream());

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(plugin.getMimeType().toString());

    switch (exportType) {
      case "month":
        Month month = businessLayer.getMonth(date);
        plugin.export(month);
        break;
      case "year":
        break;
    }
  }
}
