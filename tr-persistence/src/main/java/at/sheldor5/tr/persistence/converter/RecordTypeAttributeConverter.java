package at.sheldor5.tr.persistence.converter;

import at.sheldor5.tr.api.time.RecordType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Class to convert record type to boolean value.
 */
@Converter
public class RecordTypeAttributeConverter implements AttributeConverter<RecordType, Boolean> {
  @Override
  public Boolean convertToDatabaseColumn(final RecordType attribute) {
    return attribute.getBoolean();
  }

  @Override
  public RecordType convertToEntityAttribute(final Boolean dbData) {
    return RecordType.getType(dbData);
  }
}
