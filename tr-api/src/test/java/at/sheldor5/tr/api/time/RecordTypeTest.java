package at.sheldor5.tr.api.time;

import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void test_checkin() {
    final RecordType checkin = RecordType.CHECKIN;
    Assert.assertTrue(checkin.getBoolean());
    Assert.assertEquals(1, checkin.getInteger());
    Assert.assertEquals("CHECKIN", checkin.toString());
  }

  @Test
  public void test_checkout() {
    final RecordType checkout = RecordType.CHECKOUT;
    Assert.assertFalse(checkout.getBoolean());
    Assert.assertEquals(0, checkout.getInteger());
    Assert.assertEquals("CHECKOUT", checkout.toString());
  }

  @Test
  public void test_static_methods() {
    Assert.assertEquals(RecordType.CHECKIN, RecordType.getType(true));
    Assert.assertEquals(RecordType.CHECKIN, RecordType.getType(1));

    Assert.assertEquals(RecordType.CHECKOUT, RecordType.getType(false));
    Assert.assertEquals(RecordType.CHECKOUT, RecordType.getType(0));

    Assert.assertNull(RecordType.getType(2));
    Assert.assertNull(RecordType.getType(-1));
  }
}