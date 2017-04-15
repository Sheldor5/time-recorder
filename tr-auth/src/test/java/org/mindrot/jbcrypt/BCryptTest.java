package org.mindrot.jbcrypt;

import org.junit.Assert;
import org.junit.Test;

public class BCryptTest {

  @Test
  public void test() {
    final String password = "admin";
    final String salt = BCrypt.gensalt(12);
    final String hashedPassword = BCrypt.hashpw(password, salt);
    System.out.println(hashedPassword.length());
    System.out.println(hashedPassword);
    Assert.assertTrue(BCrypt.checkpw(password, hashedPassword));
  }
}
