package at.sheldor5.tr.api.user;

import java.util.UUID;

/**
 * This class represent a userMapping.
 */
public class User {

  protected UUID uuid;
  protected String username;
  protected String password;
  protected String forename;
  protected String surname;
  protected Schedule schedule;

  public User() {
    this(null, null, null, null);
  }

  public User(final String username, final String password) {
    this(username, password, null, null);
  }

  public User(final String username, final String password, final String forename, final String surname) {
    this.username = username;
    this.password = password;
    this.forename = forename;
    this.surname = surname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(final String forename) {
    this.forename = forename;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(final String surname) {
    this.surname = surname;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(final UUID uuid) {
    this.uuid = uuid;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(final Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param other The object to compare.
   * @return True if both objects have the same username, forename, surname, id and uuid.
   */
  @Override
  public boolean equals(final Object other) {
    if (super.equals(other)) {
      return true;
    }
    if (other == null || !(other instanceof User)) {
      return false;
    }
    final User user = (User) other;
    return uuid.equals(user.uuid) && username.equals(user.username) && forename.equals(user.forename) && surname.equals(user.surname);
  }

  @Override
  public String toString() {
    return String.format("%s@%d: {%s, %s, %s, %s}", getClass().getSimpleName(), hashCode(), username, password, forename, surname);
  }
}
