package at.sheldor5.tr.api.user;

import java.util.UUID;

/**
 * This class represent a userMapping.
 */
public class User {

  private UUID uuid;
  private String username;
  private String password;
  private String forename;
  private String surname;
  private Schedule schedule;

  /**
   * Default constructor for empty userMapping.
   */
  public User() {
    this(null, null, null, null);
  }

  /**
   * Default constructor for empty userMapping.
   */
  public User(final String username, final String password) {
    this(username, password, null, null);
  }

  /**
   * Constructor for given properties.
   *
   * @param username The username of the userMapping.
   * @param password The password of the userMapping (should be hashed).
   * @param forename The forename of the userMapping.
   * @param surname  The surname of the userMapping.
   */
  public User(final String username, final String password, final String forename, final String surname) {
    this.username = username;
    this.password = password;
    this.forename = forename;
    this.surname = surname;
  }

  /**
   * Getter for the username.
   *
   * @return The username of this userMapping.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter for the username.
   *
   * @param username The username of this userMapping.
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Getter for the forename.
   *
   * @return The forename of this userMapping.
   */
  public String getForename() {
    return forename;
  }

  /**
   * Setter for the forename.
   *
   * @param forename The forename of this userMapping.
   */
  public void setForename(final String forename) {
    this.forename = forename;
  }

  /**
   * Getter for the surname.
   *
   * @return The surname of this userMapping.
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Setter for the surname.
   *
   * @param surname The surname of this userMapping.
   */
  public void setSurname(final String surname) {
    this.surname = surname;
  }

  /**
   * Getter for the UUID.
   *
   * @return The UUID of this userMapping.
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Setter fot the UUID.
   *
   * @param uuid The UUID of this userMapping.
   */
  public void setUuid(final UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Getter for the schedule.
   *
   * @return The schedule of this userMapping.
   */
  public Schedule getSchedule() {
    return schedule;
  }

  /**
   * Setter for the schedule.
   *
   * @param schedule The schedule of this userMapping.
   */
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
