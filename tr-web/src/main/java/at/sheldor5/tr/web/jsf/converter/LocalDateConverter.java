package at.sheldor5.tr.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Michael Palata
 * @date 05.06.2017
 */
@FacesConverter(value = "at.sheldor5.tr.LocalDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  @Override
  public LocalDate getAsObject(final FacesContext context, final UIComponent component, final String value) {
    return LocalDate.parse(value, DATE_FORMATTER);
  }

  @Override
  public String getAsString(final FacesContext context, final UIComponent component, final LocalDate value) {
    return value.format(DATE_FORMATTER);
  }
}
