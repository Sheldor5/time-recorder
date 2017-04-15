package at.sheldor5.tr.persistence.converter;

import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for Java8's {@link LocalDate} to SQL's {@link Date}.
 *
 * @deprecated See https://github.com/hibernate/hibernate-orm/wiki/Migration-Guide---5.2.
 */
@Deprecated
@Converter
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(final LocalDate date) {
    return date == null ? null : Date.valueOf(date);
  }

  @Override
  public LocalDate convertToEntityAttribute(final Date sqlDate) {
    return sqlDate == null ? null : sqlDate.toLocalDate();
  }
}
