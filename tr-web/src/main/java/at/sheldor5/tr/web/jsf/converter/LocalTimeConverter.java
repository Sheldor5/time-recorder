package at.sheldor5.tr.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Michael Palata
 * @date 05.06.2017
 */
@FacesConverter(value = "at.sheldor5.tr.LocalTimeConverter")
public class LocalTimeConverter implements Converter<LocalTime> {

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

  @Override
  public LocalTime getAsObject(final FacesContext context, final UIComponent component, final String value) {
    return LocalTime.parse(value, TIME_FORMATTER);
  }

  @Override
  public String getAsString(final FacesContext context, final UIComponent component, final LocalTime value) {
    return value.format(TIME_FORMATTER);
  }
}
