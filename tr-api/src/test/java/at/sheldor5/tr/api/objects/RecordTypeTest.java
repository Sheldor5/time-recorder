package at.sheldor5.tr.api.objects;

import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void test_checkin() {
    final RecordType checkin = RecordType.CHECKIN;
    Assert.assertTrue("Boolean value should be true", checkin.getBoolean());
    Assert.assertEquals("Integer value schould be 1", 1, checkin.getInteger());
    Assert.assertEquals("String value should be CHECKIN", "CHECKIN", checkin.toString());
  }

  @Test
  public void test_checkout() {
    final RecordType checkout = RecordType.CHECKOUT;
    Assert.assertFalse("Boolean value should be false", checkout.getBoolean());
    Assert.assertEquals("Integer value schould be 0", 0, checkout.getInteger());
    Assert.assertEquals("String value should be CHECKOUT", "CHECKOUT", checkout.toString());
  }

  @Test
  public void test_static_methods() {
    Assert.assertEquals("Type should be CHECKIN", RecordType.CHECKIN, RecordType.getType(true));
    Assert.assertEquals("Type should be CHECKIN", RecordType.CHECKIN, RecordType.getType(1));

    Assert.assertEquals("Type should be CHECKOUT", RecordType.CHECKOUT, RecordType.getType(false));
    Assert.assertEquals("Type should be CHECKOUT", RecordType.CHECKOUT, RecordType.getType(0));

    Assert.assertEquals("Type should be null", null, RecordType.getType(2));
    Assert.assertEquals("Type should be null", null, RecordType.getType(-1));
  }
}