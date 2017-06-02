package at.sheldor5.tr.api.user;

import java.util.UUID;

public class UserMapping {

  protected int id;
  protected UUID uuid;
  protected Role role;
  protected User user;

  public UserMapping() {
    this(null);
  }

  public UserMapping(final UUID uuid) {
    this(0, uuid);
  }

  public UserMapping(int id) {
    this(id, null);
  }

  public UserMapping(int id, final UUID uuid) {
    this.id = id;
    this.uuid = uuid;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(final UUID uuid) {
    this.uuid = uuid;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  @Override
  public boolean equals(final Object obj) {
    if (super.equals(obj)) {
      return true;
    }
    if (obj == null || !(obj instanceof UserMapping)) {
      return false;
    }
    final UserMapping other = (UserMapping) obj;
    return id == other.id && uuid.equals(other.uuid);
  }

  @Override
  public String toString() {
    return String.format("%s@%d {%d -> %s}", getClass().getSimpleName(), hashCode(), id, uuid);
  }

}
