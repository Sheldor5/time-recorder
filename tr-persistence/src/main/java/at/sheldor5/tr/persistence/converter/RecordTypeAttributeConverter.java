package at.sheldor5.tr.persistence.converter;

import at.sheldor5.tr.api.time.RecordType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Class to convert record type to boolean value.
 */
@Converter
public class RecordTypeAttributeConverter implements AttributeConverter<RecordType, Boolean> {
  public RecordTypeAttributeConverter() {
    System.out.println("RecordTypeAttributeConverter()");
  }
  @Override
  public Boolean convertToDatabaseColumn(final RecordType attribute) {
    System.out.println("convertToDatabaseColumn()");
    if (attribute == null) {
      return null;
    }
    return attribute.getBoolean();
  }

  @Override
  public RecordType convertToEntityAttribute(final Boolean dbData) {
    System.out.println("convertToEntityAttribute()");
    return RecordType.getType(dbData);
  }
}
