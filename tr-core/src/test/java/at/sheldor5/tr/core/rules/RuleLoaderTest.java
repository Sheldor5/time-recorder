package at.sheldor5.tr.core.rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RuleLoaderTest {

  private static File FILE_XSD;
  private static File FILE_XML;
  private static File FILE_INVALID_XML_1;
  private static File FILE_INVALID_XML_2;
  private static File FILE_INVALID_XML_3;
  private static File FILE_INVALID_XML_4;
  private static File FILE_INVALID_XML_5;

  @Before
  public void init() throws URISyntaxException {
    FILE_XSD = new File(this.getClass().getResource("/rules/rules.xsd").toURI());
    FILE_XML = new File(this.getClass().getResource("/rules/austria.xml").toURI());
    FILE_INVALID_XML_1 = new File(this.getClass().getResource("/rules/invalid_1.xml").toURI());
    FILE_INVALID_XML_2 = new File(this.getClass().getResource("/rules/invalid_2.xml").toURI());
    FILE_INVALID_XML_3 = new File(this.getClass().getResource("/rules/invalid_3.xml").toURI());
    FILE_INVALID_XML_4 = new File(this.getClass().getResource("/rules/invalid_4.xml").toURI());
    FILE_INVALID_XML_5 = new File(this.getClass().getResource("/rules/invalid_5.xml").toURI());
  }

  @Test
  public void test_valid_xml() throws IOException {
    final FileInputStream xsd = new FileInputStream(FILE_XSD);
    final FileInputStream xml = new FileInputStream(FILE_XML);
    Assert.assertTrue("XML should successfully be validated", RuleLoader.validateAgainstXSD(xml, xsd));
  }

  @Test
  public void test_invalid_xmls() throws IOException {
    FileInputStream xsd;
    FileInputStream xml;

    xsd = new FileInputStream(FILE_XSD);
    xml = new FileInputStream(FILE_INVALID_XML_1);
    Assert.assertFalse("XML should be invalid", RuleLoader.validateAgainstXSD(xml, xsd));

    xsd = new FileInputStream(FILE_XSD);
    xml = new FileInputStream(FILE_INVALID_XML_2);
    Assert.assertFalse("XML should be invalid", RuleLoader.validateAgainstXSD(xml, xsd));

    xsd = new FileInputStream(FILE_XSD);
    xml = new FileInputStream(FILE_INVALID_XML_3);
    Assert.assertFalse("XML should be invalid", RuleLoader.validateAgainstXSD(xml, xsd));

    xsd = new FileInputStream(FILE_XSD);
    xml = new FileInputStream(FILE_INVALID_XML_4);
    Assert.assertFalse("XML should be invalid", RuleLoader.validateAgainstXSD(xml, xsd));

    xsd = new FileInputStream(FILE_XSD);
    xml = new FileInputStream(FILE_INVALID_XML_5);
    Assert.assertFalse("XML should be invalid", RuleLoader.validateAgainstXSD(xml, xsd));
  }
}