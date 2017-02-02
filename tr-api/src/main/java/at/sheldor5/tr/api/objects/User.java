package at.sheldor5.tr.api.objects;

public class User {

  private int id = -1;
  private String username;
  private String forename;
  private String surname;

  public User() {

  }

  public User(int id, final String username, final String forename, final String surname) {
    setId(id);
    setUsername(username);
    setForename(forename);
    setSurname(surname);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("UserID was negative");
    }
    this.id = id;
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
    return username.equals(user.username) && forename.equals(user.forename) && surname.equals(user.surname) && id == user.id;
  }
}