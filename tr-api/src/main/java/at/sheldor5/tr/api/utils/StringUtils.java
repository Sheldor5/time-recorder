package at.sheldor5.tr.api.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utils Class to operate with strings.
 */
public class StringUtils {

  /**
   * Calculates the MD5 hash of any given string.
   *
   * @param string The string toi hash.
   * @return The MD5 hash of the given string.
   */
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