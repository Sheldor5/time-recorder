package at.sheldor5.tr.sdk.utils;

import at.sheldor5.tr.sdk.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 01.02.2017.
 */
public class StringUtilsTest {

  @Test
  public void test_string_to_md5()  {
    Assert.assertEquals("MD5 calculation failed", "179ad45c6ce2cb97cf1029e212046e81", StringUtils.getMD5("testpass"));
  }
}