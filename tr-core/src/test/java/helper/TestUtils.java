package helper;

import java.util.Random;

public class TestUtils {

  private static final Random RANDOM = new Random();

  public static synchronized long getRandomLong() {
    long next = RANDOM.nextLong();
    if (next < 0) {
      next  *= -1;
    }
    return next;
  }
}