package at.sheldor5.tr.api.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * This class provides some methods to get random data.
 */
public class RandomUtils {

  private static final SecureRandom RANDOM = new SecureRandom();

  /**
   * Create a random username with the given prefix.
   *
   * @param prefix The prefix of the username.
   * @return A random username with the given prefix.
   */
  public static String getRandomUsername(final String prefix) {
    long value;
    SecureRandom random;
    try {
      random = SecureRandom.getInstance("SHA1PRNG");
      value = random.nextLong();
      while (value < 0) {
        value = random.nextLong();
      }
    } catch (NoSuchAlgorithmException nsae) {
      System.out.println(nsae.getMessage());
      return prefix + getSecureRandomLong();
    }
    value = Math.abs(value);
    return prefix + Long.toString(value);
  }

  /**
   * TODO
   * @return
   */
  public static synchronized long getSecureRandomLong() {
    long next = RANDOM.nextLong();
    if (next < 0) {
      next  *= -1;
    }
    return next;
  }

  /**
   * TODO
   * @return
   */
  public static synchronized int getSecureRandomId() {
    int next = RANDOM.nextInt();
    while (next < 0) {
      next = RANDOM.nextInt();
    }
    return next;
  }
}
