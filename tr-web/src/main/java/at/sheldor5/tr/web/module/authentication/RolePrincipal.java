package at.sheldor5.tr.web.module.authentication;

import at.sheldor5.tr.api.user.Role;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * @author Michael Palata
 * @date 02.06.2017
 */
public class RolePrincipal implements Principal {

  private Role role;

  public RolePrincipal(final Role role) {
    this.role = role;
  }

  @Override
  public String getName() {
    return role.name();
  }

  @Override
  public boolean implies(final Subject subject) {
    for (final Principal principal : subject.getPrincipals()) {
      if (role.implies(principal.getName())) {
        return true;
      }
    }
    return false;
  }
}
