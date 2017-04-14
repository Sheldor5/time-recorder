package org.springframework.security.crypto.bcrypt;

import org.junit.Assert;
import org.junit.Test;

public class BCrypt2Test {

  @Test
  public void test() {
    final String password = "admin";
    final String salt = BCrypt.gensalt();
    final String hashedPassword = BCrypt.hashpw(password, salt);
    Assert.assertTrue(BCrypt.checkpw(password, hashedPassword));
  }
}
