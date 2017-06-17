package at.sheldor5.tr.api.time;

import at.sheldor5.tr.api.user.UserMapping;
import java.time.LocalDate;

/**
 * @author Michael Palata
 * @date 17.06.2017
 */
public class Account {

  protected int id;
  protected UserMapping userMapping;
  protected LocalDate date;
  protected long time;

  public Account() {
    this(null);
  }

  public Account(final LocalDate date) {
    this.date = date;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UserMapping getUserMapping() {
    return userMapping;
  }

  public void setUserMapping(UserMapping userMapping) {
    this.userMapping = userMapping;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  @Override
  public boolean equals(final Object obj) {
    if (super.equals(obj)) {
      return true;
    }

    if (obj == null || !(obj instanceof Account)) {
      return false;
    }

    final Account account = (Account) obj;
    try {
      return userMapping.equals(account.userMapping) && date.equals(account.date);
    } catch (final NullPointerException npe) {
      return false;
    }
  }
}
