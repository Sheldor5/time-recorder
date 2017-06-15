package at.sheldor5.tr.api.time;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalConfiguration;
import at.sheldor5.tr.api.utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents a period in the time line.
 * Each {@link Session} has a {@link Record} representing the session's start
 * and a {@link Record} representing the session's end.
 * A session has also a multiplier to value the time between the start and end time.
 *
 * @author Michael Palata
 * @since 1.0.0
 */
public class Session implements Comparable<Session> {

  protected int id;
  protected Project project;
  protected UserMapping userMapping;
  protected LocalDate date;
  protected LocalTime start;
  protected LocalTime end;

  protected double multiplier = 1.0D;

  public Session() {

  }

  public Session(final LocalDate date, final LocalTime start, final LocalTime end) {
    this(null, null, date, start, end);
  }

  public Session(final Project project, final LocalDate date, final LocalTime start) {
    this(project, null, date, start, null);
  }

  public Session(final Project project, final UserMapping userMapping, final LocalDate date, final LocalTime start) {
    this(project, userMapping, date, start, null);
  }

  public Session(final Project project, final UserMapping userMapping, final LocalDate date, final LocalTime start, final LocalTime end) {
    this.project = project;
    this.userMapping = userMapping;
    this.date = date;
    this.start = start;
    this.end = end;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
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

  public LocalTime getStart() {
    return start;
  }

  public void setStart(LocalTime start) {
    this.start = start;
  }

  public LocalTime getEnd() {
    return end;
  }

  public void setEnd(LocalTime end) {
    this.end = end;
  }

  public double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
  }

  /**
   * Get the duration between the start and end time of this session in the configured time unit.
   *
   * @return The duration of this session.
   */
  public long getSummary() {
    if (start == null || end == null) {
      return 0;
    }
    return start.until(end, GlobalConfiguration.MEASURE_UNIT);
  }

  /**
   * Get the valued duration between the start and end time of this session in the configured time unit.
   *
   * @return The valued duration of this session.
   */
  public long getValuedSummary() {
    return (long) (getSummary() * multiplier);
  }

  /**
   * Compare this session to another object.
   *
   * @param other The session to compare.
   * @return Negative if this session starts before the other session or the other session is null,
   *         positive if this session starts after the other session, zero if
   *         the sessions start at the same time.
   */
  @Override
  public final int compareTo(final Session other) {
    if (other == null || start == null) {
      return -1;
    }
    return  start.compareTo(other.start);
  }

  /**
   * Check if a time point is within this session's start and end time.
   *
   * @param time The time to check.
   * @return True if the given time is after this session's start time and
   *         before this session's end time, false otherwise.
   */
  public boolean contains(final LocalTime time) {
    return time != null && time.isAfter(start) && time.isBefore(end);
  }

  public Session split(final LocalTime time) {
    if (time == null || !contains(time)) {
      return null;
    }
    // create session for the second part of the split
    final Session session = new Session();
    session.date = date;
    session.start = time;
    session.end = end;
    session.multiplier = multiplier;

    this.end = time;

    // return following session
    return session;
  }

  /**
   * Get a string representation of this session.
   *
   * @return The string representing this session.
   */
  @Override
  public String toString() {
    return String.format("%s: %s - %s = %s (%d%% = %s)",
        date,
        start,
        end,
        TimeUtils.getHumanReadableSummary(getSummary()),
        (long) (multiplier * 100),
        TimeUtils.getHumanReadableSummary(getValuedSummary()));
  }
  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object to compare to.
   * @return    true if this record and the {@code other} record
   *            have equal date, time and type, false otherwise.
   */
  @Override
  public boolean equals(final Object obj) {
    if (super.equals(obj)) {
      return true;
    }
    if (obj == null || !(obj instanceof Session)) {
      return false;
    }
    final Session session = (Session) obj;
    return date.equals(session.date) && start.equals(session.start) && end.equals(session.end);
  }

}
