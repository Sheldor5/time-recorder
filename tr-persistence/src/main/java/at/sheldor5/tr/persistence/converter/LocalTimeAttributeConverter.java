package at.sheldor5.tr.persistence.converter;

import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.AttributeConverter;

/**
 * Converter for Java8's {@link LocalTime} to SQL's {@link Time}.
 *
 * @deprecated See https://github.com/hibernate/hibernate-orm/wiki/Migration-Guide---5.2.
 */
@Deprecated
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

  @Override
  public Time convertToDatabaseColumn(final LocalTime time) {
    return time == null ? null : Time.valueOf(time);
  }

  @Override
  public LocalTime convertToEntityAttribute(final Time sqlTime) {
    return sqlTime == null ? null : sqlTime.toLocalTime();
  }
}
