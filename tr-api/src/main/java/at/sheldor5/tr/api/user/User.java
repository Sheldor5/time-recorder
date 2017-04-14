package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.utils.StringUtils;

import java.util.UUID;

/**
 * This class represent a user.
 */
public class User {

  private int id;
  private UUID uuid;
  private String username;
  private String password;
  private String salt;
  private String forename;
  private String surname;
  private Schedule schedule;

  public User() {
    this(-1);
  }

  public User(int id) {
    this(id, null);
  }

  public User(int id, final UUID uuid) {
    this(id, uuid, null);
  }

  public User(int id, final UUID uuid, final String username) {
    this(username, null, null);
    this.id = id;
    this.uuid = uuid;
  }

  /**
   * Constructor for given name.
   *
   * @param username The username of the user.
   * @param forename The forename of the user.
   * @param surname  The surname of the user.
   */
  public User(final String username, final String plainTextPassword, final String forename, final String surname) {
    this.username = username;
    setPlainTextPassword(plainTextPassword);
    this.forename = forename;
    this.surname = surname;
  }

  /**
   * Constructor for given name.
   *
   * @param username The username of the user.
   * @param forename The forename of the user.
   * @param surname  The surname of the user.
   */
  public User(final String username, final String forename, final String surname) {
    this.username = username;
    this.forename = forename;
    this.surname = surname;
  }

  /**
   * Constructor for given name and id.
   *
   * @param id       The id of the user.
   * @param username The username of the user.
   * @param forename The forename of the user.
   * @param surname  The surname of the user.
   */
  public User(int id, final String username, final String forename, final String surname) {
    this(username, forename, surname);
    setId(id);
  }

  /**
   * Getter for the id.
   *
   * @return The id of this user.
   */
  public int getId() {
    return id;
  }

  /**
   * Setter for the id.
   *
   * @param id The id of this user.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Getter for the username.
   *
   * @return The username of this user.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter for the username.
   *
   * @param username The username of this user.
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

  public void setPlainTextPassword(final String plainTextPassword) {
    this.password = StringUtils.getMD5(plainTextPassword);
  }

  /**
   * Getter for the forename.
   *
   * @return The forename of this user.
   */
  public String getForename() {
    return forename;
  }

  /**
   * Setter for the forename.
   *
   * @param forename The forename of this user.
   */
  public void setForename(final String forename) {
    this.forename = forename;
  }

  /**
   * Getter for the surname.
   *
   * @return The surname of this user.
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Setter for the surname.
   *
   * @param surname The surname of this user.
   */
  public void setSurname(final String surname) {
    this.surname = surname;
  }

  /**
   * Getter for the UUID.
   *
   * @return The UUID of this user.
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Setter fot the UUID.
   *
   * @param uuid The UUID of this user.
   */
  public void setUuid(final UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Getter for the schedule.
   *
   * @return The schedule of this user.
   */
  public Schedule getSchedule() {
    return schedule;
  }

  /**
   * Setter for the schedule.
   *
   * @param schedule The schedule of this user.
   */
  public void setSchedule(final Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param other The object to compare.
   * @return      True if both objects have the same username, forename, surname, id and uuid.
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

  @Override
  public String toString() {
    return String.format("%s@%d: {%d, %s, %s, %s, %s}", getClass().getSimpleName(), hashCode(), id, username, password, forename, surname);
  }
}
