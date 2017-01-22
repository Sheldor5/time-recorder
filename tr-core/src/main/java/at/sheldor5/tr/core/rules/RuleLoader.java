package at.sheldor5.tr.core.rules;

import java.io.InputStream;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class RuleLoader {

  private static final Logger LOGGER = LogManager.getLogger(RuleLoader.class);

  public static List<Rule> getRules() {
    return null;
  }

  static boolean validateAgainstXSD(final InputStream xml, final InputStream xsd) {
    try {
      final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      final Schema schema = factory.newSchema(new StreamSource(xsd));
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xml));
      xml.close();
      xsd.close();
      return true;
    } catch(final Exception generalException) {
      LOGGER.warn(generalException.getMessage());
      return false;
    }
  }

}