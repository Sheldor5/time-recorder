package at.sheldor5.tr.api.user;

import java.util.UUID;

public class UserMapping {

  private int id;
  private UUID uuid;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public boolean equals(Object obj) {
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
    return String.format("%d: %s", id, uuid);
  }

}
