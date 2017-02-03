package at.sheldor5.tr.api;

import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Year;
import javax.activation.MimeType;
import java.io.InputStream;

@Exporter(name = "test", displayNamePropertyIdentifier = "test.plugin.displayname")
public class TestExporterPlugin implements ExporterPlugin {

  private static final MimeType DEFAULT = new MimeType();

  @Override
  public MimeType getMimeType() {
    return DEFAULT;
  }

  @Override
  public InputStream export(Day day) {
    return null;
  }

  @Override
  public InputStream export(Month month) {
    return null;
  }

  @Override
  public InputStream export(Year year) {
    return null;
  }

  @Override
  public InputStream fullExport(Year year) {
    return null;
  }
}