package at.sheldor5.tr.core.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class RuleLoader {

  private static final Logger LOGGER = LogManager.getLogger(RuleLoader.class);

  private static File XSD;

  private final File xsd;

  public RuleLoader(final String xsdPath) throws IOException {
    if (xsdPath == null || xsdPath.isEmpty()) {
      LOGGER.error("XSD path is null or empty");
      throw new FileNotFoundException("XSD path is null or empty");
    }
    final File file = new File(xsdPath);
    if (!file.exists() || file.isDirectory()) {
      LOGGER.error("XSD file does not exist or is no file");
      throw new FileNotFoundException("XSD file does not exist or is no file");
    }
    this.xsd = file;
  }

  public List<Rule> getRules(final String xmlPath) throws IOException {
    final List<Rule> list = new ArrayList<>();

    if (xmlPath == null || xmlPath.isEmpty()) {
      LOGGER.error("XML path is null or empty");
      throw new FileNotFoundException("XML path is null or empty");
    }
    final File file = new File(xmlPath);
    if (!file.exists()) {
      LOGGER.error("XML file does not exist or is no file");
      throw new FileNotFoundException("XML file does not exist or is no file");
    }

    Element root;

    try {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document document = builder.parse(file);
      root = document.getDocumentElement();
    } catch (final ParserConfigurationException | SAXException pce) {
      LOGGER.error(pce.getMessage());
      return list;
    }

    final NodeList rules = root.getChildNodes();
    for (int r = 0; r < rules.getLength(); r++) {
      final Rule rule = new Rule();
      final Node node = rules.item(r);
    }

    return list;
  }

  private List<Rule> getRules(final Element root) {
    final List<Rule> list = new ArrayList<>();

    if (root == null) {
      return list;
    }

    final NodeList rules = root.getChildNodes();
    for (int r = 0; r < rules.getLength(); r++) {
      final Node node = rules.item(r);
      final Rule rule = new Rule();

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        final Element element = (Element) node;
        rule.name = element.getElementsByTagName("name").item(0).getTextContent();
        rule.keyDate = LocalDate.parse(element.getElementsByTagName("key-date").item(0).getTextContent());
        // before|after
        //rule.time = Time.valueOf(element.getElementsByTagName("time").item(0).getTextContent());
        /*
        rule.multiplier = Double.valueOf();
        rule.name = element.getElementsByTagName("name").item(0).getTextContent();
        rule.name = element.getElementsByTagName("name").item(0).getTextContent();
        */
      }
    }

    return list;
  }

  public static void setXSD(final File file) throws IOException {
    if (file == null || !file.exists()) {
      throw new FileNotFoundException();
    }
    XSD = file;
  }

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