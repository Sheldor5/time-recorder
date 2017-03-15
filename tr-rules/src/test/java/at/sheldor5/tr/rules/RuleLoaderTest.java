package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.time.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

  @Test
  public void test_load_rules() throws IOException {
    final RuleLoader loader = new RuleLoader(FILE_XSD);
    final List<Rule> rules = loader.getRules(FILE_XML);

    // TODO
  }

  @Test
  public void test_apply_day() throws IOException {
    final RuleLoader loader = new RuleLoader(FILE_XSD);
    final List<Rule> rules = loader.getRules(FILE_XML);
    final LocalDate monday = LocalDate.of(2017, 1, 2);
    final Day day = new Day(monday);
    Record begin;
    Record end;
    Session session;

    begin = new Record(monday, LocalTime.of(3, 0), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(4, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(4, 30), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(8, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(9, 0), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(16, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(17, 0), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(19, 1), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(19, 16), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(22, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);


    for (final Rule rule : rules) {
      Assert.assertTrue("Rule should apply", rule.applies(day));
      rule.apply(day);
    }

    final List<Session> sessions = day.getItems();

    Assert.assertEquals("Rule should split session into 7 sessions", 7, sessions.size());

    Assert.assertEquals("Session summary should be 3600 seconds (1 hour)", 3600L, sessions.get(0).getSummary());
    Assert.assertEquals("Session valued summary should be 5400 seconds (1 hour, 30 minutes)", 5400L, sessions.get(0).getValuedSummary());

    Assert.assertEquals("Session summary should be 1800 seconds (30 minutes)", 1800L, sessions.get(1).getSummary());
    Assert.assertEquals("Session valued summary should be 2700 seconds (45 minutes)", 2700L, sessions.get(1).getValuedSummary());

    Assert.assertEquals("Session summary should be 10800 seconds (3 hours)", 10800L, sessions.get(2).getSummary());
    Assert.assertEquals("Session valued summary should be 10800 seconds (3 hours)", 10800L, sessions.get(2).getValuedSummary());

    Assert.assertEquals("Session summary should be 25200 seconds (7 hours)", 25200L, sessions.get(3).getSummary());
    Assert.assertEquals("Session valued summary should be 25200 seconds (7 hours)", 25200L, sessions.get(3).getValuedSummary());

    Assert.assertEquals("Session summary should be 7200 seconds (2 hours)", 7200L, sessions.get(4).getSummary());
    Assert.assertEquals("Session valued summary should be 7200 seconds (2 hours)", 7200L, sessions.get(4).getValuedSummary());

    Assert.assertEquals("Session summary should be 60 seconds (1 minute)", 60L, sessions.get(5).getSummary());
    Assert.assertEquals("Session valued summary should be 90 seconds (1 minute, 30 seconds)", 90L, sessions.get(5).getValuedSummary());

    Assert.assertEquals("Session summary should be 9800L seconds (2 hours, 44 minutes)", 9840L, sessions.get(6).getSummary());
    Assert.assertEquals("Session valued summary should be 14700 seconds (4 hours, 6 minutes)", 14760L, sessions.get(6).getValuedSummary());

    Assert.assertEquals("Day summary should be 58500L seconds (16 hours, 15 minutes)", 58500L, day.getSummary());
    Assert.assertEquals("Day valued summary should be 3960 seconds (18 hours, 22 minutes, 30 seconds)", 66150L, day.getValuedSummary());
  }
}