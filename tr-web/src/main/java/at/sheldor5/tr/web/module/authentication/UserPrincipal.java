package at.sheldor5.tr.web.module.authentication;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class UserPrincipal implements Principal {

  private String name;

  /**
   * Create a SamplePrincipal with a Sample username.
   * <p>
   * <p>
   *
   * @param name the Sample username for this user.
   * @throws NullPointerException if the <code>name</code>
   *                              is <code>null</code>.
   */
  public UserPrincipal(String name) {
    if (name == null) {
      throw new NullPointerException("illegal null input");
    }

    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  /**
   * Compares the specified Object with this <code>SamplePrincipal</code>
   * for equality.  Returns true if the given object is also a
   * <code>SamplePrincipal</code> and the two SamplePrincipals
   * have the same username.
   * <p>
   * <p>
   *
   * @param o Object to be compared for equality with this
   *          <code>SamplePrincipal</code>.
   * @return true if the specified Object is equal equal to this
   * <code>SamplePrincipal</code>.
   */
  public boolean equals(Object o) {
    if (o == null)
      return false;

    if (this == o)
      return true;

    if (!(o instanceof UserPrincipal))
      return false;
    UserPrincipal that = (UserPrincipal) o;

    if (this.getName().equals(that.getName()))
      return true;
    return false;
  }

  /**
   * Return a string representation of this <code>SamplePrincipal</code>.
   *
   * <p>
   *
   * @return a string representation of this <code>SamplePrincipal</code>.
   */
  public String toString() {
    return("UserPrincipal: " + name);
  }

  /**
   * Return a hash code for this <code>SamplePrincipal</code>.
   * <p>
   * <p>
   *
   * @return a hash code for this <code>SamplePrincipal</code>.
   */
  public int hashCode() {
    return name.hashCode();
  }
}
