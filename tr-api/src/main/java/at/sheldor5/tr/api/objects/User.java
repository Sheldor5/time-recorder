package at.sheldor5.tr.api.objects;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class User {

  private byte[] uuid;
  private String username;
  private String forename;
  private String surname;

  public User() {

  }

  public User(final String username, final String forename, final String surname) {
    setUsername(username);
    setForename(forename);
    setSurname(surname);
  }

  public User(final UUID uuid, final String username, final String forename, final String surname) {
    this(username, forename, surname);
    setUUID(uuid);
  }

  public UUID getUUID() {
    if (uuid == null) {
      return null;
    }
    final ByteBuffer bb = ByteBuffer.wrap(uuid);
    long mostSigBits = bb.getLong();
    long leastSigBits = bb.getLong();
    return new UUID(mostSigBits, leastSigBits);
  }

  public byte[] getUUIDBytes() {
    return uuid;
  }

  public void setUUID(final UUID uuid) {
    if (uuid == null) {
      throw new IllegalArgumentException("User UUID was null");
    }
    final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    this.uuid = bb.array();
  }

  public void setUUIDBytes(final byte[] uuid) {
    if (uuid == null || uuid.length != 16) {
      throw new IllegalArgumentException("Invalid User UUID");
    }
    this.uuid = uuid;
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
    return username.equals(user.username) && forename.equals(user.forename) && surname.equals(user.surname) && Arrays.equals(uuid, user.uuid);
  }
}