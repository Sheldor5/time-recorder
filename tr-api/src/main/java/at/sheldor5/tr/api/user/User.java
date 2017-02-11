package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.utils.UuidUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class User {

  private static final SecureRandom RANDOM = new SecureRandom();

  private int id = -1;
  private UUID uuid;
  private byte[] uuid_bytes;
  private String username;
  private Schedule schedule;
  private String forename;
  private String surname;

  public User() {

  }

  public User(final String username, final String forename, final String surname) {
    setUsername(username);
    setForename(forename);
    setSurname(surname);
  }

  public User(int id, final String username, final String forename, final String surname) {
    this(username, forename, surname);
    setId(id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    if (id < 1) {
      throw new IllegalArgumentException("Invalid User ID");
    }
    this.id = id;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(final Schedule schedule) {
    this.schedule = schedule;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    if (username == null || username.length() < 5 || username.length() > 32) {
      throw new IllegalArgumentException("Username was null or its length exceeded the limit of 32 characters");
    }
    this.username = username;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(final String forename) {
    if (forename == null || forename.length() > 32) {
      throw new IllegalArgumentException("Forename was null or its length exceeded the limit of 32 characters");
    }
    this.forename = forename;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(final String surname) {
    if (surname == null || surname.length() > 32) {
      throw new IllegalArgumentException("Surname was null or its length exceeded the limit of 32 characters");
    }
    this.surname = surname;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || !(other instanceof User)) {
      return false;
    }
    final User user = (User) other;
    if (username.equals(user.username) && forename.equals(user.forename) && surname.equals(user.surname)) {
      if (id < 0 || user.id < 0) {
        if (uuid == null) {
          return false;
        }
        return uuid.equals(user.uuid);
      }
      return id == user.id;
    }
    return false;
  }

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

  public static synchronized long getSecureRandomLong() {
    long next = RANDOM.nextLong();
    if (next < 0) {
      next  *= -1;
    }
    return next;
  }

  public static synchronized int getSecureRandomId() {
    int next = RANDOM.nextInt();
    while (next < 0) {
      next = RANDOM.nextInt();
    }
    return next;
  }

  public UUID getUuid() {
    return uuid;
  }

  public byte[] getUuidBytes() {
    return uuid_bytes;
  }

  public void setUuid(final UUID uuid) {
    if (uuid == null) {
      throw new IllegalArgumentException("User UUID was null");
    }
    this.uuid = uuid;
    this.uuid_bytes = UuidUtils.getBytes(uuid);
  }

  public void setUuidBytes(final byte[] uuid_bytes) {
    if (uuid_bytes == null || uuid_bytes.length != 16) {
      throw new IllegalArgumentException("Invalid User UUID");
    }
    this.uuid_bytes = uuid_bytes;
    this.uuid = UuidUtils.getUuid(uuid_bytes);
  }
}