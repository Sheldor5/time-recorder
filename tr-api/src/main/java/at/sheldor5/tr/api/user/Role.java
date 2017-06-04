package at.sheldor5.tr.api.user;

public enum Role {
  USER(new Role[]{}),
  ADMIN(new Role[]{USER});

  private final Role implies[];

  Role(final Role implies[]) {
    this.implies = implies;
  }

  public Role[] getImplies() {
    return implies;
  }

  public boolean implies(String roleName) {
    if (roleName == null || roleName.isEmpty()) {
      return false;
    }
    roleName = roleName.toUpperCase();
    for (final Role role : Role.values()) {
      if (role.name().equals(roleName)) {
        return true;
      }
    }
    return false;
  }
}
