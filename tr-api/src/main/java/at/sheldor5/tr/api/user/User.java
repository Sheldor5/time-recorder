package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.utils.UuidUtils;

import java.util.UUID;

/**
 * This class represent a user.
 */
public class User {

  private int id = -1;
  private UUID uuid;
  private String username;
  private String password;
  private Schedule schedule;
  private String forename;
  private String surname;

  /**
   * Default constructor.
   */
  public User() {

  }

  /**
   * Constructor for given name.
   *
   * @param username The username of the user.
   * @param forename The forename of the user.
   * @param surname  The surname of the user.
   */
  public User(final String username, final String forename, final String surname) {
    setUsername(username);
    setForename(forename);
    setSurname(surname);
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
    if (id < 1) {
      throw new IllegalArgumentException("Invalid User ID");
    }
    this.id = id;
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
    if (username == null || username.length() < 5 || username.length() > 32) {
      throw new IllegalArgumentException("Username was null or its length exceeded the limit of 32 characters");
    }
    this.username = username;
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
    if (forename == null || forename.length() > 32) {
      throw new IllegalArgumentException("Forename was null or its length exceeded the limit of 32 characters");
    }
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
    if (surname == null || surname.length() > 32) {
      throw new IllegalArgumentException("Surname was null or its length exceeded the limit of 32 characters");
    }
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
    if (uuid == null) {
      throw new IllegalArgumentException("User UUID was null");
    }
    this.uuid = uuid;
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
}