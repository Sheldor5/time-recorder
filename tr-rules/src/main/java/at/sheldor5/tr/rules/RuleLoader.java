package at.sheldor5.tr.rules;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RuleLoader {

  private static final Logger LOGGER = Logger.getLogger(RuleLoader.class.getName());
  private static final Map<String, Integer> DAY_VALUE_MAP = new HashMap<>();

  static {
    DAY_VALUE_MAP.put("monday", 1);
    DAY_VALUE_MAP.put("tuesday", 2);
    DAY_VALUE_MAP.put("wednesday", 3);
    DAY_VALUE_MAP.put("thursday", 4);
    DAY_VALUE_MAP.put("friday", 5);
    DAY_VALUE_MAP.put("saturday", 6);
    DAY_VALUE_MAP.put("sunday", 7);
  }

  private final InputStream xsd;

  public RuleLoader(final InputStream xsd) throws IOException {
    this.xsd = xsd;
  }

  public List<Rule> getRules(final InputStream xml) throws IOException {
    final List<Rule> list = new ArrayList<>();

    Element root;

    try {
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document document = builder.parse(xml);
      root = document.getDocumentElement();
    } catch (final ParserConfigurationException | SAXException pce) {
      LOGGER.severe(pce.getMessage());
      return list;
    }

    return getRules(root);
  }

  private List<Rule> getRules(final Element root) {
    final List<Rule> list = new ArrayList<>();

    if (root == null) {
      return list;
    }

    final NodeList rules = root.getChildNodes();
    for (int r = 0; r < rules.getLength(); r++) {
      final Node node = rules.item(r);

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        final Element element = (Element) node;
        list.add(getRule(element));
      }
    }

    return list;
  }

  private Rule getRule(final Element element) {
    final Rule rule = new Rule();
    final NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      final Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        switch (node.getNodeName()) {
          case "name":
            rule.name = node.getTextContent();
            break;
          case "key-date":
            rule.keyDate = LocalDate.parse(node.getTextContent());
            break;
          case "description":
            rule.description = node.getTextContent();
            break;
          case "before":
            rule.timeOperations.add(getTimeOperation((Element) node));
            break;
          case "after":
            rule.timeOperations.add(getTimeOperation((Element) node));
            break;
          default:
            LOGGER.info("RuleClass type \"" + node.getNodeName() + "\" is not implemented yet!");
            break;
        }
      }
    }

    return rule;
  }

  private TimeOperation getTimeOperation(final Element element) {
    String type = element.getTagName();
    LocalTime time = LocalTime.MIN.plusNanos(1);
    double multiplier = 1.0D;
    Integer days[] = new Integer[0];

    final NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      final Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        switch (node.getNodeName()) {
          case "time":
            time = LocalTime.parse(node.getTextContent());
            break;
          case "multiplier":
            multiplier = Double.parseDouble(node.getTextContent());
            break;
          case "days":
            days = getDays((Element) node);
            break;
          default:
            LOGGER.severe("Unknown element: " + node.getNodeName());
            break;
        }
      }
    }
    switch (type) {
      case "before":
        return new Before(time, multiplier, days);
      case "after":
        return new After(time, multiplier, days);
      default:
        // TODO
        break;
    }
    return null;
  }

  private Integer[] getDays(final Element element) {
    final List<Integer> days = new ArrayList<>();
    final NodeList nodeList = element.getChildNodes();
    int from = 0;
    int to = 0;
    for (int i = 0; i < nodeList.getLength(); i++) {
      final Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        final String name = node.getNodeName();
        if ("day".equals(name)) {
          days.add(DAY_VALUE_MAP.get(node.getTextContent()));
        } else if ("from".equals(name)) {
          from = DAY_VALUE_MAP.get(node.getTextContent());
        } else if ("to".equals(name)) {
          to = DAY_VALUE_MAP.get(node.getTextContent());
        } else {
          LOGGER.severe("Unknown element: " + name);
        }
      }
    }
    if (from > 0) {
      for (int i = from; i <= to; i++) {
        days.add(i);
      }
    }
    return days.toArray(new Integer[days.size()]);
  }

  public boolean validateXML(final InputStream xmlfis) {
    try {
      final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      final Schema schema = factory.newSchema(new StreamSource(xsd));
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(xmlfis));
      try {
        xsd.reset();
      } catch (final IOException ioe) {
        ioe.printStackTrace();
      }
      return true;
    } catch (final Exception generalException) {
      LOGGER.warning(generalException.getMessage());
      return false;
    }
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
    } catch (final Exception generalException) {
      LOGGER.warning(generalException.getMessage());
      return false;
    }
  }

}