package at.sheldor5.tr.exporter.html;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.TimeUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Michael Palata
 * @date 10.06.2017
 */
public class BootstrapHtmlExporterHelper {

  static void header(final BufferedWriter writer, final User user) {
    try {
      writer.write("<head>");
      writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">");
      writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">");
      //writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
      writer.write("</head>");

      writer.write("<div class=\"row\">");
      writer.write("<div class=\"col-md-1\">&nbsp;</div>");
      writer.write(String.format("<div class=\"col-md-10\"><div class=\"page-header\"><h2>%s %s <small>%s</small></h2></div></div>", user.getForename(), user.getSurname(), user.getUsername()));
      writer.write("<div class=\"col-md-1\">&nbsp;</div>");
      writer.write("</div>");

      writer.write("<div class=\"row\">");
      writer.write("<div class=\"col-md-1\">&nbsp;</div>");
      writer.write("<div class=\"col-md-10\">");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void day(final BufferedWriter writer, final Day day, boolean tableHead) throws IOException {
    writer.write(String.format("<label>%s</label>", day.getDate()));

    if (tableHead) {
      tableHead(writer);
    }

    for (final Session session : day.getItems()) {
      writer.write("<div class=\"row\">");
      writer.write(String.format("<div class=\"col-md-1\">%s</div>", session.getStart()));
      writer.write(String.format("<div class=\"col-md-1\">%s</div>", session.getEnd()));
      writer.write(String.format("<div class=\"col-md-1\">%s</div>", TimeUtils.getHumanReadableSummary(session.getSummary())));
      writer.write(String.format("<div class=\"col-md-2\">%s</div>", session.getProject() == null ? "" : session.getProject().getName()));
      writer.write(String.format("<div class=\"col-md-1\">%s</div>", TimeUtils.getHumanReadableSummary(session.getValuedSummary())));
      writer.write("<div class=\"col-md-5\">&nbsp;</div>");
      writer.write("</div>");
    }
    writer.write("<div class=\"row\">");
    writer.write("<div class=\"col-md-1\">&nbsp;</div>");
    writer.write("<div class=\"col-md-1\">&nbsp;</div>");
    writer.write(String.format("<div class=\"col-md-1\"><label>%s</label></div>", TimeUtils.getHumanReadableSummary(day.getSummary())));
    writer.write("<div class=\"col-md-2\">&nbsp;</div>");
    writer.write(String.format("<div class=\"col-md-1\"><label>%s</label></div>", TimeUtils.getHumanReadableSummary(day.getValuedSummary())));
    writer.write("<div class=\"col-md-5\">&nbsp;</div>");
    writer.write("</div>");
    writer.flush();
  }

  static void month(final BufferedWriter writer, final Month month, boolean tableHead) throws IOException {
    String summary = TimeUtils.getHumanReadableSummary(month.getSummary());
    String valuedSummary = TimeUtils.getHumanReadableSummary(month.getValuedSummary());

    writer.write("<div class=\"row\">");
    writer.write(String.format("<div class=\"col-md-2\"><h3>%s %s</h3></div>", getMonthString(month.getDate().getMonthValue()), month.getDate().getYear()));
    writer.write(String.format("<div class=\"col-md-3\"><h3><small>%s</small></h3></div>", summary));
    writer.write(String.format("<div class=\"col-md-1\"><h3><small>%s</small></h3></div>", valuedSummary));
    writer.write("<div class=\"col-md-5\">&nbsp;</div>");
    writer.write("</div>");

    if (tableHead) {
      tableHead(writer);
    }

    for (final Day day : month.getItems()) {
      day(writer, day, false);
    }

    writer.write("</br>");
    writer.write("<div class=\"row\">");
    writer.write("<div class=\"col-md-2 bg-primary\"><label>Gesamt:</label></div>");
    writer.write(String.format("<div class=\"col-md-1 bg-primary\"><label>%s</label></div>", summary));
    writer.write("<div class=\"col-md-2 bg-primary\"><label>&nbsp;</label></div>");
    writer.write(String.format("<div class=\"col-md-1 bg-primary\"><label>%s</label></div>", valuedSummary));
    writer.write("<div class=\"col-md-5\">&nbsp;</div>");
    writer.write("</div>");
    writer.write("</br>");
    writer.write("</br>");
  }

  private static void tableHead(final BufferedWriter writer) throws IOException {
    writer.write("<div class=\"row\">");
    writer.write("<div class=\"col-md-1 bg-primary\"><label>von</label></div>");
    writer.write("<div class=\"col-md-1 bg-primary\"><label>bis</label></div>");
    writer.write("<div class=\"col-md-1 bg-primary\"><label>Zeit</label></div>");
    writer.write("<div class=\"col-md-2 bg-primary\"><label>Projekt</label></div>");
    writer.write("<div class=\"col-md-1 bg-primary\"><label>Zeitplan</label></div>");
    writer.write("<div class=\"col-md-5\">&nbsp;</div>");
    writer.write("</div>");
    writer.write("</br>");
  }

  static void footer(final BufferedWriter writer) {
    try {
      writer.write("</div></div>\"");

      writer.write("<script>");
      //writer.write("(function () { window.print(); })();");
      writer.write("</script>");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getMonthString(int i) {
    switch (i) {
      case 1:
        return "J&auml;nner";
      case 2:
        return "Februar";
      case 3:
        return "M&auml;rz";
      case 4:
        return "April";
      case 5:
        return "Mai";
      case 6:
        return "Juni";
      case 7:
        return "Juli";
      case 8:
        return "August";
      case 9:
        return "September";
      case 10:
        return "Oktober";
      case 11:
        return "November";
      case 12:
        return "Dezember";
      default:
        return "unknown";
    }
  }

}
