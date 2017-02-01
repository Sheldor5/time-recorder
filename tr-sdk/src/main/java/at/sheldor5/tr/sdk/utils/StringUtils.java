package at.sheldor5.tr.sdk.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class StringUtils {

  public static String getMD5(final String string) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (final NoSuchAlgorithmException nsae) {
      throw new RuntimeException(nsae);
    }
    md.update(string.getBytes(), 0, string.length());
    return new BigInteger(1, md.digest()).toString(16);
  }
}
