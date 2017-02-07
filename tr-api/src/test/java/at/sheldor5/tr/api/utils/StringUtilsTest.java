package at.sheldor5.tr.api.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

  @Test
  public void test_string_to_md5()  {
    Assert.assertEquals("MD5 calculation failed", "179ad45c6ce2cb97cf1029e212046e81", StringUtils.getMD5("testpass"));
  }
}